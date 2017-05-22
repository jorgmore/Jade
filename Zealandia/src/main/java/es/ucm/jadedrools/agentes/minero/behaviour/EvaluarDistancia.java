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
		
		// Mandamos la distancia a todos los demas mineros
		ACLMessage informe_distancia = new ACLMessage(ACLMessage.INFORM);
		informe_distancia.setSender(minero.getAID());
		informe_distancia.setContent(Double.toString(minero.getDistanciaObjetivo()));
		informe_distancia.setConversationId("evaluar-distancia");
		
		// Sacamos los agentes mineros
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("minero");
		template.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			for (int i = 0; i < result.length; i++){
				if (!result[i].getName().getLocalName().equals(myAgent.getLocalName())){
					informe_distancia.addReceiver(result[i].getName());
				}
			}
		}
		catch(FIPAException fe){
			fe.printStackTrace();
		}
		
		minero.send(informe_distancia);
		
		//myAgent.addBehaviour(new MovimientoMinero(x, y));

	}

}
