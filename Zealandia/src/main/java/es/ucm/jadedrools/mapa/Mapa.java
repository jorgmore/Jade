package es.ucm.jadedrools.mapa;

import es.ucm.jadedrools.objetos.Mineral;

public class Mapa {
	
	public Casilla [][]casillas;
	
	public Mapa(int ancho, int alto){
		
		casillas = new Casilla[ancho][alto];
		
	}
	
	public void setMineral(int x, int y, Mineral m){ 
		
		casillas[x][y].setMineral(m);
		
	}

}
