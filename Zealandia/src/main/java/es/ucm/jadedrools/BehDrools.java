package es.ucm.jadedrools;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class BehDrools {
	public KieServices ks;
	public KieContainer kContainer;
	public KieSession kSession;
	
	/*Constructor*/
	public BehDrools(){
		// load up the knowledge base
        ks = KieServices.Factory.get();
	    kContainer = ks.getKieClasspathContainer();
        kSession = kContainer.newKieSession("ksession-rules");

        // go !
        Message message = new Message();
        message.setMessage("Hello Antonio");
        message.setStatus(Message.HELLO);
        kSession.insert(message);
        kSession.fireAllRules();
	}

    public static final void main(String[] args) {
        try {
        	BehDrools behD = new BehDrools();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static class Message {

        public static final int HELLO = 0;
        public static final int GOODBYE = 1;

        private String message;

        private int status;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

}