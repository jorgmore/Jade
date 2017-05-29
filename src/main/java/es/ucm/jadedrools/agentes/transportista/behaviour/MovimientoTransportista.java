package es.ucm.jadedrools.agentes.transportista.behaviour;

import es.ucm.jadedrools.BehTransportista;
import es.ucm.jadedrools.agentes.transportista.Transportista;
import es.ucm.jadedrools.mapa.GestorMapa;
import jade.core.behaviours.Behaviour;

public class MovimientoTransportista extends Behaviour {
	
	private int x;
	private int y;
	
	public MovimientoTransportista(int x, int y) {
		
		this.x = x;
		this.y = y;
	
	}

	@Override
	public void action() {
		
		Transportista transportista = (Transportista)myAgent;
		
		transportista.setX_mov(this.x);
		transportista.setY_mov(this.y);
		
		BehTransportista behTrans = new BehTransportista(transportista, GestorMapa.getInstancia().getMapa());
		
		while(GestorMapa.getInstancia().getMapa().getCasilla(transportista.getX(),transportista.getY()).getTipo().name() == "PROHIBIDA"){//mientras sea prohibida, vuelve a intentar moverte a otra casilla
			transportista.setX_mov(this.x + 1);
			transportista.setY_mov(this.y + 1);
			
			BehTransportista behTrans2 = new BehTransportista(transportista, GestorMapa.getInstancia().getMapa());
		}
		
		if(transportista.getBase() == 2){
			transportista.onAgentMove(transportista.getXNave(), transportista.getYNave());
			transportista.setBase(1);
		}
		else {
			if(transportista.getX()== transportista.getX_objetivo() && transportista.getY()== transportista.getY_objetivo()){
				transportista.setBase(transportista.getBase() + 1);
			}
			transportista.onAgentMove(transportista.getX(), transportista.getY());
		}
		
		System.out.println(
				"Transportista " + transportista.getLocalName() + 
				" en ruta a " + x + ", " + y + ": " + 
				transportista.getX() + ", " + transportista.getY()
		);
		
		// Cuando ha llegado, se pone a descargar (Espero)
		if (done()){
			System.out.println(transportista.getLocalName() + " ha llegado al destino");
		}
		
		transportista.doWait(500);

	}
	
	@Override
	public boolean done() {
		
		Transportista transportista = (Transportista)myAgent;
		
		return transportista.getX() == x && transportista.getY() == y;

	}

}
