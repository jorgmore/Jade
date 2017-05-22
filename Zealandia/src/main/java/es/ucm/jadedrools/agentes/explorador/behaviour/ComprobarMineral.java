package es.ucm.jadedrools.agentes.explorador.behaviour;

import es.ucm.jadedrools.agentes.explorador.Explorador;
import es.ucm.jadedrools.mapa.Mapa;
import jade.core.behaviours.OneShotBehaviour;

public class ComprobarMineral extends OneShotBehaviour {
	
	public static final int SI_MINERAL = 0;
	public static final int NO_MINERAL = 1;
	
	private boolean hayMineral = false;
	
	private Mapa mapa;
	
	public ComprobarMineral(Mapa mapa) {
		this.mapa = mapa;
	}

	@Override
	public void action() {
		
		System.out.println("Voy a ver si hay algun mineral por aqui");
		
		Explorador explorador = (Explorador)myAgent;
		int posX = explorador.getX();
		int posY = explorador.getY();
		
		hayMineral = !mapa.getCasilla(posX, posY).getMinerales().isEmpty();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public int onEnd() {
		if (hayMineral)
			return SI_MINERAL;
		else
			return NO_MINERAL;
	}

}
