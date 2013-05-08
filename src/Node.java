import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Font;


public class Node {
	public int x,y;
	public int width,height;
	public float r,g,b,a;
	public String text;
	public static Font fnt;
	public void Draw(){
		GL11.glColor4f(r,g,b,a);
		GL11.glBegin(GL11.GL_QUADS);
	        GL11.glVertex2f(x,y);
			GL11.glVertex2f(x,y+height);
			GL11.glVertex2f(x+width,y+height);
			GL11.glVertex2f(x+width,y);
	    GL11.glEnd();
	    GL11.glPushMatrix();
//	    GL11.glScalef(0.5f, 0.5f, 1);
	    fnt.drawString(x, y,text);
	    GL11.glDisable(GL11.GL_TEXTURE_2D);
	    GL11.glPushMatrix();
	}
	
	public void DrawToCenter(int x, int y){
		GL11.glColor4f(r,g,b,0.1f);
		GL11.glLineWidth(1.0f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex2f(this.x+this.width/2,this.y+this.height/2);
			GL11.glVertex2f(x,y);
		GL11.glEnd();
	}
}
