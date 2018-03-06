package com.Hackathon.TheMuse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Music;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;


public class FacebookHelper {

	public static final String appSecretToken = "c6370e43bc53ee04331b514185e63f44";
	public static final String appID = "1986674064691412";
	public static final String accessToken = "EAAcO3pnpENQBAChVrRjBzXl8JLjv0xlz0EI7VgZAq1qj5Os1rzgqTUMpg54D34bKBOPbQUcdniwWZAT3916ZBWn3UuRbNqZC54WxATVwBazh86HOpJuOZBqQzlNg0GZCFG9TyM9wCxLxg91RBHKgQ5CvvLptSnZAsnXE7nAzbJBbQZDZD";
	public static final String[] musicParameters = { "id", "name", "category", "created_time" };
	public static final int limit = 100;
	public static Facebook fb;

	public static void setFaceBookInstance(Facebook FB) {
		fb = FB;
	}

	public ArrayList<HashMap<String, Object>> getMusicInfo() throws FacebookException {
		try {
			ArrayList<HashMap<String, Object>> musicInfo = new ArrayList<HashMap<String, Object>>();
			Reading params = new Reading();
			params.fields(musicParameters);
			params.addParameter("limit", limit);
			ResponseList<Music> response = fb.getMusic(params);
			for (Iterator<Music> itr = response.iterator(); itr.hasNext();) {
				HashMap<String, Object> info = new HashMap<String, Object>();
				Music music = (Music) itr.next();
				info.put("Band_Name", music.getName());
				info.put("Band_Category", music.getCategory());
				info.put("Band_Added_At", music.getCreatedTime());
				info.put("Band_Page_Id", music.getId());
				musicInfo.add(info);
			}
			return musicInfo;
		} catch (Exception e) {
			System.out.println("Could not fetch music information : [" + e.getMessage() + "]");
			return null;

		}
	}
	
	public void getFacebookInstance() {
		try {
			Facebook fb = new FacebookFactory().getInstance();
			fb.setOAuthAppId(appID, appSecretToken);
			fb.setOAuthAccessToken(new AccessToken(accessToken, null));
			System.out.println("Successfully logged in as: [" + fb.getMe().getName() + "]");
			
			setFaceBookInstance(fb);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
