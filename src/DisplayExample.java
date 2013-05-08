import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Vector;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.util.ResourceLoader;


public class DisplayExample {
	public int nClients = 10;
	public int centerX = 400;
	public int centerY = 300;
	public int radius = 100;
	public double theta = (2*Math.PI)/nClients;
	public Vector<Node> nodes;
	UnicodeFont font;
	public DisplayExample(){
		nodes = new Vector<Node>();
		
		try {
			//Vector<String> clients = NetworkScanner.scan();		
			Vector<String> clients = new Vector<String>();
			clients.add("157.27.241.220");
			clients.add("157.27.241.2");
			clients.add("157.27.241.21");
			clients.add("157.27.241.215");
			clients.add("157.27.241.222");
			clients.add("157.27.241.229");
			clients.add("157.27.241.226");
			clients.add("157.27.241.225");
			clients.add("157.27.241.221");
			
			nClients = clients.size();
			theta = (2*Math.PI)/nClients;
			int k =0;
			for(String s :clients){
				Node toAdd = new Node();
				toAdd.x = centerX+(int)(Math.cos(theta*k)*radius)+0;
				toAdd.y = centerY+(int)(Math.sin(theta*k)*radius)+0;
				toAdd.width = 10;
				toAdd.height = 10;
				toAdd.r = 1.0f;
				toAdd.g = 0.0f;
				toAdd.b = 0.0f;
				toAdd.a = 1.0f;
				toAdd.text = s;
				nodes.add(toAdd);
				k++;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800,600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		//up-left = 0,0
		GL11.glOrtho(0, 800, 600, 0, -1,1000);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		// init OpenGL here
		Font awtFont = new Font("Arial", Font.PLAIN, 10);
		try {
	        InputStream inputStream = ResourceLoader.getResourceAsStream("res\\bitlow.ttf");

	        Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
	        awtFont2 = awtFont2.deriveFont(15f); // set font size
	        font = new UnicodeFont(awtFont2);
	        font.getEffects().add(new ColorEffect());
	        ((UnicodeFont) font).addAsciiGlyphs();
	        
			try {
				font.loadGlyphs();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }   
		
		for(Node n : nodes){
			Node.fnt = font;
		}
		
		
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glPushMatrix();
			GL11.glTranslatef(-200, 0, 0);
		    for(Node n:nodes){
		    	n.Draw();
		    	n.DrawToCenter(centerX+5, centerY+5);
		    }
		    GL11.glColor3f(0.5f,0.5f,1.0f);
		    GL11.glDisable(GL11.GL_CULL_FACE);
		    GL11.glBegin(GL11.GL_QUADS);
		        GL11.glVertex2f(centerX,centerY);
				GL11.glVertex2f(centerX,centerY+10);
				GL11.glVertex2f(centerX+10,centerY+10);
				GL11.glVertex2f(centerX+10,centerY);
		    GL11.glEnd();
		    GL11.glPopMatrix();
		    GL11.glPushMatrix();
			GL11.glTranslatef(200, 0, 0);
		    for(Node n:nodes){
		    	n.Draw();
		    	n.DrawToCenter(centerX+5, centerY+5);
		    }
		    GL11.glColor3f(0.5f,0.5f,1.0f);
		    GL11.glBegin(GL11.GL_QUADS);
		        GL11.glVertex2f(centerX,centerY);
				GL11.glVertex2f(centerX,centerY+10);
				GL11.glVertex2f(centerX+10,centerY+10);
				GL11.glVertex2f(centerX+10,centerY);
		    GL11.glEnd();
		    GL11.glPopMatrix();
		    GL11.glColor3f(1.0f,1.0f,1.0f);
		    GL11.glBegin(GL11.GL_LINE_STRIP);
		    	GL11.glVertex2f(210, centerY+5);
		    	GL11.glVertex2f(600, centerY+5);
		    GL11.glEnd();
			pollInput();
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			font.drawString(0, 50, "UPLINK CLONE", Color.red);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
			Display.update();
		}
		
		Display.destroy();
	}
	
public void pollInput() {
		
        if (Mouse.isButtonDown(0)) {
	    int x = Mouse.getX();
	    int y = Mouse.getY();
			
	    System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
	}
		
	if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
	    System.out.println("SPACE KEY IS DOWN");
	}
		
	while (Keyboard.next()) {
	    if (Keyboard.getEventKeyState()) {
	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
		    System.out.println("A Key Pressed");
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_S) {
		    System.out.println("S Key Pressed");
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
		    System.out.println("D Key Pressed");
		}
	    } else {
	        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
		    System.out.println("A Key Released");
	        }
	    	if (Keyboard.getEventKey() == Keyboard.KEY_S) {
		    System.out.println("S Key Released");
		}
		if (Keyboard.getEventKey() == Keyboard.KEY_D) {
		    System.out.println("D Key Released");
		}
	    }
	}
    }

	
	public static void main(String[] argv) {
		DisplayExample displayExample = new DisplayExample();
		displayExample.start();
	}
}
