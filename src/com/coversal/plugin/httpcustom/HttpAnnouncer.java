package com.coversal.plugin.httpcustom;

import com.coversal.ucl.plugin.ProfileAnnouncer;

public class HttpAnnouncer extends ProfileAnnouncer {

	public HttpAnnouncer() {
		defineProfile(HttpCustom.PROFILE_NAME, HttpCustom.class);
	}

}
