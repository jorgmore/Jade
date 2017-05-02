package es.ucm.jadedrools.gui;
import es.ucm.jadedrools.mapa.Mapa;

/**
 * Clase para mostrar los agentes en la GUI.
 * Contiene la posicion y el tipo del agente
 * 
 * @author rodry
 *
 */
public class AgenteDummy {
	
	private int x, y;
	private TipoAgente tipo;
	private Mapa m;

	public AgenteDummy(TipoAgente tipo, int x, int y, Mapa m){
		
		this.tipo = tipo;
		this.x = x;
		this.y = y;
		this.m = m;
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public TipoAgente getTipo() {
		return tipo;
	}
	
	public Mapa getMapa() {
		return this.m;
	}
	
}