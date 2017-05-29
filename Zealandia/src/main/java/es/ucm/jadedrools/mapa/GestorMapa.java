package es.ucm.jadedrools.mapa;

public class GestorMapa {
	
	private static GestorMapa instancia;
	
	private Mapa mapa;
	
	private GestorMapa(){
		
		mapa = new Mapa(100, 100);
		
	}
	
	public static GestorMapa getInstancia(){
		
		if (instancia == null)
			instancia = new GestorMapa();
		return instancia;
		
	}
	
	public Mapa getMapa(){ return mapa; }

}
