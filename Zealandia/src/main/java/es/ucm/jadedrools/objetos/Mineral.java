package es.ucm.jadedrools.objetos;

public class Mineral {

	private TipoMineral tipo;
	private int cantidad;
	
	public Mineral(TipoMineral tipo, int cantidad){
		
		this.tipo = tipo;
		this.cantidad = cantidad;
		
	}

	public TipoMineral getTipo() { return tipo; }

	public int getCantidad() { return cantidad; }
	
}
