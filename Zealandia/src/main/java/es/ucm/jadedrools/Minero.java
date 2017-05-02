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
	
	class MineroReceptorMensaje extends SimpleBehaviour
    {
        private boolean fin = false;
        public void action()
        {
            System.out.println(" Preparandose para recibir");
            int coord1;
            int coord2;
            String mineral;
 
            //Obtiene el primer mensaje de la cola de mensajes
            ACLMessage mensaje = blockingReceive();
 
            if (mensaje!= null)
            {
            	String[] coords = mensaje.getContent().toString().split(",");
            	String[] tipomineral = coords[1].toString().split(";");
            	coord1 = Integer.parseInt(coords[0]);//coordenada X del mineral (donde está ahora mismo el explorador)
            	coord2 = Integer.parseInt(tipomineral[0]);//coordenada Y del mineral (donde está ahora mismo el explorador)
            	mineral = tipomineral[1];//Tipo del mineral de dichas coordenadas
            	
	            System.out.println(getLocalName() + ": acaba de recibir el siguiente mensaje: ");
	            System.out.println(mensaje.toString());
	            
	         // Envia constestación
                System.out.println(getLocalName() +": Enviando contestacion");
                ACLMessage respuesta = mensaje.createReply();
                respuesta.setPerformative( ACLMessage.INFORM );
                respuesta.setContent( "Bien MSJ RESP" );
                send(respuesta);
                //System.out.println(getLocalName() +": Enviando Bien a receptor");
                //System.out.println(respuesta.toString());
	            
	            fin = true;
            }
            //block();
            
            /*
            AID id = new AID();
            id.setLocalName("explorador1");
 
            // RESPUESTA DEL MINERO PARA EXPLORADOR: Creación del objeto ACLMessage
            ACLMessage mensajeExp = new ACLMessage(ACLMessage.REQUEST);
 
           //Rellenar los campos necesarios del mensaje
            mensajeExp.setSender(getAID());
            mensajeExp.setLanguage("Español");
            mensajeExp.addReceiver(id);
            mensajeExp.setContent("MENSAJE PARA EXPLORADOR");
 
           //Envia el mensaje a explorador
            send(mensajeExp);
            */
        }
        public boolean done()
        {
            return fin;
        }
    }
    
	protected void setup()
    {
        addBehaviour(new MineroReceptorMensaje());
    }
}