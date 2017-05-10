package es.ucm.jadedrools.agentes.minero.behaviour;

import es.ucm.jadedrools.agentes.minero.Minero;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class EvaluarDistancia extends OneShotBehaviour {
	
	private int x;
	private int y;
	
	public EvaluarDistancia(int x, int y){
		
		this.x = x;
		this.y = y;
		
	}

	@Override
	public void action() {
		
		// TODO Ponerse de acuerdo con el resto de mineros
		Minero minero = (Minero)myAgent;
		
		int a = Math.abs(x - minero.getX());
    	int b = Math.abs(y - minero.getY());
		
		double distancia = Math.sqrt(a*a + b*b);
		
		ACLMessage mensaje = new ACLMessage(ACLMessage.INFORM);
		// Rellenar los campos necesarios del mensaje
		mensaje.setSender(minero.getAID());
		mensaje.setContent(Double.toString(distancia));
		
		// Sacamos los agentes mineros
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("minero");
		template.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			for (int i = 0; i < result.length; i++)
				mensaje.addReceiver(result[i].getName());
		}
		catch(FIPAException fe){
			fe.printStackTrace();
		}
		
		//minero.send(mensaje);
		
		myAgent.addBehaviour(new MovimientoMinero(x, y));

	}

}
