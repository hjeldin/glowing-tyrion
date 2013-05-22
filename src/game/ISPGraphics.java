package game;
import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;


public class ISPGraphics{
	public Rectangle rect = new Rectangle(0,0,10,15);
	private int y;
	private int x;
	public int width,height;

	public ISPGraphics(){
		this.width = 10;
		this.height = 15;
	}

	public void Draw(int x, int y, int z){
		GL11.glColor4f(0.0f,1.0f,0.0f,1.0f);
		GL11.glBegin(GL11.GL_QUADS);
	    GL11.glVertex3f(getX(),getY(),z);
			GL11.glVertex3f(getX(),getY()+height,z);
			GL11.glVertex3f(getX()+width,getY()+height,z);
			GL11.glVertex3f(getX()+width,getY(),z);
    GL11.glEnd();
	}

	public void setX(int x) {
		this.x = x;
		rect.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
		rect.y = y;
	}

	public int getY() {
		return y;
	}
}