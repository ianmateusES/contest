package ufc.quixada.npi.contest.model;

import org.hibernate.validator.constraints.NotEmpty;
import ufc.quixada.npi.contest.repository.TrilhaRepository;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "trilha")
public class Trilha {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@NotEmpty
	@Column(name = "nome")
	private String nome;
	
	@OneToMany(mappedBy="trilha", cascade=CascadeType.REMOVE)
	private List<Trabalho> trabalhos;

	@ManyToOne(cascade=CascadeType.PERSIST)
	private Evento evento;
	
	public List<Trabalho> getTrabalhos() {
		return trabalhos;
	}

	public void setTrabalhos(List<Trabalho> trabalhos) {
		this.trabalhos = trabalhos;
	}


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

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
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
		Trilha other = (Trilha) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public int getNumeroTrabalhos(){
		return this.trabalhos.size();
	}

	public void excluir(Evento evento, TrilhaRepository trilhaRepository) {
		if (null != this && getTrabalhos().isEmpty()) {
			if (null != evento.getTrilhas() && evento.getTrilhas().removeIf(t -> t.getId() == getId())) {
				trilhaRepository.delete(this);
			}
		}
	}

}