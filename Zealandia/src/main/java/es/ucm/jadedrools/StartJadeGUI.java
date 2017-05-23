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
import es.ucm.jadedrools.mapa.GestorMapa;
import es.ucm.jadedrools.mapa.Mapa;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.ProfileException;

public class StartJadeGUI {
	
	public static final String EXPLORADOR_CLASS = "es.ucm.jadedrools.agentes.explorador.Explorador";
	public static final String MINERO_CLASS = "es.ucm.jadedrools.agentes.minero.Minero";
	
	public static void main(String args[]) {

		Properties props = new ExtendedProperties();
		props.setProperty(Profile.GUI, "true");// Para arrancar con el GUI de la plataforma
		ProfileImpl p = new ProfileImpl(props);// Profile con la propiedad de tener GUI
		
		// Arranca un sistema JADE
		// Indica que cuando se cierre el �ltimo contenedor, se cierre la JVM:
		Runtime.instance().setCloseVM(true);
		// Crea un contenedor principal para esta instancia del entorno JADE:
		ContainerController cc = Runtime.instance().createMainContainer(p);

		MapaGui mGui = new MapaGui(GestorMapa.getInstancia().getMapa());
		
		Ventana ventana = new Ventana();
		
		AgentController ac;
		
		try {
			
			/* TODO: 
			 *  - Crear agentes con una posicion
			 *  - Agregar mGui como observer del agente:
			 *  	agente.addObserver(mGui);
			 *  	Si no se puede hacer asi, pasarselo como argumentos o algo asi
			 *  - Pasarselo a mGui para que se les pueda representar visualmente
			 *		mGui.agregarAgenteVisual("explorador1", TipoAgente.EXPLORADOR, x, y);
			 */
			// EXPLORADORES
			
			ac = cc.createNewAgent("explorador1", EXPLORADOR_CLASS, new Object[]{10, 5, mGui});
			mGui.agregarAgenteVisual(ac.getName(), TipoAgente.EXPLORADOR, 10, 5);
			ac.start();
			
			ac = cc.createNewAgent("explorador2", EXPLORADOR_CLASS, new Object[]{10, 5, mGui});
			mGui.agregarAgenteVisual(ac.getName(), TipoAgente.EXPLORADOR, 10, 5);
			ac.start();
			
			// MINEROS
			
			ac = cc.createNewAgent("minero1", MINERO_CLASS, new Object[]{0, 0, mGui});
			mGui.agregarAgenteVisual(ac.getName(), TipoAgente.MINERO, 0, 0);
			ac.start();
			
			ac = cc.createNewAgent("minero2", MINERO_CLASS, new Object[]{0, 0, mGui});
			mGui.agregarAgenteVisual(ac.getName(), TipoAgente.MINERO, 0, 0);
			ac.start();
			
			ac = cc.createNewAgent("minero3", MINERO_CLASS, new Object[]{0, 0, mGui});
			mGui.agregarAgenteVisual(ac.getName(), TipoAgente.MINERO, 0, 0);
			ac.start();
			
		} 
		catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		ventana.setMapaGui(mGui);
		
	}
}