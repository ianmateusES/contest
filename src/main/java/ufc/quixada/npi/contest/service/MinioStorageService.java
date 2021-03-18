package ufc.quixada.npi.contest.service;

import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;
import ufc.quixada.npi.contest.model.Arquivo;
import ufc.quixada.npi.contest.repository.ArquivoRepository;
import ufc.quixada.npi.contest.validator.StorageException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class MinioStorageService implements StorageService {

	@Autowired
	private MessageService messsagemService;

	@Autowired
	private ArquivoRepository arquivoRepository;

	@Autowired
	private MinioClient minioClient;

	@Value("${minio.bucket}")
	private String bucket;

	@Override
	public Arquivo store(MultipartFile file) {
		if (file.isEmpty()) {
			throw new StorageException(messsagemService.getMessage("ARQUIVO_VAZIO"));
		} else if (!Objects.equals(file.getContentType(), "application/pdf")) {
			throw new StorageException(messsagemService.getMessage("FORMATO_ARQUIVO_INVALIDO"));
		}

		Arquivo arquivo = arquivoRepository.save(new Arquivo(file.getOriginalFilename(), file.getContentType()));

		try {
			minioClient.putObject(bucket, String.valueOf(getIdArquivo(arquivo)), file.getInputStream(), file.getSize(), null,
					null, file.getContentType());
		} catch (InvalidBucketNameException | NoSuchAlgorithmException | IOException | InvalidKeyException
				| NoResponseException | XmlPullParserException | ErrorResponseException | InternalException
				| InvalidArgumentException | InsufficientDataException | InvalidResponseException e) {
			arquivoRepository.delete(arquivo);
			throw new StorageException(messsagemService.getMessage("FALHA_UPLOAD_ARQUIVO"));
		}

		return arquivo;
	}

	@Override
	public Arquivo load(Integer arquivoId) {
		Arquivo arquivo = arquivoRepository.getOne(arquivoId);

		try {
			arquivo.setConteudo(minioClient.getObject(bucket, String.valueOf(getIdArquivo(arquivo))));
		} catch (InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException | IOException
				| InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException
				| InternalException | InvalidArgumentException | InvalidResponseException e) {
			throw new StorageException(messsagemService.getMessage("FALHA_LEITURA_ARQUIVO"));
		}

		return arquivo;
	}

	@Override
	public Arquivo edit(MultipartFile file, Integer arquivoId) {
		if (file.isEmpty()) {
			throw new StorageException(messsagemService.getMessage("ARQUIVO_VAZIO"));
		} else if (!Objects.equals(file.getContentType(), "application/pdf")) {
			throw new StorageException(messsagemService.getMessage("FORMATO_ARQUIVO_INVALIDO"));
		}

		Arquivo arquivo = arquivoRepository.getOne(arquivoId);
		arquivo.setNome(file.getOriginalFilename());
		arquivo.setFormato(file.getContentType());
		arquivoRepository.save(arquivo);

		try {
			minioClient.putObject(bucket, String.valueOf(getIdArquivo(arquivo)), file.getInputStream(), file.getSize(), null,
					null, file.getContentType());
		} catch (InvalidBucketNameException | NoSuchAlgorithmException | IOException | InvalidKeyException
				| NoResponseException | XmlPullParserException | ErrorResponseException | InternalException
				| InvalidArgumentException | InsufficientDataException | InvalidResponseException e) {
			throw new StorageException(messsagemService.getMessage("FALHA_UPLOAD_ARQUIVO"));
		}

		return arquivo;
	}

	@Override
	public void delete(Integer arquivoId) {
		Arquivo arquivo = arquivoRepository.getOne(arquivoId);

		if (null == arquivo) {
			throw new StorageException(messsagemService.getMessage("FALHA_LEITURA_ARQUIVO"));
		}

		arquivoRepository.delete(arquivo);

		try {
			minioClient.removeObject(bucket, String.valueOf(getIdArquivo(arquivo)));
		} catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException
				| NoResponseException | ErrorResponseException | InternalException | InvalidArgumentException
				| InvalidResponseException | IOException | XmlPullParserException e) {
			throw new StorageException(messsagemService.getMessage("FALHA_UPLOAD_ARQUIVO"));
		}
	}
	
	private Integer getIdArquivo(Arquivo arquivo) {
		return arquivo.getId();
	}

}