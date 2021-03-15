package ufc.quixada.npi.contest.service;

import br.ufc.quixada.npi.model.Email;
import br.ufc.quixada.npi.model.Email.EmailBuilder;
import br.ufc.quixada.npi.service.SendEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.model.Token;
import ufc.quixada.npi.contest.util.Constants;
import ufc.quixada.npi.contest.validator.ContestException;

@Service
public class EnviarEmailService {
	@Autowired
	private SendEmailService service;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private TokenService tokenService;

	private Logger logger = LoggerFactory.getLogger(EnviarEmailService.class);

	@Autowired
	private JavaMailSender sender;

	@Autowired
	@Qualifier("emailFrom")
	private String from;

	public void enviarMensagem(String assunto, String corpo, String destinatario) {
		try {
			sender.send(constructMail("Contest - " + assunto, corpo, destinatario));
		} catch (MailSendException e) {
			e.printStackTrace();
			logger.error(String.format("Erro ao enviar email para \"%s\"", destinatario));
		}
	}

	private SimpleMailMessage constructMail(String assunto, String corpo, String destinatario) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(assunto);
		email.setText(corpo + "\n\n\nMensagem enviada automaticamente. Por favor, não responder." + "\nEquipe Contest");
		email.setTo(destinatario);
		email.setFrom(from);
		return email;
	}

	public boolean enviarEmail(String titulo, String assunto, String emailDestinatario, String corpo) {

		// EmailBuilder emailBuilder = new
		// EmailBuilder(titulo,Constants.ENDERECO_EMAIL_CONTEST,assunto,emailDestinatario,corpo);

		// Email email = new Email(emailBuilder);

		try {
			// service.sendEmail(email);
			enviarMensagem(assunto, corpo, emailDestinatario);
			return true;
		} catch (MailException e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	public String resetarSenhaEmail(@PathVariable("token") Token token, @RequestParam String senha,
			@RequestParam String senhaConfirma, RedirectAttributes redirectAttributes) {
		if (senha.equals(senhaConfirma)) {
			Pessoa pessoa = token.getPessoa();
			String password = pessoaService.encodePassword(senha);
			pessoa.setPassword(password);
			try {
				pessoaService.addOrUpdate(pessoa);
			} catch (ContestException e) {
				logger.error(e.getMessage());
			}
			tokenService.deletar(token);
			redirectAttributes.addFlashAttribute("senhaRedefinida", true);
		}
		return "";
	}

	public String esqueciSenhaEmail(@RequestParam String email, RedirectAttributes redirectAttributes, String url) {
		Pessoa pessoa = pessoaService.getByEmail(email);
		if (pessoa != null) {
			Token token = tokenService.novoToken(pessoa, Constants.ACAO_RECUPERAR_SENHA);
			String corpo = "Você pode alterar sua senha no link a seguir: " + url + "/resetar-senha/"
					+ token.getToken();
			// enviarEmail("Redefinição de senha", "[Contest] Redefinição de senha", email,
			// corpo);
			enviarMensagem("Redefinição de senha", corpo, email);
		}
		redirectAttributes.addFlashAttribute("esqueciSenha", true);
		return "";
	}

}