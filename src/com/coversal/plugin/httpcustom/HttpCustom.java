package com.coversal.plugin.httpcustom;

import org.apache.http.ProtocolVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.coversal.ucl.api.BrowsableAPI;
import com.coversal.ucl.api.ControllerAPI;
import com.coversal.ucl.api.SpinnerParameter;
import com.coversal.ucl.api.TextParameter;
import com.coversal.ucl.plugin.PlaylistManager;
import com.coversal.ucl.plugin.Profile;
import com.coversal.ucl.plugin.ProfileAnnouncer;

public class HttpCustom extends Profile {

	static final String PROFILE_NAME = "HTTP_Custom";
	private static final String ICON_NAME = "http_custom";
	
	private static final String TYPE = "Type";
	private static final String HOSTNAME = "Hostname";
	private static final String PORT = "Port";
	private static final String PREFIX = "Prefix (Optional)";
	private static final String USERNAME = "Username (Optional)";
	private static final String PASSWORD = "Password (Optional)";
	private static final String TIMEOUT = "Timeout (s)";
	
	private static final ProtocolVersion PROTOCOL_VERSION = new ProtocolVersion("HTTP", 1, 0);
	
	DefaultHttpClient httpClient;
	private HttpController controller;
	
	public HttpCustom(ProfileAnnouncer pa) {
		super(pa);
		
		// controller
		controller = new HttpController(this);
		
		// connection parameters
		defineParameter(TYPE, new SpinnerParameter(true, "http", "https"));
		defineParameter(HOSTNAME, new TextParameter("", true));
		defineParameter(PORT, new TextParameter("80", true));
		defineParameter(PREFIX, new TextParameter("", false));
		defineParameter(USERNAME, new TextParameter("", false));
		defineParameter(PASSWORD, new TextParameter("", false));
		
		// options
		defineOption(TIMEOUT, new TextParameter("2", true));
	}

	@Override
	public void close() {
		// resetting the http client
		httpClient = null;
	}

	@Override
	public BrowsableAPI getBrowser() {
		// no browser feature available
		return null;
	}

	@Override
	public ControllerAPI getController() {
		return controller;
	}

	@Override
	public String getIconName() {
		return ICON_NAME;
	}

	@Override
	public PlaylistManager getPlaylistManager() {
		// no playlist manager available
		return null;
	}

	@Override
	public String getProfileName() {
		return PROFILE_NAME;
	}

	@Override
	public String getTargetNameField() {
		return HOSTNAME;
	}

	@Override
	public boolean init() {
		int timeout = 1000 * Integer.valueOf(getOptionValue(TIMEOUT));
		int port = Integer.valueOf(getValue(PORT));
		
		// thread safe
		HttpParams params = new BasicHttpParams();
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme(getValue(TYPE), PlainSocketFactory.getSocketFactory(), port));
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
		HttpProtocolParams.setVersion(params, PROTOCOL_VERSION);
		
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, registry);
		
		httpClient = new DefaultHttpClient(cm, params);
		
		// authentication
		if (getValue(USERNAME) != null && getValue(PASSWORD) != null) {
			httpClient.getCredentialsProvider().setCredentials(
	                new AuthScope(getValue(HOSTNAME), port),
	                new UsernamePasswordCredentials(getValue(USERNAME), getValue(PASSWORD)));
		}

		controller.setRequestBase(
				getValue(TYPE) + "://" +
				getValue(HOSTNAME) + ":" +
				getValue(PORT) + (getValue(PREFIX).matches("^/.*") ? "" :"/") + 
				getValue(PREFIX));
		
		return true;
	}

	@Override
	public boolean isActive() {
		if (httpClient != null)
			return true;
		else
			return false;
	}

	@Override
	public boolean isPasswordRequired() {
		// not implemented yet
		return false;
	}

	@Override
	public void setPassword(String passwd) {
		// not implemented yet
	}

}
