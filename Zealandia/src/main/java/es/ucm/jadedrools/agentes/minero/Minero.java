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

import es.ucm.jadedrools.agentes.ObservableAgent;
import es.ucm.jadedrools.agentes.minero.behaviour.EvaluarDistancia;
import es.ucm.jadedrools.gui.MapaGui;

public class Minero extends ObservableAgent {
	
	private int x;
	private int y;
	private boolean ocupado;
	
	protected void setup(){
		
		Object[] arrayArgumentos = getArguments();//argumentos de la creacion del agente
		
		// coords donde empieza el agente
		x = (int) arrayArgumentos[0];
		y = (int) arrayArgumentos[1];
		
		MapaGui mGui = (MapaGui)arrayArgumentos[3];
		
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

        addBehaviour(new RecibirMensaje());
        
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
	
	public void setOcupado(boolean ocupado){ this.ocupado = ocupado; }
	
	private class RecibirMensaje extends CyclicBehaviour {

		@Override
		public void action() {
			
			if (!ocupado){
			
				ACLMessage mensaje = receive();
				
				if (mensaje != null){
					
					String[] coords = mensaje.getContent().split(",");
					int coord1 = Integer.parseInt(coords[0]); //coordenada X del mineral (donde esta ahora mismo el explorador)
	            	int coord2 = Integer.parseInt(coords[1]); //coordenada Y del mineral (donde esta ahora mismo el explorador)
	            	
	            	System.out.println("Minero " + getLocalName() + " - Mensaje recibido: hay un mineral en " + coord1 + ", " + coord2);
					
					myAgent.addBehaviour(new EvaluarDistancia(coord1, coord2));
					setOcupado(true);
				}
				else
					block();
			}
		}
	}
	
	class MineroReceptorMensaje extends CyclicBehaviour
    {
        private boolean fin = false;
        //public int x;//posicion del minero en X
        //public int y;//posicion del minero en Y
        
        public void action()
        {
            System.out.println(" Preparandose para recibir");
            int coord1;
            int coord2;
            String mineral;
            
            Object[] arrayArgumentos = getArguments();//argumentos de la creacion del agente
            //coords donde empieza el agente
    		int coordInicial1 = (int)arrayArgumentos[0];
    		int coordInicial2 = (int)arrayArgumentos[1];
    		Double distancia = 0.0;
 
            //Obtiene el primer mensaje de la cola de mensajes
            ACLMessage mensajeExplorador = blockingReceive();
 
            if (mensajeExplorador!= null)
            {
            	String[] coords = mensajeExplorador.getContent().toString().split(",");
            	String[] tipomineral = coords[1].toString().split(";");
            	coord1 = Integer.parseInt(coords[0]);//coordenada X del mineral (donde est� ahora mismo el explorador)
            	coord2 = Integer.parseInt(tipomineral[0]);//coordenada Y del mineral (donde est� ahora mismo el explorador)
            	mineral = tipomineral[1];//Tipo del mineral de dichas coordenadas
            	
            	int x = Math.abs(coord1 - coordInicial1);
            	int y = Math.abs(coord2 - coordInicial2);
            	
            	distancia = Math.sqrt(x*x + y*y);//la distancia entre 2 puntos (entre el minero1 y el agente explorador)
            	
	            System.out.println(getLocalName() + ": acaba de recibir el siguiente mensaje: ");
	            System.out.println(mensajeExplorador.toString());
	            
	           // Envia constestaci�n
                System.out.println(getLocalName() +": Enviando contestacion");
                ACLMessage respuesta = mensajeExplorador.createReply();
                respuesta.setPerformative( ACLMessage.INFORM );
                //respuesta.setContent("Mi posicion: "+coordInicial1+","+coordInicial2);
                //respuesta.setContent(getLocalName()+";"+distancia+"||"+coord1+","+coord2);
                
                onAgentMove(x, y);
                
                respuesta.setContent("Mineral de "+mineral+" en la posicion "+coord1+","+coord2+" explotado");
                send(respuesta);
                
	            fin = true;
            }
        }
        /*
        public boolean done()
        {
            return fin;
        }*/
    }
}