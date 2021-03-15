package ufc.quixada.npi.contest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufc.quixada.npi.contest.model.Submissao;
import ufc.quixada.npi.contest.model.Trabalho;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SubmissaoRepository extends CrudRepository<Submissao, Long>{
	@Query("select case when count(*) > 0 then true else false end "
			+ "FROM Submissao as s, Trabalho as t  WHERE s.trabalho.id = t.id and t.evento.id = :idEvento")
	public boolean existTrabalhoNesseEvento(@Param("idEvento") Long idEvento);
	
	@Query("select case when count(*) > 0 then true else false end "
			+ "FROM Submissao as s, Trabalho as t  "
			+ "WHERE s.trabalho.id = t.id "
			+ "and s.tipoSubmissao = ufc.quixada.npi.contest.model.TipoSubmissao.FINAL "
			+ "and t.evento.id = :idEvento")
	public boolean existTrabalhoFinalNesseEvento(@Param("idEvento") Long idEvento);
	
	public Submissao findSubmissaoByTrabalhoId(Long idTrabalho);
	public Submissao findByTrabalho(Trabalho trabalho);
}