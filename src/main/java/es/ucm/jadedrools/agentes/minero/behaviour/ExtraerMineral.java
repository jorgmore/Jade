package es.ucm.jadedrools.agentes.minero.behaviour;

import es.ucm.jadedrools.agentes.minero.Minero;
import es.ucm.jadedrools.mapa.GestorMapa;
import es.ucm.jadedrools.mapa.Mapa;
import es.ucm.jadedrools.objetos.Mineral;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class ExtraerMineral extends Behaviour {
	
	@Override
	public void action() {
		
		Minero minero = (Minero)myAgent;
		
		try{
			int cantidad = GestorMapa.getInstancia().getMapa()
					.getCasilla(minero.getX(), minero.getY()).getMinerales().get(0).getCantidad();
			System.out.println("Agente" + minero.getLocalName() + "Picando! Queda " + cantidad);
		}
		catch(Exception e){ System.out.println("Ha petado");}
		
		GestorMapa.getInstancia().getMapa()
			.getCasilla(minero.getX(), minero.getY()).minar(1, 0);
		
		ACLMessage mensajeATransp = new ACLMessage(ACLMessage.REQUEST);
		// Rellenar los campos necesarios del mensaje
		mensajeATransp.setSender(minero.getAID());
		mensajeATransp.setContent(minero.getX() + "," + minero.getY());//pos de la mina
		mensajeATransp.setConversationId("mina-carga");
		
		// Sacamos los agentes transportistas
		DFAgentDescription template2 = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("transportista");
		template2.addServices(sd);
		
		try {
			DFAgentDescription[] result2 = DFService.search(myAgent, template2);
			for (int i = 0; i < result2.length; i++)
				mensajeATransp.addReceiver(result2[i].getName());
		}
		catch(FIPAException fe){
			fe.printStackTrace();
		}
		
		// Mandamos el mensaje
		minero.send(mensajeATransp);
		
		minero.doWait(500);
		
		if (done()){
			minero.desocupar();
		}
	}

	@Override
	public boolean done() {
		
		Minero minero = (Minero)myAgent;
		return GestorMapa.getInstancia().getMapa()
				.getCasilla(minero.getX(), minero.getY()).getMinerales().isEmpty();
		
	}
}
