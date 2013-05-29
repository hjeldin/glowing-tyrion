package game;

import java.io.Serializable;

import org.lwjgl.opengl.GL11;

public class Camera implements Serializable{
	public static float tx=0;
	public static float ty=0;
	public static float tz=0;
	public static float zoom=1;
	public static void translate(float x,float y,float z){
		tx += x;
		ty += y;
		tz += z;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
	    GL11.glTranslatef(tx,ty,zoom);
	    GL11.glOrtho(0,800,600,0,-1,20.f);
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static void loadIdentity()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
	    GL11.glLoadIdentity();
	    GL11.glOrtho(0,800,600,0,-1,20.f);
	    GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static void zoom(float dx){
		if(dx >0){
			zoom += 0.01f;
		} else if(dx < 0){
			zoom -= 0.01f;
		}
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0f,0.0f,zoom);
	}
}
