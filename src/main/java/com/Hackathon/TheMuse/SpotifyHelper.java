package com.Hackathon.TheMuse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;

public class SpotifyHelper {
	public static final String MetaDataFolder = "Metadata_temp";
	public static final String appSecretToken = "5bc7efad4f6341f39c331e871ad30458";
	public static final String appID = "11c2fe73370f43d98ece45efb4444478";
	public static final String accessToken = "BQDuGVJT_voRV-RHi7EQvJDnNQHAgMnkSrzt6ItuGwVBpRNbXd_O17_Vrq54wMtGUAkTXO_ehbtCYHARsKUMSvg5l4hGFS78-PIeOhSKClwahQsx0E5HvqaDmBFnzh7Qlxy_GeCQEya-cTgsEgMQDKmPoB4PN_teLwOknF-jqpIK6-QZ";
	public static final String searchAPI = "search";
	public static final String playlistAPI = "playlists";
	public static final String playlist = "playlist";
	public static final String userAPI = "users";
	public static final String user = "user";
	public static final int limit = 5;
	public static final String baseURL = "https://api.spotify.com/v1/";
	public static final String userID = "amitshakya90";
	public static final String SpotifyTrackAddAPI = "spotify:track:";
	public static final String SpotifyPlaylistURL = "https://open.spotify.com/";
	public static final String redirectUri = "http://localhost";
	public static ArrayList<HashMap<String, Object>> ArtistInfoMap = new ArrayList<HashMap<String, Object>>();
	public static final String code = "playlist-modify-private";

	public void setArtistInfo(ArrayList<HashMap<String, Object>> artistInfo) {
		ArtistInfoMap = artistInfo;
	}

	public String discoverArtist(String orignalartist) throws Exception {
		String json = "";
		Discoverer discover = new Discoverer();
		ArrayList<HashMap<String, Object>> ArtistInfo = new ArrayList<HashMap<String, Object>>();
		ArtistInfo = discover.getBandInfoForArtist(orignalartist);
		setArtistInfo(ArtistInfo);
		json = new Gson().toJson(ArtistInfo);
		return json;
	}

	public String getFaceBookData() throws Exception {
		String json = "";
		FacebookHelper faceBook = new FacebookHelper();
		faceBook.getFacebookInstance();
		ArrayList<HashMap<String, Object>> fbObject = faceBook.getMusicInfo();
		json = new Gson().toJson(fbObject);
		return json;
	}

