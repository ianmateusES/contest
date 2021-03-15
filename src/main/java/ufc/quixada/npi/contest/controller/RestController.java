package ufc.quixada.npi.contest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.*;
import ufc.quixada.npi.contest.service.*;

import java.util.Date;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private AtividadeService atividadeService;

    @Autowired
    private ParticipacaoService participacaoService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private TrabalhoService trabalhoService;

    @RequestMapping("/users/findByNome")
    public List<Pessoa> getAllByName(@RequestParam(value = "search", required = false) String search) {
        return pessoaService.findByNome(search);
    }

    @RequestMapping("/revisores/{evento}")
    public List<Pessoa> getRevisoresByEvento(@PathVariable Evento evento, @RequestParam(value = "search", required = false) String nome) {
        return eventoService.findRevisores(evento, nome);
    }

    @PreAuthorize("#atividade != null && #atividade.evento.isOrganizador(authentication.principal)")
    @RequestMapping("/atividades/status/{atividade}")
    public void atualizarStatusAtividade(@PathVariable Atividade atividade, @RequestParam(value = "ativo", required = false) boolean ativo) {
        atividade.setAtivo(ativo);
        atividadeService.adicionarOuAtualizar(atividade);
    }

    @PreAuthorize("#participacao != null && #participacao.atividade.evento.isOrganizador(authentication.principal)")
    @RequestMapping("/atividades/participacao/{participacao}")
    public void atualizarStatusParticipacao(@PathVariable Participacao participacao, @RequestParam(value = "valido", required = false) boolean valido) {
        participacao.setValida(valido);
        participacaoService.atualizar(participacao);
    }

    @PreAuthorize("#trabalho.evento.isOrganizador(authentication.principal) || #trabalho.sessao.isReponsavel(authentication.principal)")
    @RequestMapping("/apresentacao/{idTrabalho}/{status}")
    public void informarApresentacao(@PathVariable("idTrabalho") Trabalho trabalho, @PathVariable("status") boolean status, Authentication auth) {
        if (trabalho != null) {
            trabalho.setResponsavelApresentacao((Pessoa)auth.getPrincipal());
            trabalho.setDataApresentacao(new Date());
            trabalho.setStatusApresentacao(status);
            trabalhoService.atualizar(trabalho);
        }
    }

}
