package es.ucm.jadedrools;

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

public class Minero extends Agent {
	
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
            	coord1 = Integer.parseInt(coords[0]);//coordenada X del mineral (donde está ahora mismo el explorador)
            	coord2 = Integer.parseInt(tipomineral[0]);//coordenada Y del mineral (donde está ahora mismo el explorador)
            	mineral = tipomineral[1];//Tipo del mineral de dichas coordenadas
            	
            	int x = Math.abs(coord1 - coordInicial1);
            	int y = Math.abs(coord2 - coordInicial2);
            	
            	distancia = Math.sqrt(x*x + y*y);//la distancia entre 2 puntos (entre el minero1 y el agente explorador)
            	
	            System.out.println(getLocalName() + ": acaba de recibir el siguiente mensaje: ");
	            System.out.println(mensajeExplorador.toString());
	            
	           // Envia constestación
                System.out.println(getLocalName() +": Enviando contestacion");
                ACLMessage respuesta = mensajeExplorador.createReply();
                respuesta.setPerformative( ACLMessage.INFORM );
                //respuesta.setContent("Mi posicion: "+coordInicial1+","+coordInicial2);
                //respuesta.setContent(getLocalName()+";"+distancia+"||"+coord1+","+coord2);
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
    
	protected void setup()
    {
        addBehaviour(new MineroReceptorMensaje());
    }
}