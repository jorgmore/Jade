package es.ucm.jadedrools.agentes;

import java.util.Vector;

import es.ucm.jadedrools.AgentObserver;
import jade.core.Agent;

/**
 * Clase de la que deben extender los agentes para poder avisar a la GUI de sus 
 * acciones.
 * 
 * @author rodry
 *
 */
public class ObservableAgent extends Agent {
	
	protected Vector<AgentObserver> observers;
	
	public void registerObserver( AgentObserver observer ){
		
		observers.add(observer);
		
	}
	
	public void removeObserver( AgentObserver observer ){
		
		int i = observers.indexOf(observer);
		observers.remove(i);
		
	}
	
	public void onAgentMove(int x, int y){
		
		for (AgentObserver ob: observers){
			
			ob.onAgentMoved(getName(), x, y);
			
		}
	}
}