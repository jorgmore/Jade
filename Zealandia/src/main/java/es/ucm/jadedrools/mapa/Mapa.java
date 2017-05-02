package es.ucm.jadedrools.mapa;

import java.util.concurrent.ThreadLocalRandom;

import es.ucm.jadedrools.objetos.Mineral;
import es.ucm.jadedrools.objetos.TipoMineral;


/**
 * Clase que implementa el mapa.
 * 
 * @author rodry
 *
 */
public class Mapa {
	
	private Casilla [][]casillas;
	private int ancho;
	private int alto;
	private int x;
	private int y;
	private boolean TieneMineral;
	
	public Mapa(int ancho, int alto, int x, int y){
		
		this.ancho = ancho;
		this.alto = alto;
		this.x = x;
		this.y = y;
		this.TieneMineral = false;
		casillas = new Casilla[ancho][alto];
		
		for (int i = 0; i < ancho; i++){
			for (int j = 0; j < alto; j++){
				
				casillas[i][j] = new Casilla();
				
				// Hacemos un random para ver que tipo de casilla es y si contiene algo
				int tipoCasilla = ThreadLocalRandom.current().nextInt(0, 2 + 1);		
				if (tipoCasilla == 1)
					casillas[i][j].setTipo(TipoCasilla.PANTANOSO);
				else if (tipoCasilla == 2)
					casillas[i][j].setTipo(TipoCasilla.PROHIBIDA);
				
				if (casillas[i][j].getTipo() != TipoCasilla.PROHIBIDA){
				
					int mineral = ThreadLocalRandom.current().nextInt(0, 10 + 1);
					if (mineral == 1)
						casillas[i][j].setMineral(new Mineral(TipoMineral.COBRE, 10));
					else if (mineral == 2)
						casillas[i][j].setMineral(new Mineral(TipoMineral.PLATA, 10));
					else if (mineral == 3)
						casillas[i][j].setMineral(new Mineral(TipoMineral.ORO, 10));
					
				}
			}
		}
	}
	
	public int getAncho(){ return ancho; }
	
	public int getAlto(){ return alto; }
	
	public int getX(){ return x; }
	
	public int getY(){ return y; }
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public Casilla getCasilla(int x, int y){ return casillas[x][y]; }
	
	public void setCasillaRecorrida(int x, int y){
		this.casillas[x][y].setCasillaRecorrida();
	}
	
	public void setMineral(int x, int y, Mineral m){ 
		
		casillas[x][y].setMineral(m);
		
	}
	
	public void setTieneMineral(boolean tiene){
		this.TieneMineral = tiene;
	}
	
	public boolean getTieneMineral(){
		return this.TieneMineral;
	}
}