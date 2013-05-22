package game;
import helpers.Text;
import interfaces.IGame;

import java.awt.Font;
import java.io.InputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Vector;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.ResourceLoader;


public class DisplayExample implements Serializable{
	
	private class ServerThread implements Runnable{
		private DisplayExample de;
		public ServerThread(DisplayExample i){
			de = i;
			
		}
		public void run() {
			try {
				/*InetAddress ip = InetAddress.getLocalHost();
				String ipp = ip.getHostAddress().toString();
				gsp = (IGame)Naming.lookup("//"+ipp+":2222/ProxyServer");*/
				/*gsp.addActiveNode();
				gsp.updateMap(clients);
				de.activeNodes = gsp.getActiveNodes();*/
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	public IGame gsp = null;
	
	public int nClients = 10;
	public int centerX = 400;
	public int centerY = 300;
	public int radius = 250;
	public double theta = (2*Math.PI)/nClients;
	public Vector<Node> nodes;
	public String extIP = "";
	private HUD hud;
	public static Vector<String> clients;
	public int activeNodes = 0;
	/** time at last frame */
	long lastFrame;
 
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	public DisplayExample(IGame gsp){
		hud =  new HUD();
		nodes = new Vector<Node>();
		clients = new Vector<String>();
		this.gsp = gsp;
		try {
			
			try{
				clients = NetworkScanner.load();
			}catch(Exception e){
				clients = NetworkScanner.scan();
				NetworkScanner.write(clients);
			}
			nClients = clients.size();
			int k = 0;
			theta = (2*Math.PI)/nClients;
			for(String s :clients){
				Node toAdd = new Node();
				if(k%2 == 0){
					radius = 200;
				} else {
					radius = 250;
				}
				toAdd.setX(centerX+(int)(Math.cos(theta*k)*radius));
				toAdd.setY(centerY+(int)(Math.sin(theta*k)*radius));
				toAdd.width = 10;
				toAdd.height = 10;
				toAdd.r = 1.0f;
				toAdd.g = 0.0f;
				toAdd.b = 0.0f;
				toAdd.a = 1.0f;
				//toAdd.text = s;
				nodes.add(toAdd);
				k++;
			}
			
			extIP = NetworkScanner.getIp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServerThread st = new ServerThread(this);
		st.run();
	}
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
 
	    return delta;
	}
 
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
 
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("Uplink clone - FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	@SuppressWarnings("unchecked")
	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 600, 0, -1, 20);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glClearColor(0.75f, 0.75f, 0.75f, 1);
		UnicodeFont font2 = null;
		try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream("res/bitlow.ttf");
			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(24f); // set font size
			font2 = new UnicodeFont(awtFont2);
			
			font2.getEffects().add(new ColorEffect());
			font2.addAsciiGlyphs();
			font2.loadGlyphs();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		GL11.glClearColor(0, 0, 0, 1);
		Node.fnt = font2;
		Text.fnt = font2;
		HUD.fnt = font2;
		hud.add(extIP, 5, 5);
		getDelta();
		lastFPS = getTime();
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			update(delta);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
		    // set the color of the quad (R,G,B,A)
		    for(Node n : nodes){
		    	n.Draw();
		    	n.DrawToCenter(centerX+5, centerY+5);
		    }
		    
		    for(Node n :nodes){
		    	n.renderText();
		    }
		    GL11.glColor3f(0.5f,0.5f,1.0f);
		    GL11.glBegin(GL11.GL_QUADS);
	        GL11.glVertex2f(centerX,centerY);
					GL11.glVertex2f(centerX,centerY+10);
					GL11.glVertex2f(centerX+10,centerY+10);
					GL11.glVertex2f(centerX+10,centerY);
		    GL11.glEnd();
		    hud.render();
		    try {
				activeNodes = gsp.getActiveNodes();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		    hud.drawString("Active Nodes:"+activeNodes,200,5);
			Display.update();
			Display.sync(60);
		}
		
		try {
			gsp.removeActiveNode();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		Display.destroy();
	}
	
	
	public void update(int delta)
	{
		pollInput(delta);
		updateFPS();
	}
	
	public void pollInput(int delta) {
		
    if (Mouse.isButtonDown(0)) {
	    int x = Mouse.getX();
	    int y = 600 - Mouse.getY();
		for (Node n : nodes){
			if(n.rect.contains(x, y)){
				if(n.clicked)
					n.clicked=false;
				else 
					n.clicked = true;
			}
		}
	}
		
	if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
	    System.out.println("SPACE KEY IS DOWN");
	}
		
	while (Keyboard.next()) {
	    if (Keyboard.getEventKeyState()) {
	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
	        	Camera.translate(0.01f,0,0);
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_S) {
		    Camera.translate(0,0.01f,0);
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
			Camera.translate(-0.01f,0,0);
		}
		if(Keyboard.getEventKey() == Keyboard.KEY_W){
			Camera.translate(0,-0.01f,0);
		}
	    } else {
	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
		    //System.out.println("A Key Released");
	        }
	    	if (Keyboard.getEventKey() == Keyboard.KEY_S) {
		    //System.out.println("S Key Released");
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
		    //System.out.println("D Key Released");
		}
	    }
	}
    }

	
	/*public static void main(String[] argv) {
		
		DisplayExample displayExample = new DisplayExample();
		displayExample.start();
	}*/

	public void updateMap(Vector<String> obj) {
		for(String s : obj){
			System.out.println(s);
		}
	}
}
