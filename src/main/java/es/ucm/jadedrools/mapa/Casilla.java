package es.ucm.jadedrools.mapa;

import java.util.Vector;

import es.ucm.jadedrools.objetos.Mineral;

public class Casilla {
	
	private TipoCasilla tipo;
	private Vector<Mineral> minerales; // Puede ser vacio
	
	public Casilla(){ 
		
		minerales = new Vector<>(); 
		tipo = TipoCasilla.NORMAL;
		
	}
	
	public void setTipo(TipoCasilla tipo){ this.tipo = tipo; }
	
	public TipoCasilla getTipo(){ return tipo; }
	
	public void setMineral(Mineral m){ minerales.add(m); }
	// public void deleteMineral( ?? ){  }
	
	public Vector<Mineral> getMinerales(){ return minerales; }
	
	public void setCasillaRecorrida(){
		//this.tipo = TipoCasilla.RECORRIDA;
	}
	
	public void minar(int cant, int pos){
		if (pos > minerales.size() || minerales.isEmpty())
			return;
		int nuevaCantidad = minerales.get(pos).getCantidad() - cant;
		if (nuevaCantidad > 0)
			minerales.get(pos).setCantidad(nuevaCantidad);
		else
			minerales.remove(pos);
	}
}