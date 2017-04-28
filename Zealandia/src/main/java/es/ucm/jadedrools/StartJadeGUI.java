package es.ucm.jadedrools;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import com.sun.accessibility.internal.resources.accessibility;

import es.ucm.jadedrools.gui.Ventana;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.ProfileException;

public class StartJadeGUI {
	
	public static void main(String args[]) {

		Properties props = new ExtendedProperties();
		props.setProperty(Profile.GUI, "true");// Para arrancar con el GUI de la plataforma
		ProfileImpl p = new ProfileImpl(props);// Profile con la propiedad de tener GUI
		
		// Arranca un sistema JADE
		// Indica que cuando se cierre el �ltimo contenedor, se cierre la JVM:
		Runtime.instance().setCloseVM(true);
		// Crea un contenedor principal para esta instancia del entorno JADE:
		ContainerController cc = Runtime.instance().createMainContainer(p);
		
		try {
			
			AgentController ac = cc.createNewAgent("explorador1", "es.ucm.jadedrools.Explorador", null);
			ac.start();
			
			ac = cc.createNewAgent("minero", "es.ucm.jadedrools.Minero", null);
			ac.start();
			
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		Ventana ventana = new Ventana();
		
		
	}
}