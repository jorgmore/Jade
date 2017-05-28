package es.ucm.jadedrools;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import es.ucm.jadedrools.mapa.Mapa;
import es.ucm.jadedrools.agentes.minero.Minero;

public class BehMinero {
	public KieServices ks;
	public KieContainer kContainer;
	public KieSession kSession;
	
	/*Constructor*/
	
	public BehMinero(Minero min, Mapa mapa){
        ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
	    kSession = kContainer.newKieSession("ksession-rules");

        kSession.insert(mapa);
        kSession.insert(min);
        action();
	}
	
    public void action() {
    	kSession.fireAllRules();
	}
}