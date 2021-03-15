package ufc.quixada.npi.contest.model;


import java.util.List;

public class TrabalhoProduct {
	private List<Revisao> revisoes;

	public List<Revisao> getRevisoes() {
		return revisoes;
	}

	public void setRevisoes(List<Revisao> revisoes) {
		this.revisoes = revisoes;
	}

	public boolean isRevisado() {
		if (this.revisoes == null || this.revisoes.isEmpty()) {
			return false;
		}
		for (Revisao revisao : revisoes) {
			if (!revisao.isRevisado()) {
				return false;
			}
		}
		return true;
	}

	public boolean isAprovado() {
		if (this.revisoes == null || this.revisoes.isEmpty()) {
			return false;
		}
		for (Revisao revisao : this.revisoes) {
			if (!isRevisado() || !revisao.getAvaliacao().equals(Avaliacao.APROVADO)) {
				return false;
			}
		}
		return true;
	}

	public boolean isReprovado() {
		if (this.revisoes == null || this.revisoes.isEmpty()) {
			return false;
		}
		for (Revisao revisao : this.revisoes) {
			if (isRevisado() && revisao.getAvaliacao().equals(Avaliacao.REPROVADO)) {
				return true;
			}
		}
		return false;
	}

	public boolean isAprovadoComRessalvas() {
		if (this.revisoes == null || this.revisoes.isEmpty()) {
			return false;
		}
		for (Revisao revisao : this.revisoes) {
			if (isRevisado() && !revisao.getAvaliacao().equals(Avaliacao.REPROVADO)
					&& revisao.getAvaliacao().equals(Avaliacao.RESSALVAS)) {
				return true;
			}
		}
		return false;
	}

	public Avaliacao getResultado() {
		return isAprovado() ? Avaliacao.APROVADO
				: isAprovadoComRessalvas() ? Avaliacao.RESSALVAS
						: isReprovado() ? Avaliacao.REPROVADO : Avaliacao.NAO_REVISADO;
	}

	public boolean isBestPaper() {
		for (Revisao revisao : this.revisoes) {
			if (revisao.getIndicacao()) {
				return true;
			}
		}
		return false;
	}
}