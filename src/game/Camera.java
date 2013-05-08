package game;

import org.lwjgl.opengl.GL11;

public class Camera {
	public static float tx;
	public static float ty;
	public static float tz;
	
	public static void translate(float x,float y,float z){
		tx += x;
		ty += y;
		tz += z;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
	    GL11.glTranslatef(tx,ty,tz);
	    GL11.glOrtho(0,800,600,0,-1,20.f);
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

}
