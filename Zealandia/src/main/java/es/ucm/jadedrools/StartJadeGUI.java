package es.ucm.jadedrools;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import com.sun.accessibility.internal.resources.accessibility;

import es.ucm.jadedrools.gui.MapaGui;
import es.ucm.jadedrools.gui.TipoAgente;
import es.ucm.jadedrools.gui.Ventana;
import es.ucm.jadedrools.mapa.Mapa;
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

		Mapa m = new Mapa(100, 100);
		MapaGui mGui = new MapaGui(m);
		
		Ventana ventana = new Ventana();
		
		try {
			
			/* TODO: 
			 *  - Crear agentes con una posicion
			 *  - Agregar mGui como observer del agente:
			 *  	agente.addObserver(mGui);
			 *  	Si no se puede hacer asi, pasarselo como argumentos o algo asi
			 *  - Pasarselo a mGui para que se les pueda representar visualmente
			 *		mGui.agregarAgenteVisual("explorador1", TipoAgente.EXPLORADOR, x, y);
			 */	
			AgentController ac = cc.createNewAgent("explorador1", "es.ucm.jadedrools.Explorador", new Object[]{10, 5});
			mGui.agregarAgenteVisual("explorador1", TipoAgente.EXPLORADOR, 10, 5);
			ac.start();
			
			ac = cc.createNewAgent("minero1", "es.ucm.jadedrools.Minero", new Object[]{11, 5});
			mGui.agregarAgenteVisual("minero1", TipoAgente.MINERO, 11, 5);
			ac.start();
			
		} 
		catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		
		ventana.setMapaGui(mGui);
		
	}
}