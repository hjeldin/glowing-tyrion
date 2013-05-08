package helpers;

import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

public class Text {
	public static UnicodeFont fnt;
	public int x,y;
	public String text;
	public Text(String text){
		this.text = text;
	}
	
	public void render(int x, int y){
		fnt.drawString(this.x + x, this.y + y, text, Color.black);
	}
}
