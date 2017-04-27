package es.ucm.jadedrools;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.ProfileException;
public class StartJadeGUI {
	public static void main(String args[]) {
		Properties props = new ExtendedProperties();
		props.setProperty(Profile.GUI, "true");// Para arrancar con el GUI de la plataforma
		ProfileImpl p = new ProfileImpl(props);// Profile con la propiedad de tener GUI
		// Arranca un sistema JADE
		// Indica que cuando se cierre el último contenedor, se cierre la JVM:
		Runtime.instance().setCloseVM(true);
		// Crea un contenedor principal para esta instancia del entorno JADE:
		Runtime.instance().createMainContainer(p);
	}
}