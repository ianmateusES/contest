package ufc.quixada.npi.contest.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.service.MessageService;

import javax.inject.Named;


@Named
public class EventoValidator implements Validator{
	
	private static final String ERRO_DATA_SUBMISSAO_FINAL = "ERRO_DATA_SUBMISSAO_FINAL";
	private static final String ERRO_DATA_REVISAO_FINAL = "ERRO_DATA_REVISAO_FINAL";
	private static final String ERRO_DATA_REVISAO_INICIAL = "ERRO_DATA_REVISAO_INICIAL";
	private static final String ERRO_DATA_SUBMISSAO_INICIAL = "ERRO_DATA_SUBMISSAO_INICIAL";
	private static final String ERRO_CAMPOS_EVENTO_NULL = "ERRO_CAMPOS_EVENTO_NULL";

	@Autowired
	private MessageService messageService;
	
	private static final String EVENTO_CAMPO_NULL="evento";
	private static final String PRAZO_REVISAO_INICIAL="prazoRevisaoInicial";
	private static final String PRAZO_REVISAO_FINAL="prazoRevisaoFinal";
	private static final String PRAZO_SUBMISSAO_INICIAL="prazoSubmissaoInicial";
	private static final String PRAZO_SUBMISSAO_FINAL="prazoSubmissaoInicial";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Evento.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object object, Errors erros) {
		Evento  evento = (Evento) object;
		if(datasVisibilidadeOuventoNull(evento)){
			erros.rejectValue(null, EVENTO_CAMPO_NULL, messageService.getMessage(ERRO_CAMPOS_EVENTO_NULL));
		}else{
			if (haErrosPrazoSubmissaoInicial(evento)) {
					erros.rejectValue(PRAZO_SUBMISSAO_INICIAL, PRAZO_SUBMISSAO_INICIAL, messageService.getMessage(ERRO_DATA_SUBMISSAO_INICIAL));
			}
			if(haErrosPrazoRevisaoInicial(evento)){
				erros.rejectValue(PRAZO_REVISAO_INICIAL, PRAZO_REVISAO_INICIAL, messageService.getMessage(ERRO_DATA_REVISAO_INICIAL));
			}
			if(haErrosPrazoRevisaoFinal(evento)){
				erros.rejectValue(PRAZO_REVISAO_FINAL, PRAZO_REVISAO_FINAL, messageService.getMessage(ERRO_DATA_REVISAO_FINAL));
			}
			if(haErrosPrazoSubmissaoFinal(evento)){
				erros.rejectValue(PRAZO_SUBMISSAO_FINAL, PRAZO_SUBMISSAO_FINAL, messageService.getMessage(ERRO_DATA_SUBMISSAO_FINAL));
			}
		}
	}

	public boolean datasVisibilidadeOuventoNull(Evento evento){
		return (evento == null || evento.getPrazoNotificacao() == null || evento.getTerminoSubmissao() == null
			|| evento.getCameraReady() == null || evento.getInicioSubmissao() == null
			|| evento.getVisibilidade() == null);
	}
	
	public boolean haErrosPrazoSubmissaoInicial(Evento evento){
		return evento.getInicioSubmissao().after(evento.getTerminoSubmissao())
				|| evento.getInicioSubmissao().after(evento.getPrazoNotificacao())
				|| evento.getInicioSubmissao().after(evento.getCameraReady());
	}
	private boolean haErrosPrazoRevisaoInicial(Evento evento) {
		return evento.getTerminoSubmissao().after(evento.getPrazoNotificacao())
				|| evento.getTerminoSubmissao().after(evento.getCameraReady())
				|| evento.getTerminoSubmissao().before(evento.getInicioSubmissao());
	}
	

	private boolean haErrosPrazoSubmissaoFinal(Evento evento) {
		return evento.getCameraReady().before(evento.getPrazoNotificacao())
				|| evento.getCameraReady().before(evento.getTerminoSubmissao())
				|| evento.getCameraReady().before(evento.getInicioSubmissao());
	}

	private boolean haErrosPrazoRevisaoFinal(Evento evento) {
		return evento.getPrazoNotificacao().after(evento.getCameraReady())
				|| evento.getPrazoNotificacao().before(evento.getTerminoSubmissao())
				|| evento.getPrazoNotificacao().before(evento.getInicioSubmissao());
	}
}