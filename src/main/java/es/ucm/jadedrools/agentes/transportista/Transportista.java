package es.ucm.jadedrools.agentes.transportista;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import es.ucm.jadedrools.agentes.ObservableAgent;
import es.ucm.jadedrools.agentes.minero.Minero;
import es.ucm.jadedrools.agentes.transportista.behaviour.EvaluarDistancia;
import es.ucm.jadedrools.agentes.transportista.behaviour.MovimientoTransportista;
import es.ucm.jadedrools.gui.MapaGui;
import es.ucm.jadedrools.mapa.Mapa;

public class Transportista extends ObservableAgent {
	private int x;
	private int y;
	
	private int x_objetivo;
	private int y_objetivo;
	
	private int x_mov;
	private int y_mov;
	
	private EstadoTransportista estado;
	
	private MessageTemplate mt; // Template para los mensajes que se reciben
	
	protected void setup(){
		
		Object[] arrayArgumentos = getArguments();//argumentos de la creacion del agente
		
		// coords donde empieza el agente
		x = x_objetivo = (int) arrayArgumentos[0];
		y = y_objetivo = (int) arrayArgumentos[1];
		
		MapaGui mGui = (MapaGui)arrayArgumentos[2];
		
		observers = new Vector<>();
		registerObserver(mGui);
		
		// Registrar el transportista en las paginas amarillas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("transportista");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this , dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		desocupar();
		
        addBehaviour(new RecibirMensajeDeMinero(this));
        
    }
	
