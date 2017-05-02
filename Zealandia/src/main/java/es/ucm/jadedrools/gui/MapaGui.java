package es.ucm.jadedrools.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;

import es.ucm.jadedrools.AgentObserver;
import es.ucm.jadedrools.mapa.Mapa;
import es.ucm.jadedrools.objetos.Mineral;

public class MapaGui extends JPanel implements AgentObserver{
	
	private Mapa mapa;
	private Hashtable<String, AgenteDummy> agentes;
	
	private int CASILLA_SIZE = 24;

	public MapaGui(Mapa m) {
		
		super();		
		mapa = m;
		agentes = new Hashtable<>();
		
	}
	
	@Override
	public void paint(Graphics g) {
		
		for (int i = 0; i < mapa.getAncho(); i++){
			for (int j = 0; j < mapa.getAlto(); j++){
			
				switch (mapa.getCasilla(i, j).getTipo()) {
				case NORMAL:
					g.setColor(Color.green);
					break;
				case PANTANOSO:
					g.setColor(new Color(145, 104, 21));
					break;
				case PROHIBIDA:
					g.setColor(new Color(90, 60, 0));
					break;
				default:
					break;
				}
				
				g.fillRect(i*CASILLA_SIZE, j*CASILLA_SIZE, CASILLA_SIZE, CASILLA_SIZE);
				
				Vector<Mineral> minerales = mapa.getCasilla(i, j).getMinerales();
				if (!minerales.isEmpty()){				
					switch (minerales.firstElement().getTipo()) {
					case COBRE:
						g.setColor(new Color(247, 172, 128));
						break;
					case PLATA:
						g.setColor(new Color(234, 232, 225));
						break;
					case ORO:
						g.setColor(new Color(247, 200, 34));
						break;
					default:
						break;
					}
					
					g.fillOval(i*CASILLA_SIZE + 6, j*CASILLA_SIZE + 6, CASILLA_SIZE/2, CASILLA_SIZE/2);
				}
				
				ArrayList<AgenteDummy> arr = new ArrayList<>(agentes.values());
				
				for (AgenteDummy agente: arr){
					
					switch (agente.getTipo()) {
					case EXPLORADOR:
						g.setColor(new Color(69, 247, 232));
						break;
					case MINERO:
						g.setColor(new Color(189, 145, 255));
						break;
					case TRANSPORTISTA:
						g.setColor(new Color(255, 229, 0));
						break;
					default:
						break;
					}
					
					g.drawString("X", agente.getX()*CASILLA_SIZE + 6, agente.getY()*CASILLA_SIZE - 6);
					
				}
			}
		}
	}
	
	public void agregarAgenteVisual(String nombreAgente, TipoAgente tipo, int x, int y, Mapa m){
		
		agentes.put(nombreAgente, new AgenteDummy(tipo, x, y, m));
		
	}
	
	@Override
	public void onAgentMoved(String nombreAgente, int x, int y) {
		
		agentes.get(nombreAgente).setX(x);
		agentes.get(nombreAgente).setY(y);
		
	}

}