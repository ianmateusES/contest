package ufc.quixada.npi.contest.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

import ufc.quixada.npi.contest.util.PessoaLogadaUtil;

@Entity
@Table(name = "trabalho")
public class Trabalho implements Comparable<Trabalho> {

	private TrabalhoProduct2 trabalhoProduct2 = new TrabalhoProduct2();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotEmpty
	@Column(name = "titulo")
	private String titulo;

	@Column(name = "resumo", columnDefinition = "TEXT")
	private String resumo;

	@Column(name = "palavras_chave", columnDefinition = "TEXT")
	private String palavrasChave;

	@ManyToOne
	private Pessoa autor;

	@ManyToOne
	private Pessoa orientador;

	@ManyToMany
	@JoinTable(name = "coautores_trabalho", joinColumns = { @JoinColumn(name = "trabalho_id") }, inverseJoinColumns = {
			@JoinColumn(name = "coautor_id") })
	private List<Pessoa> coautores;

	@ManyToMany
	@JoinTable(name = "revisores_trablho", joinColumns = { @JoinColumn(name = "trabalho_id") }, inverseJoinColumns = {
			@JoinColumn(name = "revisor_id") })
	private List<Pessoa> revisores;

	@ManyToMany
	@JoinTable(name = "bolsistas_trablho", joinColumns = { @JoinColumn(name = "trabalho_id") }, inverseJoinColumns = {
			@JoinColumn(name = "bolsista_id") })
	private List<Pessoa> bolsistas;

	@ManyToOne
	private Evento evento;

	@ManyToOne
	private Trilha trilha;

	private boolean statusApresentacao;

	@ManyToOne
	private Pessoa responsavelApresentacao;

	private Date dataApresentacao;

	@ManyToOne
	@JoinColumn(name = "secao_id")
	private Sessao sessao;

	@ManyToOne
	private Modalidade modalidadeApresentacao;

	@ManyToOne
	private Modalidade modalidadeSubmissao;

	@OneToOne
	private Arquivo arquivo;

	@Column(name = "criada_em")
	@Temporal(TemporalType.DATE)
	private Date criadaEm;

	@Column(name = "atualizada_em")
	@Temporal(TemporalType.DATE)
	private Date atualizadaEm;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Revisao> getRevisoes() {
		return trabalhoProduct2.getTrabalhoProduct().getRevisoes();
	}

