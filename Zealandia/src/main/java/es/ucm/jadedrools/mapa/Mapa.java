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
	
	public Mapa(int ancho, int alto){
		
		this.ancho = ancho;
		this.alto = alto;
		casillas = new Casilla[ancho][alto];
		
		for (int i = 0; i < ancho; i++){
			for (int j = 0; j < alto; j++){
				
				casillas[i][j] = new Casilla();
				
				// Hacemos un random para ver que tipo de casilla es y si contiene algo
				int tipoCasilla = ThreadLocalRandom.current().nextInt(0, 20 + 1);		
				if (tipoCasilla == 1)
					casillas[i][j].setTipo(TipoCasilla.PANTANOSO);
				else if (tipoCasilla == 2)
					casillas[i][j].setTipo(TipoCasilla.PROHIBIDA);
				
				if (casillas[i][j].getTipo() != TipoCasilla.PROHIBIDA){
				
					int mineral = ThreadLocalRandom.current().nextInt(0, 50 + 1);
					int cantidad = ThreadLocalRandom.current().nextInt(0, 50 + 1);
					if (mineral == 1)
						casillas[i][j].setMineral(new Mineral(TipoMineral.COBRE, cantidad));
					else if (mineral == 2)
						casillas[i][j].setMineral(new Mineral(TipoMineral.PLATA, cantidad));
					else if (mineral == 3)
						casillas[i][j].setMineral(new Mineral(TipoMineral.ORO, cantidad));
					
				}
			}
		}
	}
	
	public int getAncho(){ return ancho; }
	
	public int getAlto(){ return alto; }
	
	public Casilla getCasilla(int x, int y){ return casillas[x][y]; }
	
	public void setMineral(int x, int y, Mineral m){ 
		
		casillas[x][y].setMineral(m);
		
	}
	
	public boolean enRango(int x, int y){
		
		return x > 0 && x < ancho && y > 0 && y < alto && casillas[x][y].getTipo() != TipoCasilla.PROHIBIDA;
		
	}
}