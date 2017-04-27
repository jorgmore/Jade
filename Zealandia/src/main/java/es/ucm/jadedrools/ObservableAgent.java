package es.ucm.jadedrools;

import java.util.Vector;

import jade.core.Agent;

public class ObservableAgent extends Agent {
	
	private Vector<AgentObserver> observers;
	
	public void registerObserver( AgentObserver observer ){
		
		observers.add(observer);
		
	}
	
	public void removeObserver( AgentObserver observer ){
		
		int i = observers.indexOf(observer);
		observers.remove(i);
		
	}
	
	protected void onAgentMove(){
		
		for (AgentObserver ob: observers){
			
			ob.onAgentMoved();
			
		}
	}
}
