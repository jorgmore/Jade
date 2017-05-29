package es.ucm.jadedrools.agentes.transportista.behaviour;

import es.ucm.jadedrools.agentes.transportista.Transportista;
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
		
		Transportista transportista = (Transportista)myAgent;
		
		try{
			int cantidad = GestorMapa.getInstancia().getMapa()
					.getCasilla(transportista.getX(), transportista.getY()).getMinerales().get(0).getCantidad();
			System.out.println("Agente" + transportista.getLocalName() + "Cargando! Queda " + cantidad);
		}
		catch(Exception e){ System.out.println("Ha petado");}
		
		
		GestorMapa.getInstancia().getMapa()
			.getCasilla(transportista.getX(), transportista.getY()).minar(1, 0);
		
		transportista.doWait(500);
		
		if (done()){
			transportista.desocupar();
		}
	}

	@Override
	public boolean done() {
		
		Transportista transportista = (Transportista)myAgent;
		return GestorMapa.getInstancia().getMapa()
				.getCasilla(transportista.getX(), transportista.getY()).getMinerales().isEmpty();
		
	}
}
