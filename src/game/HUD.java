package game;

import helpers.Text;

import java.awt.Rectangle;
import java.util.Vector;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

public class HUD {
	public static UnicodeFont fnt;
	private Rectangle rect;
	private Vector<Text> texts;
	public HUD(){
		rect = new Rectangle(0,570,800,30);
		texts = new Vector<Text>();
	}
	
	public void add(String toAdd, int x, int y){
		Text t = new Text(toAdd);
		t.x = x;
		t.y = y;
		texts.add(t);
	}
	
	public void render() {
		//GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor3f(0.75f, 0.75f, 0.75f);
		// draw quad
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(rect.x, rect.y);
		GL11.glVertex2f(rect.x + rect.width, rect.y);
		GL11.glVertex2f(rect.x + rect.width, rect.y + rect.height);
		GL11.glVertex2f(rect.x, rect.y + rect.height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		for(Text t : texts)
			t.render(rect.x, rect.y);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		//GL11.glEnable(GL11.GL_LIGHTING);
    }

	public void drawString(String string, int i, int j) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fnt.drawString(rect.x + i, rect.y + j, string, Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
}
