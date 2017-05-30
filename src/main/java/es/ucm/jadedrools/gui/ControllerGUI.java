package es.ucm.jadedrools.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class ControllerGUI extends JPanel {
	
	List<AgentController> agentes;
	
	public ControllerGUI(List<AgentController> agentes){
		
		super(new GridLayout(1, 2));
		
		this.agentes = agentes;
		
		JButton parar = new JButton("Pausar");
		parar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (AgentController ac : agentes){
					try {
						ac.suspend();
					} catch (StaleProxyException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		JButton continuar = new JButton("Continuar");
		continuar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for (AgentController ac : agentes){
					try {
						ac.activate();
					} catch (StaleProxyException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		add(parar);
		add(continuar);
		
	}
}
