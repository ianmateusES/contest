package ufc.quixada.npi.contest.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "evento")
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotEmpty(message = "{NOME_EVENTO_VAZIO_ERROR}")
	@Column(name = "nome")
	private String nome;

	@Column(name = "descricao")
	private String descricao;

	private String codigo;

	@Enumerated(EnumType.STRING)
	@Column(name = "visibilidade")
	private VisibilidadeEvento visibilidade;

	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	private EstadoEvento estado;

	@Temporal(TemporalType.DATE)
	@Column(name = "inicio_submissao")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date inicioSubmissao;

	@Temporal(TemporalType.DATE)
	@Column(name = "camera_ready")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date cameraReady;

	@Temporal(TemporalType.DATE)
	@Column(name = "termino_submissao")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date terminoSubmissao;

	@Temporal(TemporalType.DATE)
	@Column(name = "prazo_notificacao")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date prazoNotificacao;

	@ManyToMany
	@JoinTable(name = "organizadores_evento", joinColumns = { @JoinColumn(name = "evento_id") }, inverseJoinColumns = {
			@JoinColumn(name = "organizador_id") })
	private List<Pessoa> organizadores;

	@ManyToMany
	@JoinTable(name = "revisores_evento", joinColumns = { @JoinColumn(name = "evento_id") }, inverseJoinColumns = {
			@JoinColumn(name = "revisor_id") })
	private List<Pessoa> revisores;

	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = false)
	@OrderBy("nome ASC")
	private List<Trilha> trilhas;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
	@OrderBy("nome ASC")
	@JoinTable(name = "modalidade_submissao_evento", joinColumns = @JoinColumn(name = "evento_id"), inverseJoinColumns = @JoinColumn(name = "modalidade_id"))
	private List<Modalidade> modalidadesSubmissao;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = false)
	@OrderBy("nome ASC")
	@JoinTable(name = "modalidade_apresentacao_evento", joinColumns = @JoinColumn(name = "evento_id"), inverseJoinColumns = @JoinColumn(name = "modalidade_id"))
	private List<Modalidade> modalidadesApresentacao;

	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = false)
	@OrderBy("nome ASC")
	private List<Sessao> sessoes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public VisibilidadeEvento getVisibilidade() {
		return visibilidade;
	}

	public void setVisibilidade(VisibilidadeEvento visibilidade) {
		this.visibilidade = visibilidade;
	}

	public EstadoEvento getEstado() {
		return estado;
	}

	public void setEstado(EstadoEvento estado) {
		this.estado = estado;
	}

	public Date getInicioSubmissao() {
		return inicioSubmissao;
	}

	public void setInicioSubmissao(Date inicioSubmissao) {
		this.inicioSubmissao = inicioSubmissao;
	}

	public Date getUltimoDiaSubmissaoInicial() {
		Calendar c = Calendar.getInstance();
		c.setTime(terminoSubmissao);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
	}

	public Date getCameraReady() {
		return cameraReady;
	}

	public void setCameraReady(Date cameraReady) {
		this.cameraReady = cameraReady;
	}

	public Date getTerminoSubmissao() {
		return terminoSubmissao;
	}

	public void setTerminoSubmissao(Date terminoSubmissao) {
		this.terminoSubmissao = terminoSubmissao;
	}

	public Date getPrazoNotificacao() {
		return prazoNotificacao;
	}

	public void setPrazoNotificacao(Date prazoNotificacao) {
		this.prazoNotificacao = prazoNotificacao;
	}

	public List<Pessoa> getOrganizadores() {
		return organizadores;
	}

	public void setOrganizadores(List<Pessoa> organizadores) {
		this.organizadores = organizadores;
	}

	public List<Pessoa> getRevisores() {
		return revisores;
	}

	public void setRevisores(List<Pessoa> revisores) {
		this.revisores = revisores;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
		Evento other = (Evento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Evento [id=" + id + ", nome=" + nome + ", descricao=" + descricao + ", visibilidade=" + visibilidade
				+ ", estado=" + estado + ", prazoSubmissaoInicial=" + inicioSubmissao + ", prazoSubmissaoFinal="
				+ cameraReady + ", prazoRevisaoInicial=" + terminoSubmissao + ", prazoRevisaoFinal=" + prazoNotificacao
				+ ", organizadores=" + organizadores + ", revisores=" + revisores + ", trilhas=" + trilhas
				+ ", sessoes=" + sessoes + "]";
	}

	public List<Trilha> getTrilhas() {
		return trilhas;
	}

	public void setTrilhas(List<Trilha> trilhas) {
		if (this.trilhas == null) {
			this.trilhas = trilhas;
		}
		this.trilhas.clear();
		this.trilhas.addAll(trilhas);
	}

	public List<Modalidade> getModalidadesSubmissao() {
		return modalidadesSubmissao;
	}

	public void addModalidadeSubmissao(Modalidade modalidadeSubmissao) {
		this.modalidadesSubmissao.add(modalidadeSubmissao);
	}

	public void removeModalidadeSubmissao(Modalidade modalidadeSubmissao) {
		this.modalidadesSubmissao.remove(modalidadeSubmissao);
	}

	public List<Modalidade> getModalidadesApresentacao() {
		return modalidadesApresentacao;
	}

	public void addModalidadeApresentacao(Modalidade modalidadeApresentacao) {
		this.modalidadesApresentacao.add(modalidadeApresentacao);
	}

	public void removeModalidadeApresentacao(Modalidade modalidadeApresentacao) {
		this.modalidadesApresentacao.remove(modalidadeApresentacao);
	}

	public boolean isPeriodoSubmissao() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date hoje = new Date();
			hoje = formatter.parse(formatter.format(hoje));
			return !(hoje.before(inicioSubmissao) || hoje.after(terminoSubmissao));
		} catch (ParseException e) {
			return false;
		}

	}

	public boolean isPeriodoRevisao() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date hoje = new Date();
			hoje = formatter.parse(formatter.format(hoje));
			return !(hoje.after(prazoNotificacao) && !terminoSubmissao.after(hoje));
		} catch (ParseException e) {
			return false;
		}
	}

	public boolean isPeriodoFinal() {
		Date dataAtual = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(prazoNotificacao);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date diaAposRevisaoFinal = cal.getTime();
		boolean comecaAposRevisaoFinal = (dataAtual.compareTo(diaAposRevisaoFinal) >= 0);

		cal.setTime(cameraReady);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date diaAposSubmissaoFinal = cal.getTime();
		boolean terminaNoDiaOuAntesSubissaoFinal = (dataAtual.compareTo(diaAposSubmissaoFinal) <= 0);
		return (comecaAposRevisaoFinal && terminaNoDiaOuAntesSubissaoFinal);
	}

	public boolean isAfterRevisao() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date hoje = new Date();
			hoje = formatter.parse(formatter.format(hoje));
			return hoje.after(prazoNotificacao);
		} catch (ParseException e) {
			return false;
		}
	}

	public boolean isAfterSubmissao() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date hoje = new Date();
			hoje = formatter.parse(formatter.format(hoje));
			return hoje.after(terminoSubmissao);
		} catch (ParseException e) {
			return false;
		}
	}

	public boolean isPeriodoSubmissaoFinal() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date hoje = new Date();
			hoje = formatter.parse(formatter.format(hoje));

			Calendar c = Calendar.getInstance();
			c.setTime(cameraReady);
			c.add(Calendar.DATE, 1);
			Date termino = c.getTime();

			return (hoje.after(prazoNotificacao) && hoje.before(termino));
		} catch (ParseException e) {
			return false;
		}
	}

	public List<Sessao> getSessoes() {
		return sessoes;
	}

	public void setSessoes(List<Sessao> sessoes) {
		if (this.sessoes == null) {
			this.sessoes = sessoes;
		}
		this.sessoes.clear();
		this.sessoes.addAll(sessoes);
	}

	public boolean isOrganizador(Object object) {
		Pessoa pessoa = (Pessoa) object;
		return this.organizadores.contains(pessoa);
	}

	public boolean isRevisor(Object object) {
		Pessoa pessoa = (Pessoa) object;
		return this.revisores.contains(pessoa);
	}

}