package es.ucm.jadedrools;

/**
 * Clase para informar a la GUI de las acciones de los agentes, para mostrarlas
 * de forma acorde.
 * 
 * @author rodry
 *
 */
public interface AgentObserver {
	
	public void onAgentMoved(String nombreAgente, int x, int y);

}