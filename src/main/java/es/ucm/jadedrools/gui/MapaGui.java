package es.ucm.jadedrools.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import es.ucm.jadedrools.AgentObserver;
import es.ucm.jadedrools.mapa.Mapa;
import es.ucm.jadedrools.objetos.Mineral;

public class MapaGui extends JPanel implements AgentObserver{
	
	private Mapa mapa;
	private Hashtable<String, AgenteDummy> agentes;
	
	BufferedImage img_explorador = null;
	BufferedImage img_minero = null;
	BufferedImage img_transportista = null;
	
	private int CASILLA_SIZE = 36;

	public MapaGui(Mapa m) {
		
		super();		
		mapa = m;
		agentes = new Hashtable<>();
		
		try {
			img_explorador = ImageIO.read(new File("pictures/explorer.png"));
			img_minero = ImageIO.read(new File("pictures/miner.png"));
			img_transportista = ImageIO.read(new File("pictures/transport.png"));
		} catch (IOException e) { System.out.println("Ha petado la imageen!"); }
		
		this.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					CASILLA_SIZE += e.getScrollAmount()*-e.getWheelRotation();
					if (CASILLA_SIZE < 0) CASILLA_SIZE = 0;
					if (CASILLA_SIZE > 100) CASILLA_SIZE = 100;
					repaint();
				}
				
			}
		});
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (int i = 0; i < mapa.getAncho(); i++){
			for (int j = 0; j < mapa.getAlto(); j++){
			
				switch (mapa.getCasilla(i, j).getTipo()) {
				case NORMAL:
					g.setColor(new Color(82, 181, 0));
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
					
					g.fillOval(
							i*CASILLA_SIZE + CASILLA_SIZE/4, 
							j*CASILLA_SIZE + CASILLA_SIZE/4, 
							CASILLA_SIZE/2, 
							CASILLA_SIZE/2);
				}
				
				ArrayList<AgenteDummy> arr = new ArrayList<>(agentes.values());
				
				for (AgenteDummy agente: arr){
					
					BufferedImage img = null;
					
					switch (agente.getTipo()) {
					case EXPLORADOR:
						img = img_explorador;
						break;
					case MINERO:
						img = img_minero;
						break;
					case TRANSPORTISTA:
						img = img_transportista;
						break;
					default:
						break;
					}
					
					if (img != null){
						
						int dstx1 = agente.getX()*CASILLA_SIZE;
						int dsty1 = agente.getY()*CASILLA_SIZE;
						int dstx2 = agente.getX()*CASILLA_SIZE + CASILLA_SIZE;
						int dsty2 = agente.getY()*CASILLA_SIZE + CASILLA_SIZE;
						
						int srcx1 = 0;
						int srcy1 = 0;
						int srcx2 = img.getWidth();
						int srcy2 = img.getHeight();
						
						g.drawImage(img, dstx1, dsty1, dstx2, dsty2, srcx1, srcy1, srcx2, srcy2, this);
						
					}
				}
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintComponent(g);
	}
	
	public void agregarAgenteVisual(String nombreAgente, TipoAgente tipo, int x, int y){
		
		agentes.put(nombreAgente, new AgenteDummy(tipo, x, y));
		
	}
	
	@Override
	public void onAgentMoved(String nombreAgente, int x, int y) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				AgenteDummy a = agentes.get(nombreAgente);
				
				a.setX(x);
				a.setY(y);
				
				agentes.put(nombreAgente, a);
				
				repaint();
			}
		});
	}
}