	public void setRevisoes(List<Revisao> revisoes) {
		trabalhoProduct2.getTrabalhoProduct().setRevisoes(revisoes);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public Pessoa getAutor() {
		return autor;
	}

	public void setAutor(Pessoa autor) {
		this.autor = autor;
	}

	public Pessoa getOrientador() {
		return orientador;
	}

	public void setOrientador(Pessoa orientador) {
		this.orientador = orientador;
	}

	public List<Pessoa> getCoautores() {
		return coautores;
	}

	public void setCoautores(List<Pessoa> coautores) {
		this.coautores = coautores;
	}

	public List<Pessoa> getRevisores() {
		return revisores;
	}

	public void setRevisores(List<Pessoa> revisores) {
		this.revisores = revisores;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public void setTrilha(Trilha trilha) {
		this.trilha = trilha;
	}

	public Modalidade getModalidadeApresentacao() {
		return modalidadeApresentacao;
	}

	public Pessoa getResponsavelApresentacao() {
		return responsavelApresentacao;
	}

	public void setResponsavelApresentacao(Pessoa responsavelApresentacao) {
		this.responsavelApresentacao = responsavelApresentacao;
	}

	public Date getDataApresentacao() {
		return dataApresentacao;
	}

	public void setDataApresentacao(Date dataApresentacao) {
		this.dataApresentacao = dataApresentacao;
	}

	public void setModalidadeApresentacao(Modalidade modalidadeApresentacao) {
		this.modalidadeApresentacao = modalidadeApresentacao;
	}

	public Modalidade getModalidadeSubmissao() {
		return modalidadeSubmissao;
	}

	public void setModalidadeSubmissao(Modalidade modalidadeSubmissao) {
		this.modalidadeSubmissao = modalidadeSubmissao;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public Date getCriadaEm() {
		return criadaEm;
	}

	public void setCriadaEm(Date criadaEm) {
		this.criadaEm = criadaEm;
	}

	public Date getAtualizadaEm() {
		return atualizadaEm;
	}

	public void setAtualizadaEm(Date atualizadaEm) {
		this.atualizadaEm = atualizadaEm;
	}

	public boolean isChefeSessao(Pessoa pessoa) {
		return this.sessao != null && pessoa.equals(this.sessao.getResponsavel());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trabalho other = (Trabalho) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Trabalho o) {
		return this.titulo.toUpperCase().compareTo(o.getTitulo().toUpperCase());
	}

	public Sessao getSessao() {
		return sessao;
	}

	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}

	public Trilha getTrilha() {
		return trilha;
	}

	public String getPalavrasChave() {
		return palavrasChave;
	}

	public void setPalavrasChave(String palavrasChave) {
		this.palavrasChave = palavrasChave;
	}

	public boolean isAutorOuCoautor() {
		Long idPessoaLogada = PessoaLogadaUtil.pessoaLogada().getId();

		return (checkIsAutor(idPessoaLogada) || checkIsCoautor(idPessoaLogada));
	}

	public boolean checkIsAutor(Long idPessoaLogada) {
		if (idPessoaLogada.equals(autor.getId())) {
			return true;
		}
		return false;
	}

	public boolean checkIsCoautor(Long idPessoaLogada) {
		if (null != coautores && !coautores.isEmpty()) {
			for (Pessoa pessoa : coautores) {
				if (idPessoaLogada.equals(pessoa.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isAutor(Pessoa pessoa) {
		return this.getAutor().equals(pessoa);
	}

	public boolean isCoautor(Pessoa pessoa) {
		return this.coautores != null && this.coautores.contains(pessoa);
	}

	public boolean getStatusApresentacao() {
		return statusApresentacao;
	}

	public void setStatusApresentacao(boolean statusApresentacao) {
		this.statusApresentacao = statusApresentacao;
	}

	public boolean isRevisado() {
		return trabalhoProduct2.getTrabalhoProduct().isRevisado();
	}

	public boolean isAprovado() {
		return trabalhoProduct2.getTrabalhoProduct().isAprovado();
	}

	public boolean isReprovado() {
		return trabalhoProduct2.getTrabalhoProduct().isReprovado();
	}

	public boolean isAprovadoComRessalvas() {
		return trabalhoProduct2.getTrabalhoProduct().isAprovadoComRessalvas();
	}

	public boolean isBestPaper() {
		return trabalhoProduct2.getTrabalhoProduct().isBestPaper();
	}

	public boolean isAlocadoSessao() {
		return sessao != null;
	}

	public Revisao getRevisao(Pessoa revisor) {
		return trabalhoProduct2.getRevisao(revisor);
	}

	public boolean isRevisor(Pessoa pessoa) {
		return trabalhoProduct2.isRevisor(pessoa);
	}

	public String getCoautoresInString() {
		List<Pessoa> lista = this.getCoautores();
		if (lista != null) {
			StringBuilder nomes = new StringBuilder();
			for (Pessoa p : lista) {
				nomes.append(p.getNome().toUpperCase());
				if (lista.indexOf(p) != (lista.size() - 1)) {
					nomes.append(", ");
				}
			}
			return nomes.toString();
		}
		return "";
	}

	public Avaliacao getResultado() {
		return trabalhoProduct2.getTrabalhoProduct().getResultado();
	}

	public boolean canViewFile(Pessoa usuario) {
		return this.getAutor().equals(usuario) || this.isCoautor(usuario) || trabalhoProduct2.isRevisor(usuario)
				|| this.getEvento().isOrganizador(usuario)
				|| (this.getSessao() != null && usuario.equals(this.getSessao().getResponsavel()));
	}

	public List<Pessoa> getBolsistas() {
		return bolsistas;
	}

	public void setBolsistas(List<Pessoa> bolsistas) {
		this.bolsistas = bolsistas;
	}

	public boolean isBolsista(Pessoa pessoa) {
		return this.bolsistas != null && this.bolsistas.contains(pessoa);
	}

}