package es.ucm.jadedrools.agentes.explorador.behaviour;

import es.ucm.jadedrools.agentes.explorador.Explorador;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class AvisarMineros extends OneShotBehaviour {

	@Override
	public void action() {
		
		Explorador explorador = (Explorador)myAgent;
		int posX = explorador.getX();
		int posY = explorador.getY();
		
		System.out.println("Explorador " + explorador.getLocalName() + 
				": Voy a avisar a los mineros de que vengan a " + posX + ", " + posY);
		
		// Creacion del objeto ACLMessage
		ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
		// Rellenar los campos necesarios del mensaje
		mensaje.setSender(explorador.getAID());
		mensaje.setContent(posX + "," + posY + ",COBRE");
		mensaje.setConversationId("mina-encontrada");
		
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
		
		// Mandamos el mensaje
		explorador.send(mensaje);

	}

}
