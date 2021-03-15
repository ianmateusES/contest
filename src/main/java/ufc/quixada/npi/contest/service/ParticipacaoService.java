package ufc.quixada.npi.contest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufc.quixada.npi.contest.model.Atividade;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Participacao;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.repository.ParticipacaoRepository;
import ufc.quixada.npi.contest.repository.PessoaRepository;
import ufc.quixada.npi.contest.validator.ContestException;

import java.util.Date;
import java.util.List;

@Service
public class ParticipacaoService {

    @Autowired
    private ParticipacaoRepository participacaoRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AtividadeService atividadeService;

    public void registrarParticipacaoComoParticipante(Participacao participacao) throws ContestException {
        Pessoa participante = userDetailsService.autenticar(participacao.getParticipante().getEmail(), participacao.getParticipante().getPassword());
        if(participante == null) {
            throw new ContestException("O login ou a senha não correspondem!");
        }

        Atividade atividade = atividadeService.getAtividadePorCodigo(participacao.getAtividade().getCodigo());
        registrarParticipacao(atividade, participante, participante);
    }

    public void registrarParticipacao(Atividade atividade, Pessoa participante, Pessoa responsavel) throws ContestException {
        if(atividade == null) {
            throw new ContestException("Atividade não encontrada!");
        }

        if (participacaoRepository.findByAtividadeAndParticipante(atividade, participante) != null) {
            throw new ContestException("Já existe inscrição para esta atividade!");
        }

        List<Participacao> participacoesConcomitantes = participacaoRepository.findParticipacoesConcomitantesByParticipante(participante, atividade.getInicio(), atividade.getTermino());
        if(participacoesConcomitantes != null && !participacoesConcomitantes.isEmpty()) {
            throw new ContestException("Foram encontradas participações já registradas em atividades no mesmo horário!");
        }

        Date dataHoraAtual = new Date();
        if (!atividade.getEvento().isOrganizador(responsavel)) {
            if (((atividade.getInicio() != null && dataHoraAtual.before(atividade.getInicio()) || (atividade.getTermino() != null && dataHoraAtual.after(atividade.getTermino()))) && !atividade.isAtivo())) {
                throw new ContestException("A atividade não está no período ativo!");
            }
        }

        Participacao participacao = new Participacao();
        participacao.setAtividade(atividade);
        participacao.setValida(true);
        participacao.setData(new Date());
        participacao.setResponsavel(responsavel);
        participacao.setParticipante(participante);
        participacaoRepository.save(participacao);
    }

    public void atualizar(Participacao participacao) {
        participacaoRepository.save(participacao);
    }

    public List<Participacao> findByParticipante(Pessoa participante, Evento evento) {
        return participacaoRepository.findByParticipanteAndEvento(participante, evento);
    }
}
