package es.ucm.jadedrools.agentes.minero.behaviour;

import es.ucm.jadedrools.agentes.minero.Minero;
import es.ucm.jadedrools.mapa.Mapa;
import es.ucm.jadedrools.objetos.Mineral;
import jade.core.behaviours.Behaviour;

public class ExtraerMineral extends Behaviour {
	
	private Mapa mapa;
	
	public ExtraerMineral(Mapa mapa, int x, int y){
		super();
		
		Minero minero = (Minero)myAgent;
		this.mapa = mapa;
		
	}

	@Override
	public void action() {
		
		Minero minero = (Minero)myAgent;
		
		try{
			int cantidad = mapa.getCasilla(minero.getX(), minero.getY()).getMinerales().get(0).getCantidad();
			System.out.println("Agente" + minero.getLocalName() + "Picando! Queda " + cantidad);
		}
		catch(Exception e){ System.out.println("Ha petado");}
		
		
		mapa.getCasilla(minero.getX(), minero.getY()).minar(1, 0);
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (done()){
			minero.desocupar();
		}
	}

	@Override
	public boolean done() {
		
		Minero minero = (Minero)myAgent;
		return mapa.getCasilla(minero.getX(), minero.getY()).getMinerales().isEmpty();
		
	}
}
