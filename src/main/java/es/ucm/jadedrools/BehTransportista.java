package es.ucm.jadedrools;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import es.ucm.jadedrools.mapa.Mapa;
import es.ucm.jadedrools.agentes.transportista.Transportista;

public class BehTransportista {
	public KieServices ks;
	public KieContainer kContainer;
	public KieSession kSession;
	
	/*Constructor*/
	
	public BehTransportista(Transportista trans, Mapa mapa){
        ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
	    kSession = kContainer.newKieSession("ksession-rules");

        kSession.insert(mapa);
        kSession.insert(trans);
        action();
	}
	
    public void action() {
    	kSession.fireAllRules();
	}
}