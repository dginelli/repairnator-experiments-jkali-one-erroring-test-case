package inciDashboard_e5a.model;

import javax.persistence.Embeddable;

@Embeddable
public class Propiedad {

    private String nombre;
    private String valor;

    public Propiedad() {
    }

    public Propiedad(String nombre, String valor) {
	this.nombre = nombre;
	this.valor = valor;
    }

    public String getNombre() {
	return nombre;
    }

    public void setNombre(String nombre) {
	this.nombre = nombre;
    }

    public String getValor() {
	return valor;
    }

    public void setValor(String valor) {
	this.valor = valor;
    }

}
