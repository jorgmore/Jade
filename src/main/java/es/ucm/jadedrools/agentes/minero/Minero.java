package es.ucm.jadedrools.agentes.minero;

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
import es.ucm.jadedrools.agentes.minero.behaviour.EvaluarDistancia;
import es.ucm.jadedrools.agentes.minero.behaviour.ExtraerMineral;
import es.ucm.jadedrools.agentes.minero.behaviour.MovimientoMinero;
import es.ucm.jadedrools.gui.MapaGui;
import es.ucm.jadedrools.mapa.Mapa;

public class Minero extends ObservableAgent {
	private int x;
	private int y;
	
	private int x_objetivo;
	private int y_objetivo;
	
	private int x_mov;
	private int y_mov;
	
	private EstadoMinero estado;
	private MessageTemplate mt; // Template para los mensajes que se reciben
	
	private int base = 1;
	
	private int xNave;
	private int yNave;
	
	protected void setup(){
		Object[] arrayArgumentos = getArguments();//argumentos de la creacion del agente
		
		// coords donde empieza el agente
		x = x_objetivo = (int) arrayArgumentos[0];
		y = y_objetivo = (int) arrayArgumentos[1];
		
		MapaGui mGui = (MapaGui)arrayArgumentos[2];
		
		xNave = (int) arrayArgumentos[0];
		yNave = (int) arrayArgumentos[1];
		
		observers = new Vector<>();
		registerObserver(mGui);
		
		// Registrar el minero en las paginas amarillas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("minero");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try {
			DFService.register(this , dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		desocupar();

        addBehaviour(new RecibirMensaje(this));
        
    }
	
	@Override
	protected void takeDown() {
		// Desregistrar? de las paginas amarillas
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
	
	public int getX_objetivo(){ return x_objetivo; }
	public int getY_objetivo(){ return y_objetivo; }
	
	public void setX_mov(int x){ this.x_mov = x; }
	public void setY_mov(int y){ this.y_mov = y; }
	
	public void setObjetivo(int x, int y){
		x_objetivo = x;
		y_objetivo = y;
	}
	
	public void setBase(int base){
		this.base = base;
	}
	
	public int getBase(){
		return this.base;
	}
	
	public int getXNave(){
		return this.xNave;
	}
	
	public int getYNave(){
		return this.yNave;
	}
	
	public double getDistanciaObjetivo(){
		int a = Math.abs(x_objetivo - x);
    	int b = Math.abs(y_objetivo - y);
		
		return Math.sqrt(a*a + b*b);
	}
	
	public void desocupar(){ 
		mt = MessageTemplate.MatchConversationId("mina-encontrada");
		estado = EstadoMinero.ESPERANDO; 
	}
	public void ocupar(){ estado = EstadoMinero.OCUPADO; }
	
	private class RecibirMensaje extends CyclicBehaviour {
		
		private int agentes_restantes = 0;
		private boolean mas_cercano = true;
		private Vector<String> empates = new Vector<>();
		private int random;
		public Minero minero;
		
		public RecibirMensaje(Minero m) {
			this.minero = m;
		}

		@Override
		public void action() {
			switch (estado) {
			case ESPERANDO:
				// Primer estado. Esperamos a recibir un mensaje de algun explorador.
				ACLMessage mensaje_explorador = receive(mt);
				
				if (mensaje_explorador != null){
					
					// Sacamos las coordenadas x,y del mensaje
					String[] coords = mensaje_explorador.getContent().split(",");
					int coord1 = Integer.parseInt(coords[0]);
	            	int coord2 = Integer.parseInt(coords[1]);
	            	setObjetivo(coord1, coord2);
	            	
	            	System.out.println("Minero " + getLocalName() + " - Mensaje recibido: hay un mineral en " + coord1 + ", " + coord2);
	            	
	            	// Sacamos el numero de mineros registrados para saber
	            	// Cuantos mensajes como maximo recibiremos
	            	DFAgentDescription template = new DFAgentDescription();
	        		ServiceDescription sd = new ServiceDescription();
	        		sd.setType("minero");
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
							MessageTemplate.MatchConversationId("evaluar-distancia"),
							MessageTemplate.MatchPerformative(ACLMessage.REFUSE));
					estado = EstadoMinero.ESCUCHANDO;
					
				}
				else
					block();
				break;
				
			case ESCUCHANDO:
				// Segundo estado. Recibimos distancias de los otros mineros.
				ACLMessage mensaje_minero = receive(mt);
				
				if (mensaje_minero != null){
					System.out.println(getLocalName() + " - He recibido un mensaje de " + mensaje_minero.getSender().getLocalName());
					
					if (mensaje_minero.getPerformative() != ACLMessage.REFUSE){
						
						double distancia = Double.parseDouble(mensaje_minero.getContent());
						double mi_distancia = getDistanciaObjetivo();
						
						if (mi_distancia == distancia)
							empates.add(mensaje_minero.getSender().getLocalName());
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
								suertes.setConversationId("minero-desempate");
								while (!empates.isEmpty())
									suertes.addReceiver(new AID(empates.remove(0), AID.ISLOCALNAME));
								send(suertes);
								
								estado = EstadoMinero.DESEMPATANDO;
								mt = MessageTemplate.and(
										MessageTemplate.MatchConversationId("minero-desempate"),
										MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
								
							}
							// Si no, vamos directos
							else {
								estado = EstadoMinero.MOVIENDO;
								addBehaviour(new MovimientoMinero(x_objetivo, y_objetivo));
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
				// Estado de desempate, esperamos a recibir una respuesta del resto de mineros
				ACLMessage mensaje_desempate = receive(mt);
				
				if (mensaje_desempate != null){
					
					int su_random = Integer.parseInt(mensaje_desempate.getContent());
					System.out.println("El random de " + getLocalName() + ": " + random + 
							", el de " + mensaje_desempate.getSender().getLocalName() + ": " + su_random);
					
					mas_cercano &= (su_random < random);
					agentes_restantes--;
					
					if (agentes_restantes <= 0){
						
						if (mas_cercano){
							
							estado = EstadoMinero.MOVIENDO;
							addBehaviour(new MovimientoMinero(x_objetivo, y_objetivo));
							
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
			case OCUPADO:
				// Estamos ocupados minando.
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