	@Override
	protected void takeDown() {
		// Desregistrar de las paginas amarillas
		try {
			DFService.deregister(this);
		} 
		catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	public int getX(){ return x; }
	public int getY(){ return y; }
	
	public void setX(int x){ this.x = x; }
	public void setY(int y){ this.y = y; }
	
	public int getX_mov(){ return x_mov; }
	public int getY_mov(){ return y_mov; }
	
	public void setX_mov(int x){ this.x_mov = x; }
	public void setY_mov(int y){ this.y_mov = y; }
	
	public void setObjetivo(int x, int y){
		x_objetivo = x;
		y_objetivo = y;
	}
	
	public double getDistanciaObjetivo(){
		int a = Math.abs(x_objetivo - x);
    	int b = Math.abs(y_objetivo - y);
		
		return Math.sqrt(a*a + b*b);
	}
	
	public void desocupar(){ 
		mt = MessageTemplate.MatchConversationId("mina-carga");
		estado = EstadoTransportista.ESPERANDO;
	}
	public void ocupar(){ estado = EstadoTransportista.OCUPADO; }
	
	private class RecibirMensajeDeMinero extends CyclicBehaviour {
		private int agentes_restantes = 0;
		private boolean mas_cercano = true;
		private Vector<String> empates = new Vector<>();
		private int random;
		public Transportista transportista;
		
		public RecibirMensajeDeMinero(Transportista t) {
			this.transportista = t;
		}

		@Override
		public void action() {
			switch (estado) {
			case ESPERANDO:
				// Primer estado. Esperamos a recibir un mensaje de algun minero. Y determino primero si tiene carga para llevar esa mina
				ACLMessage mensaje_minero = receive(mt);
				
				if (mensaje_minero != null){
					
					// Sacamos las coordenadas x,y del mensaje
					String[] coords = mensaje_minero.getContent().split(",");
					int coord1 = Integer.parseInt(coords[0]);
	            	int coord2 = Integer.parseInt(coords[1]);
	            	setObjetivo(coord1, coord2);
	            	
	            	System.out.println("Transportista " + getLocalName() + " - Mensaje recibido: hay una carga en " + coord1 + ", " + coord2);
	            	
	            	// Sacamos el numero de transportistas registrados para saber
	            	// Cuantos mensajes como maximo recibiremos
	            	DFAgentDescription template = new DFAgentDescription();
	        		ServiceDescription sd = new ServiceDescription();
	        		sd.setType("transportista");
	        		template.addServices(sd);
	        		try {
	        			DFAgentDescription[] result = DFService.search(myAgent, template);
	        			agentes_restantes = result.length - 1;
	        		}
	        		catch(FIPAException fe){
	        			fe.printStackTrace();
	        		}
					
					myAgent.addBehaviour(new EvaluarDistancia(coord1, coord2));
					
					// Queremos recibir mensajes de ocupado y de distancias
					mt = MessageTemplate.or(
							MessageTemplate.MatchConversationId("evaluar-distanciaTrans"),
							MessageTemplate.MatchPerformative(ACLMessage.REFUSE));
					estado = EstadoTransportista.ESCUCHANDO;
					
				}
				else
					block();
				break;
				
			case ESCUCHANDO:
				// Segundo estado. Recibimos distancias de los otros transportistas.
				ACLMessage mensaje_transportista = receive(mt);
				
				if (mensaje_transportista != null){
					System.out.println(getLocalName() + " - He recibido un mensaje de " + mensaje_transportista.getSender().getLocalName());
					
					if (mensaje_transportista.getPerformative() != ACLMessage.REFUSE){
						
						double distancia = Double.parseDouble(mensaje_transportista.getContent());
						double mi_distancia = getDistanciaObjetivo();
						
						if (mi_distancia == distancia)
							empates.add(mensaje_transportista.getSender().getLocalName());
						else
							mas_cercano &= (mi_distancia < distancia);						
						
					}
					agentes_restantes--;
					
					if (agentes_restantes <= 0){
						System.out.println(getLocalName() + " - Soy el mas cercano? " + mas_cercano);

						if (mas_cercano){
							
							// Si ha habido empates, desempatamos y luego vemos
							if (!empates.isEmpty()){
								
								random = ThreadLocalRandom.current().nextInt(0, 1000 + 1);
								agentes_restantes = empates.size();
								
								ACLMessage suertes = new ACLMessage(ACLMessage.PROPOSE);
								suertes.setSender(getAID());
								suertes.setContent(String.valueOf(random));
								suertes.setConversationId("transportista-desempate");
								while (!empates.isEmpty())
									suertes.addReceiver(new AID(empates.remove(0), AID.ISLOCALNAME));
								send(suertes);
								
								estado = EstadoTransportista.DESEMPATANDO;
								mt = MessageTemplate.and(
										MessageTemplate.MatchConversationId("transportista-desempate"),
										MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
								
							}
							// Si no, vamos directos
							else {
								estado = EstadoTransportista.MOVIENDO;
								addBehaviour(new MovimientoTransportista(x_objetivo, y_objetivo));
							}
						}
						else
							desocupar();
						
						empates.clear();
						mas_cercano = true;
						
					}
				}
				else
					block();
				break;
				
			case DESEMPATANDO:
				// Estado de desempate, esperamos a recibir una respuesta del resto de transportistas
				ACLMessage mensaje_desempate = receive(mt);
				
				if (mensaje_desempate != null){
					
					int su_random = Integer.parseInt(mensaje_desempate.getContent());
					System.out.println("El random de " + getLocalName() + ": " + random + 
							", el de " + mensaje_desempate.getSender().getLocalName() + ": " + su_random);
					
					mas_cercano &= (su_random < random);
					agentes_restantes--;
					
					if (agentes_restantes <= 0){
						
						if (mas_cercano){
							
							estado = EstadoTransportista.MOVIENDO;
							addBehaviour(new MovimientoTransportista(x_objetivo, y_objetivo));
							
						}
						else
							desocupar();
						
						mas_cercano = true;
					}
					
				}
				else
					block();
				break;
				
			case MOVIENDO:	
				if(transportista.x_objetivo == transportista.x && transportista.y_objetivo == transportista.y){
					desocupar();
				}
			case OCUPADO:
				// Estamos ocupados descargando.
				ACLMessage mensaje = receive();
				
				if (mensaje != null){
					
					ACLMessage negativa = new ACLMessage(ACLMessage.REFUSE);
					negativa.setSender(getAID());
					negativa.addReceiver(mensaje.getSender());
					send(negativa);
					
				}
				else 
					block();
				break;
			default:
				break;
			}
		}
	}
}