package es.ucm.jadedrools.agentes.explorador.behaviour;

import java.util.concurrent.ThreadLocalRandom;

import es.ucm.jadedrools.agentes.explorador.Explorador;
import es.ucm.jadedrools.mapa.GestorMapa;
import es.ucm.jadedrools.mapa.Mapa;
import jade.core.behaviours.OneShotBehaviour;

public class MovimientoBehaviour extends OneShotBehaviour {
	
	public static final int MAPA_RECORRIDO = 0;
	public static final int MAPA_NO_RECORRIDO = 1;

	@Override
	public void action() {
		
		Explorador explorador = (Explorador)myAgent;
		int posX = explorador.getX();
		int posY = explorador.getY();
		
		int newX = -(explorador.getX() + 1);
		int newY = -(explorador.getY() + 1);
		
		while (!GestorMapa.getInstancia().getMapa().enRango(posX + newX, posY + newY) && 
				!(posX + newX == posX && posY + newY == posY)){
		
			newX = ThreadLocalRandom.current().nextInt(-1, 1 + 1);
			newY = ThreadLocalRandom.current().nextInt(-1, 1 + 1);
			
		}
		
		System.out.println("Explorador " +  explorador.getLocalName() + 
				": Me muevo a " + (posX + newX) + ", " + (posY + newY));
		
		explorador.setX(posX + newX);
		explorador.setY(posY + newY);
		
		explorador.setExplorado(posX + newX, posY + newY);
		explorador.onAgentMove(posX + newX, posY + newY);
		
		explorador.doWait(500);
	}
	
	@Override
	public int onEnd() {
		return MAPA_NO_RECORRIDO;
	}

}
