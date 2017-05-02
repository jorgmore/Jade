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

import java.util.ArrayList;
import java.util.Vector;

import es.ucm.jadedrools.mapa.Mapa;
import es.ucm.jadedrools.mapa.TipoCasilla;

public class Explorador extends Agent {
	// Put agent initializations here
	protected void setup() {
		Object[] arrayArgumentos = getArguments();
		
		//coords donde empieza el agente
		int coordInicial1 = (int)arrayArgumentos[0];
		int coordInicial2 = (int)arrayArgumentos[1];
		
		Mapa mapa = (Mapa)arrayArgumentos[2];
        
        addBehaviour(new ExploradorMensaje( mapa, coordInicial1, coordInicial2));
	}

	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Explorador "+getAID().getName()+
                        " Terminado.");
	}
	
	class ExploradorMensaje extends CyclicBehaviour
    {
        boolean fin = false;
        Mapa mapa;
        int coordInicial1;
        int coordInicial2;
        
        public ExploradorMensaje(Mapa mapa, int coordInicial1, int coordInicial2){
    		this.mapa = mapa;
    		this.coordInicial1 = coordInicial1;
    		this.coordInicial2 = coordInicial2;
    	}
        
        public void action()
        {
        	int x = 0;
            int y = 0;
            int minRandom=0;
            int maxRandom=100-1;
            boolean existe = false;
            
            ArrayList<Integer[]> listaCoords = new ArrayList<Integer[]>();
           
            while(listaCoords.size() < 100){//100*100=10000
                Integer[] coords = new Integer[2];
                String mineral = "";
              	if(listaCoords.size() > 0){//cuando es 0, empiezo donde se asigna el explorador
      	        	Double aleatorio1 = Math.random() * (maxRandom - minRandom);//eje x
      	            Double aleatorio2 = Math.random() * (maxRandom - minRandom);//eje y
      	            coords[0] = (int) Math.round(aleatorio1);
      	  		    coords[1] = (int) Math.round(aleatorio2);
              	}
              	else if(listaCoords.size() == 0){//cuando es 0, empiezo donde se asigna el explorador
      	            coords[0] = (int) this.coordInicial1;
      	  		    coords[1] = (int) this.coordInicial2;
              	}
    		    existe = false;
    		    //System.out.print(coords[0]+" "); 
      		    //System.out.println(coords[1]);
        		  
        		   //Se chequea si existen las coordenadas en la lista de coordenadas
        		   for(int i=0; i<listaCoords.size(); i++){
            		 if (listaCoords.get(i)[0] == coords[0] && listaCoords.get(i)[1] == coords[1]) {
       			        existe = true;
       			     }
                   }
        		  
        		   if(existe == false){
        			  listaCoords.add(coords);
        			  mapa.setTieneMineral(false);
        			  BehExplorador behExp = new BehExplorador(coords[0],coords[1], mapa);
        			  
        			  if(mapa.getTieneMineral() == true){//Si hay mineral, enviar al minero msj
        				 System.out.println("HAY MINERAL!!");
        			    for(int i=0; i< mapa.getCasilla(mapa.getX(),mapa.getY()).getMinerales().size(); i++){
        				    mineral += mineral+mapa.getCasilla(mapa.getX(),mapa.getY()).getMinerales().elementAt(i).getTipo();
        				    System.out.println(getLocalName() +": Preparandose para enviar un mensaje a mineros");
        		            AID id = new AID();
        		            id.setLocalName("minero1");
        		            
        		            //AID id2 = new AID();
        		            //id2.setLocalName("minero2");
        		 
        		            // Creación del objeto ACLMessage
        		            ACLMessage mensaje = new ACLMessage(ACLMessage.REQUEST);
        		            //ACLMessage mensaje2 = new ACLMessage(ACLMessage.REQUEST);
        		 
        		            //Rellenar los campos necesarios del mensaje
        		            mensaje.setSender(getAID());
        		            mensaje.setLanguage("Español");
        		            mensaje.addReceiver(id);
        		            mensaje.setContent(coords[0]+","+coords[1]+";"+mineral);
        		            
        		            //mensaje2.setSender(getAID());
        		            //mensaje2.setLanguage("Español");
        		            //mensaje2.addReceiver(id2);
        		            //mensaje2.setContent(coords[0]+","+coords[1]+";"+mineral);
        		 
        		            //Envia el mensaje a los destinatarios
        		            send(mensaje);
        		           // send(mensaje2);

        		          //Espera la respuesta
        		            ACLMessage mensajeRecibido = blockingReceive();
        		            if (mensajeRecibido!= null)
        		            {
        		                System.out.println(getLocalName() + ": acaba de recibir el siguiente mensaje: ");
        		                System.out.println(mensajeRecibido.toString());
        		                fin = true;
        		            }
        			    }
        			  }
        		   }
              }
            
        }
        /*
        public boolean done()
        {
            return fin;
        }
        */
    }
}