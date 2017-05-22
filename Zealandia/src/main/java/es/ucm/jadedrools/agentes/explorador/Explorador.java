package es.ucm.jadedrools.agentes.explorador;

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

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import es.ucm.jadedrools.BehExplorador;
import es.ucm.jadedrools.agentes.ObservableAgent;
import es.ucm.jadedrools.agentes.explorador.behaviour.AvisarMineros;
import es.ucm.jadedrools.agentes.explorador.behaviour.ComprobarMineral;
import es.ucm.jadedrools.agentes.explorador.behaviour.MovimientoBehaviour;
import es.ucm.jadedrools.agentes.explorador.behaviour.VolverANave;
import es.ucm.jadedrools.gui.MapaGui;
import es.ucm.jadedrools.mapa.Mapa;
import es.ucm.jadedrools.mapa.TipoCasilla;

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

		Mapa mapa = (Mapa) arrayArgumentos[2];
		mapa_explorado = new boolean[mapa.getAncho()][mapa.getAlto()];

		// Registramos al observador
		MapaGui mGui = (MapaGui) arrayArgumentos[3];
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
		estados.registerFirstState(new MovimientoBehaviour(mapa), "movimiento");
		estados.registerLastState(new VolverANave(), "finalizar");
		estados.registerState(new ComprobarMineral(mapa), "comprobar");
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

	/*class ExploradorMensaje extends CyclicBehaviour {
		boolean fin = false;
		Mapa mapa;
		int coordInicial1;
		int coordInicial2;

		public ExploradorMensaje(Mapa mapa, int coordInicial1, int coordInicial2) {
			this.mapa = mapa;
			this.coordInicial1 = coordInicial1;
			this.coordInicial2 = coordInicial2;
		}

		public void action() {
			int x = 0;
			int y = 0;
			int minRandom = 0;
			int maxRandom = 100 - 1;
			boolean existe = false;

			ArrayList<Integer[]> listaCoords = new ArrayList<Integer[]>();

			while (listaCoords.size() < 100) {// 100*100=10000
				Integer[] coords = new Integer[2];
				String mineral = "";
				if (listaCoords.size() > 0) {// cuando es 0, empiezo donde se asigna el explorador
					Double aleatorio1 = Math.random() * (maxRandom - minRandom);// eje x
					Double aleatorio2 = Math.random() * (maxRandom - minRandom);// eje y
					coords[0] = (int) Math.round(aleatorio1);
					coords[1] = (int) Math.round(aleatorio2);
				} else if (listaCoords.size() == 0) {// cuando es 0, empiezo donde se asigna el explorador
					coords[0] = (int) this.coordInicial1;
					coords[1] = (int) this.coordInicial2;
				}
				existe = false;
				// System.out.print(coords[0]+" ");
				// System.out.println(coords[1]);

				// Se chequea si existen las coordenadas en la lista de coordenadas
				for (int i = 0; i < listaCoords.size(); i++) {
					if (listaCoords.get(i)[0] == coords[0] && listaCoords.get(i)[1] == coords[1]) {
						existe = true;
					}
				}

				if (existe == false) {
					
					onAgentMove(coords[0], coords[1]);
					
					listaCoords.add(coords);
					mapa.setTieneMineral(false);
					BehExplorador behExp = new BehExplorador(coords[0], coords[1], mapa);

					if (mapa.getTieneMineral() == true) {// Si hay mineral, enviar al minero msj
						System.out.println("HAY MINERAL!!");
						for (int i = 0; i < mapa.getCasilla(mapa.getX(), mapa.getY()).getMinerales().size(); i++) {
							mineral += mineral
									+ mapa.getCasilla(mapa.getX(), mapa.getY()).getMinerales().elementAt(i).getTipo();
							System.out.println(getLocalName() + ": Preparandose para enviar un mensaje a mineros");
							AID id = new AID();
							id.setLocalName("minero1");

							// AID id2 = new AID();
							// id2.setLocalName("minero2");

							// Creaci�n del objeto ACLMessage
							ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
							// ACLMessage mensaje2 = new ACLMessage(ACLMessage.REQUEST);

							// Rellenar los campos necesarios del mensaje
							mensaje.setSender(getAID());
							mensaje.setLanguage("Espa�ol");
							mensaje.addReceiver(id);
							mensaje.setContent(coords[0] + "," + coords[1] + ";" + mineral);

							// mensaje2.setSender(getAID());
							// mensaje2.setLanguage("Espa�ol");
							// mensaje2.addReceiver(id2);
							// mensaje2.setContent(coords[0]+","+coords[1]+";"+mineral);

							// Envia el mensaje a los destinatarios
							send(mensaje);
							// send(mensaje2);

							// Espera la respuesta
							ACLMessage mensajeRecibido = blockingReceive();
							if (mensajeRecibido != null) {
								System.out.println(getLocalName() + ": acaba de recibir el siguiente mensaje: ");
								System.out.println(mensajeRecibido.toString());
								fin = true;
							}
						}
					}
				}
			}

		}
	}*/
}