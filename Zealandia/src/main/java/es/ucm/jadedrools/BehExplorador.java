package es.ucm.jadedrools;
import org.kie.api.KieServices;
import java.util.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import es.ucm.jadedrools.mapa.Casilla;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.lang.Object;
import es.ucm.jadedrools.mapa.Mapa;

public class BehExplorador {
	public KieServices ks;
	public KieContainer kContainer;
	public KieSession kSession;
	public int x;
	public int y;
	
	/*Constructor*/
	public BehExplorador(int x, int y, Mapa mapa){
		// load up the knowledge base
		this.x = x;
		this.y = y;
        ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
	    kSession = kContainer.newKieSession("ksession-rules");

	   // go !
	    mapa.setX(x);
	    mapa.setY(y);
        kSession.insert(mapa);
        action();
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
    
    public void action() {
    	kSession.fireAllRules();
	}
}