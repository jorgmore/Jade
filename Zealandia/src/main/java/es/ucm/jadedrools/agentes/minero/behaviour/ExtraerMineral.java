package es.ucm.jadedrools.agentes.minero.behaviour;

import es.ucm.jadedrools.agentes.minero.Minero;
import jade.core.behaviours.Behaviour;

public class ExtraerMineral extends Behaviour {
	
	int cantidadDebug = 10;

	@Override
	public void action() {
		
		Minero minero = (Minero)myAgent;
		
		System.out.println("Picando!");
		cantidadDebug--;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (done())
			minero.setOcupado(false);
	}

	@Override
	public boolean done() {
		
		return cantidadDebug == 0;
	}
}