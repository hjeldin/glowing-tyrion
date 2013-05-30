package game;
import java.awt.Rectangle;
import java.io.Serializable;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;


public class Node implements Serializable{
	public NodeData nd;
	public Rectangle rect = new Rectangle(0,0,10,10);
	public int width,height;
	public float r,g,b,a;
	
	public boolean clicked = false;
	public static UnicodeFont fnt;
	
	public void Draw(){
		if(nd.active)
			GL11.glColor4f(r,g,b,a);
		else
			GL11.glColor4f(0,0,1,1);
		GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex3f(getX(),getY(),5.0f);
			GL11.glVertex3f(getX(),getY()+height,5.0f);
			GL11.glVertex3f(getX()+width,getY()+height,5.0f);
			GL11.glVertex3f(getX()+width,getY(),5.0f);
	    GL11.glEnd();
	}
	
	public void renderText(){
		if(clicked){
	    	GL11.glEnable(GL11.GL_TEXTURE_2D);
	    	fnt.drawString(getX(), getY(), nd.ip+"");
		    GL11.glDisable(GL11.GL_TEXTURE_2D);
	    }
	}
	
	public void DrawToCenter(float x, float y){
		GL11.glColor4f(r, g, b, 0.3f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex3f(this.getX()+5, this.getY()+5,5.0f);
			GL11.glVertex3f(x, y,5.0f);
		GL11.glEnd();
	}

	public void setX(int x) {
		this.nd.x = x;
		rect.x = x;
	}

	public int getX() {
		return nd.x;
	}

	public void setY(int y) {
		this.nd.y = y;
		rect.y = y;
	}

	public int getY() {
		return nd.y;
	}

	public String toString(){
		String tmpStr = "";
		tmpStr += "IP: "+nd.ip;
		tmpStr += "\nAttivo: "+nd.active;
		return tmpStr;
	}

}
