package es.ucm.jadedrools;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.lang.Object;

public class BehExplorador {
	public KieServices ks;
	public KieContainer kContainer;
	public KieSession kSession;
	
	/*Constructor*/
	public BehExplorador(){
		// load up the knowledge base
        ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
	    kSession = kContainer.newKieSession("ksession-rules");
        // go !
        Mapa message = new Mapa();
        //message.setMessage("Hello Antonio");
        //message.setStatus(Mapa.VACIO);
        message.setMatriz();
        kSession.insert(message);
        //kSession.fireAllRules();
        action();
	}

    public static final void main(String[] args) {
        try {
        	BehExplorador behExp = new BehExplorador();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    public void action() {
    	kSession.fireAllRules();
//		ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
//		for (int i = 0; i < AgentesMineros.length; ++i) {
//			cfp.addReceiver(AgentesMineros[i]);
//		} 
//		cfp.setContent(NombreMineria);
//		cfp.setConversationId("probando");
//		cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
//		myAgent.send(cfp);
//		// Prepare the template to get proposals
//		mt = MessageTemplate.and(MessageTemplate.MatchConversationId("minas"),
//				MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
//		step = 1;
	}

    public class Mapa {
        public static final int VACIO = 0;
        public static final int MINERAL = 1;
        public static final int BALIZA = 2;
        public static final int RECORRIDO = 3;
        
        public int [][] mapa = new int[2][2];//el tamaño de la matriz cual será siempre?
        
        public void setMatrizRecorrido() {
          this.mapa[0][0] = this.RECORRIDO;
        }
        
        public void setMatrizMineral() {
            this.mapa[0][1] = this.BALIZA;
        }
        
        /*Deberia ser una asignacion aleatoria*/
        public void setMatriz() {
          this.mapa[0][0] = 0;
          this.mapa[0][1] = 1;
          this.mapa[1][0] = 1;
          this.mapa[1][1] = 0;
        }
    }

}