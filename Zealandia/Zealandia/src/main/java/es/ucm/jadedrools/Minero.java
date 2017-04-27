package es.ucm.jadedrools;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;

public class Minero extends Agent {
	private Hashtable catalogo;

	// Put agent initializations here
	protected void setup() {
		// Create the catalogue
                System.out.println("Bienvenido! Agente-Minero "+getAID().getName()+" es Leido.");
		catalogo = new Hashtable();

		// Create and show the GUI 
		//miGui = new GuiVentaLibros(this);
		//miGui.showGui();

		// Register the book-selling service in the yellow pages
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Venta-libros");
		sd.setName("Cartera de inversión JADE");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Add the behaviour serving queries from buyer agents
		//addBehaviour(new DemandaOfertaServidor());

		// Add the behaviour serving purchase orders from buyer agents
		addBehaviour(new OrdenesCompraServidor());
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
			DFService.deregister(this);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		// Close the GUI
		//miGui.dispose();
		// Printout a dismissal message
		System.out.println("Agente Minero "+getAID().getName()+" terminado.");
	}

	/**
     This is invoked by the GUI when the user adds a new book for sale
	 */
	public void ActualizaCatalogo(final String titulo, final int precio) {
		addBehaviour(new OneShotBehaviour() {
			public void action() {
				catalogo.put(titulo, new Integer(precio));
				System.out.println(titulo+" insertado en catálogo. Precio = "+precio);
			}
		} );
	}
	
	private class OrdenesCompraServidor extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				String titulo = msg.getContent();
				ACLMessage respuesta = msg.createReply();
				respuesta.setPerformative(ACLMessage.INFORM);
				System.out.println(titulo+" vendido a agente "+msg.getSender().getName());
				myAgent.send(respuesta);
			}
			else {
				block();
			}
		}
	}  // End of inner class OfferRequestsServer
}