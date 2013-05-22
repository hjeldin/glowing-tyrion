package game;
import interfaces.*;
import java.io.Serializable;

public class ClientRemoteListener implements Serializable,IRemoteListener{
	public void remoteEvent(Object obj){
		System.out.println("lol" + obj.toString());
	}


	public void update(){
		//System.out.println("called update");
	}
}