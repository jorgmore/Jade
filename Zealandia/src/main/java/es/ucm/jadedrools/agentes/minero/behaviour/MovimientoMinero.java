package es.ucm.jadedrools.agentes.minero.behaviour;

import es.ucm.jadedrools.agentes.minero.Minero;
import es.ucm.jadedrools.mapa.Mapa;
import jade.core.behaviours.Behaviour;

public class MovimientoMinero extends Behaviour {
	
	private int x;
	private int y;
	private Mapa mapa;
	
	public MovimientoMinero(int x, int y, Mapa mapa) {
		
		this.x = x;
		this.y = y;
		this.mapa = mapa;
	
	}

	@Override
	public void action() {
		
		Minero minero = (Minero)myAgent;
		
		int currX = minero.getX();
		int currY = minero.getY();
		
		/*
		 * X X X
		 * X O X
		 * X X X
		 */
		
		int mejorNextX = currX;
		int mejorNextY = currY;
		double mejorDistancia = 10000.0;
		
		for (int i = -1; i <= 1; i++){
			for (int j = -1; j <= 1; j++){
				
				int a = Math.abs((currX + i) - x);
            	int b = Math.abs((currY + j) - y);
            	
            	//la distancia entre 2 puntos (entre el minero1 y el mineral)
            	double distancia = Math.sqrt(a*a + b*b);
            	
            	if (distancia < mejorDistancia){
            		mejorNextX = currX + i;
            		mejorNextY = currY + j;
            		mejorDistancia = distancia;
            	}
			}
		}
		
		minero.setX(mejorNextX);
		minero.setY(mejorNextY);
		minero.onAgentMove(mejorNextX, mejorNextY);
		
		System.out.println(
				"Minero " + minero.getLocalName() + 
				" en ruta a " + x + ", " + y + ": " + 
				mejorNextX + ", " + mejorNextY
		);
		
		// Cuando ha llegado, se pone a picar (Espero)
		if (done()){
			System.out.println(minero.getLocalName() + " ha llegado al destino");
			minero.addBehaviour(new ExtraerMineral(mapa, minero.getX(), minero.getY()));
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public boolean done() {
		
		Minero minero = (Minero)myAgent;
		
		return minero.getX() == x && minero.getY() == y;

	}

}