	public String getAccessToken() {
		final Api api = Api.builder()
	            .clientId(appID)
	            .clientSecret(appSecretToken)
	            .build();
		final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();
		final SettableFuture<ClientCredentials> responseFuture = request.getAsync();
	    Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
	      public void onSuccess(ClientCredentials clientCredentials) {
	        System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
	        System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");
	      }
	      public void onFailure(Throwable throwable) {
	      }
	    });
		return null;
	}

	public String createPlaylistWrapper(String orignalartist) {
		SpotifyHelper spotify = new SpotifyHelper();
		Discoverer discover = new Discoverer();
		String json = "";
		ArrayList<HashMap<String, Object>> playlistParams = new ArrayList<HashMap<String, Object>>();
		for (HashMap<String, Object> artist : ArtistInfoMap) {
			playlistParams = spotify.getTrackInfo(artist.get(Constants.Artist).toString(),
					artist.get(Constants.Track).toString(), playlistParams);
		}
		json = new Gson().toJson(playlistParams);
		discover.FileWriter(json, MetaDataFolder + "/playlistinfo.json");
		HashMap<String, Object> playlistInfo = createPlaylist(orignalartist);
		String tracks = "";
		String temp = "";
		int count = 1;
		for (HashMap<String, Object> playlistParam : playlistParams) {
			tracks = "";
			if (playlistParam.containsKey(Constants.TrackID)) {
				tracks = playlistParam.get(Constants.TrackID).toString();
			} else {
				continue;
			}
			temp = temp + SpotifyTrackAddAPI + tracks + ",";
			tracks = temp;
			if (count % 10 == 0) {
				spotify.pushSongsToPlaylist(tracks.substring(0, tracks.lastIndexOf(",")),
						playlistInfo.get(Constants.PlayListID).toString());
				tracks = "";
				temp = "";
			}
			count++;
		}
		String url = String.format("%s%s/%s/%s/%s", SpotifyPlaylistURL, user, userID, playlist,
				playlistInfo.get(Constants.PlayListID).toString());
		return url;
	}

	public ArrayList<HashMap<String, Object>> getTrackInfo(String artist, String track,
			ArrayList<HashMap<String, Object>> playlistParams) {
		String limit = "1";
		String offset = "0";
		HashMap<String, Object> trackInfo = new HashMap<String, Object>();
		try {
			String URL = String.format("%s%s?q=%s+AND+%s:%s&access_token=%s&offset=%s&limit=%s&type=%s", baseURL,
					searchAPI, URLEncoder.encode(track, "UTF-8"), Constants.Artist, URLEncoder.encode(artist, "UTF-8"),
					accessToken, offset, limit, Constants.Track);
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			StringBuilder sb = new StringBuilder("");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			String response = sb.toString();
			trackInfo = getTrackInfoWrapper(response);
			playlistParams.add(trackInfo);
			// System.out.println(trackInfo);
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return playlistParams;
		}
		return playlistParams;

	}

	public HashMap<String, Object> getTrackInfoWrapper(String response) {
		HashMap<String, Object> trackInfo = new HashMap<String, Object>();
		JSONObject obj = new JSONObject(response);
		obj = obj.getJSONObject(Constants.Tracks);
		JSONArray levelI = obj.getJSONArray(Constants.Items);
		for (int j = 0; j < levelI.length(); j++) {
			JSONObject objectI = levelI.getJSONObject(j);
			trackInfo.put(Constants.Track, objectI.getString(Constants.Name));
			trackInfo.put(Constants.TrackID, objectI.getString(Constants.ID));
			JSONArray levelII = objectI.getJSONArray(Constants.Artists);
			for (int k = 0; k < levelII.length(); k++) {
				JSONObject objectII = levelII.getJSONObject(j);
				trackInfo.put(Constants.Artist, objectII.getString(Constants.Name));
				trackInfo.put(Constants.ArtistID, objectII.getString(Constants.ID));
			}
		}
		return trackInfo;
	}

	public static HashMap<String, Object> createPlaylist(String playlist) {
		String description = "This is playlist created by TheMuse for artist [" + playlist + "]";
		System.out.println("Creating playlist [" + playlist + "]");
		Boolean nPublic = false;
		try {
			HashMap<String, Object> playListParams = new HashMap<String, Object>();
			HashMap<String, Object> playListInfo = new HashMap<String, Object>();
			playListParams.put(Constants.Description, description);
			playListParams.put(Constants.nPublic, nPublic);
			playListParams.put(Constants.Name, playlist);
			String URL = String.format("%s%s/%s/%s?access_token=%s", baseURL, userAPI,
					URLEncoder.encode(userID, "UTF-8"), playlistAPI, accessToken);
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			String input = new Gson().toJson(playListParams);
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			StringBuilder sb = new StringBuilder("");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			String response = sb.toString();
			JSONObject obj = new JSONObject(response);
			playListInfo.put(Constants.PlayList, obj.get(Constants.Name));
			playListInfo.put(Constants.PlayListID, obj.get(Constants.ID));
			return playListInfo;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return null;

		}
	}

	public void pushSongsToPlaylist(String tracks, String playlistid) {
		try {
			System.out.println("Adding songs to playlist..");
			HashMap<String, Object> playListParams = new HashMap<String, Object>();
			String URL = String.format("%s%s/%s/%s/%s/%s?uris=%s&access_token=%s", baseURL, userAPI,
					URLEncoder.encode(userID, "UTF-8"), playlistAPI, playlistid, Constants.Tracks, tracks, accessToken);
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			System.out.println(playListParams);
			String input = new Gson().toJson(playListParams);
			System.out.println(input);
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			StringBuilder sb = new StringBuilder("");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			String response = sb.toString();
			JSONObject obj = new JSONObject(response);
			if (obj.has(Constants.SnapshotID)) {
				System.out.println("Playlist snapshot [" + obj.getString(Constants.SnapshotID).toString() + "]");
			}

		} catch (Exception e) {
			System.out.println("Exception " + e.getStackTrace());
		}

	}

}
