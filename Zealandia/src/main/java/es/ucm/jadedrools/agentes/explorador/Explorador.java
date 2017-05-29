package es.ucm.jadedrools.agentes.explorador;

import java.util.Vector;

import es.ucm.jadedrools.agentes.ObservableAgent;
import es.ucm.jadedrools.agentes.explorador.behaviour.AvisarMineros;
import es.ucm.jadedrools.agentes.explorador.behaviour.ComprobarMineral;
import es.ucm.jadedrools.agentes.explorador.behaviour.MovimientoBehaviour;
import es.ucm.jadedrools.agentes.explorador.behaviour.VolverANave;
import es.ucm.jadedrools.gui.MapaGui;
import es.ucm.jadedrools.mapa.GestorMapa;
import es.ucm.jadedrools.mapa.Mapa;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Explorador extends ObservableAgent {
	
	private boolean[][] mapa_explorado;
	private int x;
	private int y;
	
	// Put agent initializations here
	protected void setup() {
		Object[] arrayArgumentos = getArguments();

		// coords donde empieza el agente
		x = (int) arrayArgumentos[0];
		y = (int) arrayArgumentos[1];
		
		Mapa mapa = GestorMapa.getInstancia().getMapa();

		mapa_explorado = new boolean[mapa.getAncho()][mapa.getAlto()];

		// Registramos al observador
		MapaGui mGui = (MapaGui) arrayArgumentos[2];
		observers = new Vector<>();
		registerObserver(mGui);
		
		// Registrar el explorador en las paginas amarillas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("explorador");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this , dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}

		//addBehaviour(new ExploradorMensaje(mapa, coordInicial1, coordInicial2));
		addBehaviour(new RecibirMensaje());
		
		// Maquina de estados para los behaviours del explorador
		FSMBehaviour estados = new FSMBehaviour();
		estados.registerFirstState(new MovimientoBehaviour(), "movimiento");
		estados.registerLastState(new VolverANave(), "finalizar");
		estados.registerState(new ComprobarMineral(), "comprobar");
		estados.registerState(new AvisarMineros(), "avisar");
		
		estados.registerTransition("movimiento", "finalizar", MovimientoBehaviour.MAPA_RECORRIDO);
		estados.registerTransition("movimiento", "comprobar", MovimientoBehaviour.MAPA_NO_RECORRIDO);
		estados.registerTransition("comprobar", "avisar", ComprobarMineral.SI_MINERAL);
		estados.registerTransition("comprobar", "movimiento", ComprobarMineral.NO_MINERAL);
		estados.registerDefaultTransition("avisar", "movimiento");
		
		addBehaviour(estados);
		
	}

	protected void takeDown() {
		// Desregistrar? de las paginas amarillas
		try {
			DFService.deregister(this);
		} 
		catch (FIPAException e) {
			e.printStackTrace();
		}
		
		// Printout a dismissal message
		System.out.println("Explorador " + getAID().getName() + " Terminado.");
		
	}
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	
	public void setX(int x){ this.x = x; }
	public void setY(int y){ this.y = y; }
	
	public void setExplorado(int x, int y){
		mapa_explorado[x][y] = true;
	}
	
	public boolean getExplorado(int x, int y){
		return mapa_explorado[x][y];
	}
	
	private class RecibirMensaje extends CyclicBehaviour {

		@Override
		public void action() {
			
			ACLMessage mensaje = receive();
			
			if (mensaje != null){
				
				System.out.println("Explorador " + getAID().getName() + " Mensaje Recibido!");
				
			}
			else
				block();
			
		}
	}
}