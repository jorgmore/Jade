package es.ucm.jadedrools.gui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Ventana {
	
	private JFrame ventana;
	
	public Ventana(){
		
		ventana = new JFrame("Zealandia");
		ventana.setVisible(true);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setMinimumSize(new Dimension(700, 700));
		ventana.setMaximumSize(new Dimension(700, 700));
		ventana.setResizable(false);
		ventana.setLocation(0, 0);
		
		ventana.pack();
		
	}

}
