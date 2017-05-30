package es.ucm.jadedrools.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class Ventana {
	
	private JFrame ventana;
	private JButton b1;
	private JPanel p;
	JPanel pnlButton = new JPanel();
	JButton btnAddFlight = new JButton("Stop/Go");
	
	public Ventana(){
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	    } 
	    catch (Exception e) {}
		
		ventana = new JFrame("Zealandia");
		ventana.setLayout(new BorderLayout());
		ventana.setVisible(true);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setMinimumSize(new Dimension(700, 700));
		//ventana.setLocation(0, 0);
		
		ventana.pack();
		
	}
	
	public void setMapaGui(MapaGui m){
		
		if (ventana != null){
			JScrollPane scrPane = new JScrollPane(m);
			scrPane.getViewport().setPreferredSize(ventana.getSize());
			ventana.add(scrPane, BorderLayout.CENTER); // similar to getContentPane().add(scrPane);
			ventana.pack();
			
			//ventana.add(m);
		}
	}
	
	public void setControllerGui(ControllerGUI c){
		if (ventana != null){
			ventana.add(c, BorderLayout.SOUTH);
			ventana.pack();
		}
	}
}