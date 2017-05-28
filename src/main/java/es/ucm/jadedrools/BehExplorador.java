package es.ucm.jadedrools;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import es.ucm.jadedrools.mapa.Mapa;
import es.ucm.jadedrools.agentes.explorador.Explorador;

public class BehExplorador {
	public KieServices ks;
	public KieContainer kContainer;
	public KieSession kSession;
	int x;
	int y;
	
	/*Constructor*/
	public BehExplorador(Explorador exp, Mapa mapa){
        ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
	    kSession = kContainer.newKieSession("ksession-rules");

        kSession.insert(mapa);
        kSession.insert(exp);
        action();
	}
	
    public void action() {
    	kSession.fireAllRules();
	}
}