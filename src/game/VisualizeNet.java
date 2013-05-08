package game;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class VisualizeNet{
	Gson jsonSerializer;
	Internet alice ;
	public VisualizeNet(){
		jsonSerializer = new Gson();
		try{
			FileReader fread = new FileReader("map.dat");
			BufferedReader in = new BufferedReader(fread);
			String jsonFile = "";
			String tmpStr = "";
			while((tmpStr = in.readLine()) != null){
				jsonFile += tmpStr;
			}
			alice = new Internet();
			alice = jsonSerializer.fromJson(jsonFile,Internet.class);
			alice.GenerateISP();
			System.out.println(alice.toString());
		} catch(Exception e){
			System.out.println("errore"+e.toString());
		}
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
		GL11.glClearColor(0, 0, 0, 1);
		while (!Display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
			alice.draw();
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}

	public static void main(String args[]){
		VisualizeNet displayExample = new VisualizeNet();
		displayExample.start();
	}
}