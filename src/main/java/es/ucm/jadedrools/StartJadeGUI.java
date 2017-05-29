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
	public static final String TRANSPORTISTA_CLASS = "es.ucm.jadedrools.agentes.transportista.Transportista";
	
	public static void main(String args[]) {

		Properties props = new ExtendedProperties();
		props.setProperty(Profile.GUI, "true");// Para arrancar con el GUI de la plataforma
		ProfileImpl p = new ProfileImpl(props);// Profile con la propiedad de tener GUI
		int xNave = 1;
		int yNave = 0;
		
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
			for (int i = 0; i < 2; i++){
				ac = cc.createNewAgent("explorador_" + i, EXPLORADOR_CLASS, new Object[]{xNave, yNave, mGui});
				mGui.agregarAgenteVisual(ac.getName(), TipoAgente.EXPLORADOR, xNave, yNave);
				ac.start();
			}
			
			// MINEROS
			for (int i = 0; i < 3; i++){
				ac = cc.createNewAgent("minero_" + i, MINERO_CLASS, new Object[]{xNave, yNave, mGui});
				mGui.agregarAgenteVisual(ac.getName(), TipoAgente.MINERO, xNave, yNave);
				ac.start();
			}
			
			// TRANSPORTISTAS
			for (int i = 0; i < 2; i++){
				ac = cc.createNewAgent("transportista_" + i, TRANSPORTISTA_CLASS, new Object[]{xNave, yNave, mGui});
				mGui.agregarAgenteVisual(ac.getName(), TipoAgente.TRANSPORTISTA, xNave, yNave);
				ac.start();
			}
			//CAMBIAR EN ES.UCM.JADEDROOLS.MAPA EL MAPA.JAVA PARA LA POSICION DE LA NAVE, linea 53
		}
		catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		ventana.setMapaGui(mGui);
		
	}
}