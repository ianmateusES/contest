package ufc.quixada.npi.contest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.repository.PessoaRepository;
import ufc.quixada.npi.contest.validator.ContestException;

import java.util.List;

@Service
public class PessoaService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private MessageService messsagemService;
	
	public void addOrUpdate(Pessoa pessoa) throws ContestException {
		List<Pessoa> pessoas = pessoaRepository.findByCpfOrEmailAndIdIsNot(pessoa.getCpf(), pessoa.getEmail(), pessoa.getId() == null ? 0 : pessoa.getId());

		if(!pessoas.isEmpty()) {
			throw new ContestException(messsagemService.getMessage("ERRO_USUARIO_EXISTENTE"));
		}

		pessoaRepository.save(pessoa);
	}

	public List<Pessoa> list() {
		return pessoaRepository.findAll();
	}

	public boolean delete(Long id) {
		if (pessoaRepository.findOne(id) != null) {
			pessoaRepository.delete(id);
			return true;
		}

		return false;
	}

	public Pessoa get(Long id) {
		return pessoaRepository.findOne(id);
	}

	public Pessoa getByCpf(String cpf) {
		return pessoaRepository.findByCpf(cpf);
	}

	public Pessoa getByEmail(String email){
		return pessoaRepository.findByEmail(email);
	}
	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	public List<Pessoa> findByNome(String search) {
		return pessoaRepository.findByNomeContainingIgnoreCaseOrderByNome(search);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}