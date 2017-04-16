package es.ucm.jadedrools;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Explorador extends Agent {
	// The title of the book to buy
	private String NombreMineria;
	// The list of known seller agents
	private AID[] AgentesMineros;//SERIAN AgentesMineros

	// Put agent initializations here
	protected void setup() {
		// Printout a welcome message
		System.out.println("Hola Explorador! "+
                        getAID().getName()+" estoy activo");

			addBehaviour(new TickerBehaviour(this, 10000) {
				protected void onTick() {
					System.out.println("Esperando asignación del explorador");
					// Update the list of seller agents
					DFAgentDescription template = 
                                                new DFAgentDescription();
					ServiceDescription sd = 
                                                new ServiceDescription();
					sd.setType("venta-libros");
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template); 
						System.out.println("Se encuentran los siguientes agentes mineros:");
						AgentesMineros = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							AgentesMineros[i] = result[i].getName();
							System.out.println(AgentesMineros[i].getName());
						}
					}
					catch (FIPAException fe) {
						fe.printStackTrace();
					}

					// Perform the request
					myAgent.addBehaviour(new SolicitudMineria());
				}
			} );
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Explorador "+getAID().getName()+
                        " Terminado.");
	}

	/**
	   Inner class RequestPerformer.
	   This is the behaviour used by agents
	 */
	private class SolicitudMineria extends Behaviour {
		private AID explorador; // El agente que provee las minas a explotar
		private MessageTemplate mt; // The template to receive replies
		private int step = 0;

		public void action() {
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			for (int i = 0; i < AgentesMineros.length; ++i) {
				cfp.addReceiver(AgentesMineros[i]);
			} 
			cfp.setContent(NombreMineria);
			cfp.setConversationId("probando");
			cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
			myAgent.send(cfp);
			// Prepare the template to get proposals
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("minas"),
					MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
			step = 1;
		}

		public boolean done() {
			if (step == 2 && explorador == null) {
				System.out.println("Fallo");
			}
			return ((step == 2 && explorador == null) || step == 4);
		}
	}  // End of inner class RequestPerformer
}