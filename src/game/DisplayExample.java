package game;
import helpers.Text;
import interfaces.IGame;

import java.awt.Font;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import com.google.gson.Gson;

import java.rmi.server.RMIClassLoader;
import java.rmi.server.UnicastRemoteObject;
import interfaces.*;
public class DisplayExample implements Serializable{
		
	private class RemoteThread extends Thread {
		private ClientRemoteListener cdl;
		private DisplayExample disp;
		public RemoteThread(ClientRemoteListener cdl, DisplayExample disp){
			this.cdl = cdl;
			this.disp = disp;
		}

		public synchronized void run(){
			while(cdl.gameMap == null){
				System.out.println("Still here!");
				try{
				Thread.sleep(1000);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			disp.nodes = new Vector<Node>();
			disp.isps = new Vector<Node>();
			Gson jsonSerializer;
			jsonSerializer = new Gson();
			Internet alice;
			alice = new Internet();
			alice = jsonSerializer.fromJson(cdl.gameMap,Internet.class);
			alice.GenerateISP();
			disp.lulz = alice;
			for(ISP i : alice.isps){
				for(Network k : i.networks){
					for(NodeData m : k.nodes){
						Node n = null;
						if(m.active)
							n = new Node(15,15);
						else
							n = new Node(10,10);
						n.nd = m;
						n.r = n.nd.nodeColor[0];
						n.g = n.nd.nodeColor[1];
						n.b = n.nd.nodeColor[2];
						n.a = 1.0f;
						n.setX(m.x);
						n.setY(m.y);
						disp.nodes.add(n);
					}
					Node net = new Node(10,10);
					net.nd = k.gateway;
					net.r = 1;
					net.g = 0;
					net.b = 0;
					net.a = 1;
					net.setX(k.gateway.x);
					net.setY(k.gateway.y);
					disp.nodes.add(net);
				}
				Node isp = new Node(10,10);
				isp.nd = i.me;
				isp.r = 0;
				isp.g = 1;
				isp.b = 0;
				isp.a = 1;
				isp.setX(i.me.x);
				isp.setY(i.me.y);
				disp.isps.add(isp);
			}
			synchronized (disp) {
			    disp.notify();
			}
		}
	}

	public Vector<Node> nodes = new Vector<Node>();
	public Vector<Node> isps = new Vector<Node>();
	public IGame gsp = null;
	public IGameAdmin gspa = null;
	public String extIP = "";
	private HUD hud;
	public int activeNodes = 0;
	private int[] translations;
	protected float[] myColor = new float[3];
	/** time at last frame */
	long lastFrame;
	public RemoteThread t;
	public int oldX = 0, oldY = 0;
 
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	private ClientRemoteListener cdl;
	private IRemoteListener stub ;
	public Internet lulz;
	private boolean isADmin = false;
	
	public DisplayExample(IGame gsp, IGameAdmin gspa){

		hud =  new HUD();
		if (gsp != null)
			this.gsp = gsp;
		else{
			this.gspa = gspa;
			this.isADmin = true;
		}
		try {
			extIP = NetworkScanner.getIp();
			//Class classClient = RMIClassLoader.loadClass(System.getProperty("java.rmi."));
			cdl=new ClientRemoteListener(extIP);
			stub = (IRemoteListener) UnicastRemoteObject.exportObject(cdl);
			gsp.addActiveNode(stub);
			//myColor = gsp.getColor();
			//cdl.setColor(myColor);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		//w s d a
		translations = new int[4];
		translations[0] = 0;
		translations[1] = 0;
		translations[2] = 0;
		translations[3] = 0;
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
	public synchronized void start() {
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
		GL11.glOrtho(0, 800, 600, 0, 0, 20);
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
		try{
			gsp.updateMap(new Vector<String>());
			t = new RemoteThread(cdl,this);
			t.start();
			synchronized (this) {
				this.wait();
			}
			
			//call remote thread
		}catch(Exception e){
			e.printStackTrace();
		}
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
			update(delta);
			if(cdl.update()){
				try {
					t.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
			if(lulz != null){
				//lulz.draw();
				for(Node n : nodes) 
				{ 
					n.Draw();
					n.DrawToCenter(n.nd.centerX,n.nd.centerY);
				}
				int precX = isps.get(isps.size()-1).getX();
				int precY = isps.get(isps.size()-1).getY();
				for(Node n : isps) 
				{ 
					n.DrawToCenter(precX,precY);
					precX = n.getX();
					precY = n.getY();
					n.Draw();
				}
			}
			
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			
		    hud.render();
		    try {
				activeNodes = gsp.getActiveNodes();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		    hud.drawString("ACTIVE NODES:"+activeNodes,200,5);
			Display.update();
			Display.sync(60);
		}
		
		try {
			gsp.removeActiveNode(stub);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Display.destroy();
	}
	
	
	public void update(int delta)
	{
		try {
			pollInput(delta);
			updateFPS();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pollInput(int delta) throws Exception {
		int dWheel = Mouse.getDWheel();
		Camera.zoom(dWheel);

	    if (Mouse.isButtonDown(0)) {
		    int x = Mouse.getX();
		    int y = 600 - Mouse.getY();
		    //System.out.println("x: " +x+ " oldX: "+oldX+" y: " +y+ " oldY: "+oldY);
		    if(x != oldX || y != oldY){
				for(Node n : nodes)
					if(n.rect.contains(x-Camera.tx, y+Camera.ty) ){
						String str = gsp.infect(n.nd.ip, cdl.getIp());
						System.out.println(str);
					}
				oldX = x;
				oldY = y;
			}
	    }
			
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
		    System.out.println("SPACE KEY IS DOWN");
		    gsp.resetMap();
		}
		
		float trX = 0, trY = 0, trZ = 0;
		
		if(translations[0] == 1){
			trY = -5f;
		}
		if(translations[1] == 1){
			trY = 5f;
		}
		if(translations[2] == 1){
			trX = -5f;
		}
		if(translations[3] == 1){
			trX = 5f;
		}
		Camera.translate(trX,trY,trZ);
		
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
	        	translations[3] = 1;

					}
					if (Keyboard.getEventKey() == Keyboard.KEY_S) {
					    translations[1] = 1;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_D) {
					    translations[2] = 1;
					}
					if(Keyboard.getEventKey() == Keyboard.KEY_W){
					    translations[0] = 1;
					}
		    } else {
		        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
				    translations[3] = 0;
		        }
	    		if (Keyboard.getEventKey() == Keyboard.KEY_S) {
				    translations[1] = 0;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
				    translations[2] = 0;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_W) {
					translations[0] = 0;
				}
		    }
			}
    }
}
