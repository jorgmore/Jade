package es.ucm.jadedrools.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import es.ucm.jadedrools.mapa.Mapa;

public class Ventana {
	
	private JFrame ventana;
	
	public Ventana(){
		
		ventana = new JFrame("Zealandia");
		ventana.setLayout(new GridLayout());
		ventana.setVisible(true);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setMinimumSize(new Dimension(700, 700));
		ventana.setLocation(0, 0);
		
		ventana.pack();
		
	}
	
	public void setMapaGui(MapaGui m){
		
		if (ventana != null){
			JScrollPane scrPane = new JScrollPane(m);
			scrPane.getViewport().setPreferredSize(ventana.getSize());
			ventana.add(scrPane); // similar to getContentPane().add(scrPane);
			ventana.pack();
			
			//ventana.add(m);
		}
	}
}