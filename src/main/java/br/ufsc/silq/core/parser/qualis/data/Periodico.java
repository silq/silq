package br.ufsc.silq.core.parser.qualis.data;

public class Periodico {

	public String issn, titulo, estrato, area, status;

	public Periodico() {

	}

	public Periodico(String issn, String titulo, String estrato, String area, String status) {
		this.issn = issn;
		this.titulo = titulo;
		this.estrato = estrato;
		this.area = area;
		this.status = status;
	}

	@Override
	public String toString() {
		return issn + " " + titulo + " " + estrato + " " + area + " " + status;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEstrato() {
		return estrato;
	}

	public void setEstrato(String estrato) {
		this.estrato = estrato;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
