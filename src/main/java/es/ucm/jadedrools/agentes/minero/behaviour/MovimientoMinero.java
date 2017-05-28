package es.ucm.jadedrools.agentes.minero.behaviour;

import es.ucm.jadedrools.BehMinero;
import es.ucm.jadedrools.agentes.minero.Minero;
import es.ucm.jadedrools.mapa.GestorMapa;
import jade.core.behaviours.Behaviour;

public class MovimientoMinero extends Behaviour {
	
	private int x;
	private int y;
	
	public MovimientoMinero(int x, int y) {
		
		this.x = x;
		this.y = y;
	
	}

	@Override
	public void action() {
		
		Minero minero = (Minero)myAgent;
		
		minero.setX_mov(this.x);
		minero.setY_mov(this.y);
		
		BehMinero behMin = new BehMinero(minero, GestorMapa.getInstancia().getMapa());
		
		while(GestorMapa.getInstancia().getMapa().getCasilla(minero.getX(),minero.getY()).getTipo().name() == "PROHIBIDA"){//mientras sea prohibida, vuelve a intentar moverte a otro lado
			minero.setX_mov(this.x + 1);
			minero.setY_mov(this.y + 1);
			
			BehMinero behMin2 = new BehMinero(minero, GestorMapa.getInstancia().getMapa());
		}
		
		minero.onAgentMove(minero.getX(), minero.getY());
		
		System.out.println(
				"Minero " + minero.getLocalName() + 
				" en ruta a " + x + ", " + y + ": " + 
				minero.getX() + ", " + minero.getY()
		);
		
		// Cuando ha llegado, se pone a picar (Espero)
		if (done()){
			System.out.println(minero.getLocalName() + " ha llegado al destino");
			minero.addBehaviour(new ExtraerMineral());
		}
		
		minero.doWait(500);

	}
	
	@Override
	public boolean done() {
		
		Minero minero = (Minero)myAgent;
		
		return minero.getX() == x && minero.getY() == y;

	}

}
