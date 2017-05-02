package es.ucm.jadedrools;

import java.util.Vector;

import jade.core.Agent;

/**
 * Clase de la que deben extender los agentes para poder avisar a la GUI de sus 
 * acciones.
 * 
 * @author rodry
 *
 */
public class ObservableAgent extends Agent {
	
	private Vector<AgentObserver> observers;
	
	public void registerObserver( AgentObserver observer ){
		
		observers.add(observer);
		
	}
	
	public void removeObserver( AgentObserver observer ){
		
		int i = observers.indexOf(observer);
		observers.remove(i);
		
	}
	
	protected void onAgentMove(int x, int y){
		
		for (AgentObserver ob: observers){
			
			ob.onAgentMoved(getAID().getName(), x, y);
			
		}
	}
}