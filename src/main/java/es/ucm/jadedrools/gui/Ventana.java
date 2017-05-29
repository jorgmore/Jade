package es.ucm.jadedrools.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import es.ucm.jadedrools.mapa.Mapa;

public class Ventana {
	
	private JFrame ventana;
	private JButton b1;
	private JPanel p;
	JPanel pnlButton = new JPanel();
	JButton btnAddFlight = new JButton("Stop/Go");
	
	public Ventana(){
		
		ventana = new JFrame("Zealandia");
		ventana.setLayout(new GridLayout());
		ventana.setVisible(true);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setMinimumSize(new Dimension(700, 700));
		//ventana.setLocation(0, 0);
		
		
		p = new JPanel();
		//p.setLayout(new GridLayout());
		b1 = new JButton("Stop/Go");
		
		p.add(b1);
		//ventana.add(p);
		p.setLocation(1,0);
		b1.setBounds(0, 500, 100, 100);
		//ventana.add(p);
		
		b1.addActionListener(null
			
		);
		
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