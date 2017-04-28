package es.ucm.jadedrools.mapa;

import java.util.Vector;

import es.ucm.jadedrools.objetos.Mineral;

public class Casilla {
	
	private Vector<Mineral> minerales; // Puede ser vacio
	
	public Casilla(){ minerales = new Vector<>(); }
	
	public void setMineral(Mineral m){ minerales.add(m); }
	// public void deleteMineral( ?? ){  }
	
	public Vector<Mineral> getMinerales(){ return minerales; }
	

}
