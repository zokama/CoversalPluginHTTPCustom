package com.coversal.plugin.httpcustom;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import android.os.RemoteException;
import com.coversal.ucl.plugin.Controller;


public class HttpController extends Controller {

	HttpCustom profile;
	String requestBase;
	
	
	
	public HttpController(HttpCustom hc) {
		super(hc);
		
		profile = hc;
		requestBase = "";
		
		defineCommand(PLAY_PAUSE, "", true);
		defineCommand(STOP, "", true);
		defineCommand(REWIND, "", true);
		defineCommand(FORWARD, "", true);
		defineCommand(NEXT, "", true);
		defineCommand(PREVIOUS, "", true);
		defineCommand(VOL_UP, "", true);
		defineCommand(VOL_DOWN, "", true);
		defineCommand(POWER, "", true);
		defineCommand(FULLSCREEN, "", true);
		defineCommand(PROGRAM_UP, "", true);
		defineCommand(PROGRAM_DOWN, "", true);
		defineCommand(OK, "", true);
		defineCommand(UP, "", true);
		defineCommand(DOWN, "", true);
		defineCommand(LEFT, "", true);
		defineCommand(RIGHT, "", true);
	}

	
	
	void setRequestBase(String request) {
		requestBase = request;
	}
	
	
	
	@Override
	public boolean execute(String command) throws RemoteException {
		HttpCustom.debug("Executing: "+ requestBase + getCommand(command));
		
		HttpPost request = new HttpPost(requestBase + getCommand(command));

		try {
			HttpResponse response = profile.httpClient.execute(request);
			response.getEntity().consumeContent();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	
	
	@Override
	public List<String> getContextMenuItems(int arg0) throws RemoteException {
		return null;
	}

	@Override
	public String getLayoutName() throws RemoteException {
		return "coversal1";
	}

	@Override
	public int getMediaDuration() throws RemoteException {
		return 0;
	}

	@Override
	public int getMediaPosition() throws RemoteException {
		return 0;
	}

	@Override
	public String getPlayingMedia() throws RemoteException {
		return null;
	}

	@Override
	public boolean isPlaying() throws RemoteException {
		return false;
	}

	@Override
	public void onItemSelected(String arg0, String arg1) throws RemoteException {
	}

}
