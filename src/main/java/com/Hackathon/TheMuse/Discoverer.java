package com.Hackathon.TheMuse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
import facebook4j.FacebookException;

public class Discoverer {
	public static String MetaDataFolder = System.getProperty("user.dir") + "\\Metadata_temp";
	public static final String appSecretToken = "66fb1b084bd6069b9de1cfafd0b81202";
	public static final String appID = "66fb1b084bd6069b9de1cfafd0b81202";

	public static final String accessToken = "EAACEdEose0cBAIcRRJDBS8qyOG0pqTHUfsDhvGPdfZCmpJzBC8NJ6VBKJZBdUt2PHZBjX91D8M91dew0j9ELZASEjFvJlr7GVNhip3Hstjgtj1t6VSyzjQ4XZAOHbImop6XHMzLyP6YUAUcZB2EVNAtGQZB0Qc3b5wHr7aPCLYWlWNMDXUNJoYXfGhUCErXHvGZCUgncy4sAvAZDZD";
	public static final String baseURL = "http://ws.audioscrobbler.com/2.0/?";
	public static final String trackAPI = "track.getInfo";
	public static final String artistAPI = "artist.getInfo";
	public static final String similarArtistAPI = "artist.getSimilar";
	public static final String topAlbumAPI = "album.getInfo";
	public static final String topTracksAPI = "artist.getTopTracks";
	public static final String facebookArtistName = "Band_Name";
	public static final String facebookLikedAt = "Band_Added_At";
	public static final String facebookCategory = "Band_Category";
	public static final String lastFMLimit = "5";
	public static final String ImageSize = "large";

	public void getBandInfoFromFacebook() throws FacebookException {
		FacebookHelper faceBook = new FacebookHelper();
		Discoverer lastFm = new Discoverer();
		String json = "";
		faceBook.getFacebookInstance();
		ArrayList<HashMap<String, Object>> fbObject = faceBook.getMusicInfo();
		for (HashMap<String, Object> musicInfo : fbObject) {
			String artist = musicInfo.get(facebookArtistName).toString();
			String category = musicInfo.get(facebookCategory).toString();
			System.out.println(String.format("Currently crawling artist [%s] with category [%s]", artist, category));
			HashMap<String, Object> artistInfo = new HashMap<String, Object>();
			if (artist != null && artist != "") {
				artistInfo = getArtistInfo(artist, true);
			} else {
				return;
			}
			ArrayList<HashMap<String, Object>> similarArtistsInfo = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> topTracksInfo = new ArrayList<HashMap<String, Object>>();
			similarArtistsInfo = lastFm.getTopSimilarArtists(artist, artistInfo);
			topTracksInfo = lastFm.getTopTracks(similarArtistsInfo);
			json = new Gson().toJson(topTracksInfo);
			FileWriter(json, MetaDataFolder + "/" + artist + "_trackinfo.json");
		}
	}

	public ArrayList<HashMap<String, Object>> getBandInfoForArtist(String artist) throws FacebookException {
		Discoverer lastFm = new Discoverer();
		ArrayList<HashMap<String, Object>> similarArtistsInfo = new ArrayList<HashMap<String, Object>>();
		ArrayList<HashMap<String, Object>> topTracksInfo = new ArrayList<HashMap<String, Object>>();
		String json = "";
		try {
			System.out.println(String.format("Currently crawling artist [%s]", artist));
			HashMap<String, Object> artistInfo = new HashMap<String, Object>();
			artistInfo = getArtistInfo(artist, true);

			similarArtistsInfo = lastFm.getTopSimilarArtists(artist, artistInfo);
			json = new Gson().toJson(similarArtistsInfo);
			if (artist.contains("/")) {
				artist = artist.replace("/", "");
			}

			
			FileWriter(json, MetaDataFolder + "/" + artist + "info.json");
			topTracksInfo = lastFm.getTopTracks(similarArtistsInfo);
			json = new Gson().toJson(topTracksInfo);

			FileWriter(json, MetaDataFolder + "/" + artist + "_trackinfo.json");
		} catch (Exception e) {
			System.out.println("Exception happened " + e.getMessage() + "]");
			return null;
		}
		return topTracksInfo;
	}

	public void fer() throws FacebookException {
		FacebookHelper faceBook = new FacebookHelper();
		Discoverer lastFm = new Discoverer();
		String json = "";
		faceBook.getFacebookInstance();
		ArrayList<HashMap<String, Object>> fbObject = faceBook.getMusicInfo();
		for (HashMap<String, Object> musicInfo : fbObject) {
			String artist = musicInfo.get(facebookArtistName).toString();
			String category = musicInfo.get(facebookCategory).toString();
			System.out.println(String.format("Currently crawling artist [%s] with category [%s]", artist, category));
			HashMap<String, Object> artistInfo = new HashMap<String, Object>();
			artistInfo = getArtistInfo(artist, true);
			ArrayList<HashMap<String, Object>> similarArtistsInfo = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> topTracksInfo = new ArrayList<HashMap<String, Object>>();
			similarArtistsInfo = lastFm.getTopSimilarArtists(artist, artistInfo);
			topTracksInfo = lastFm.getTopTracks(similarArtistsInfo);
			json = new Gson().toJson(topTracksInfo);
			FileWriter(json, MetaDataFolder + "/" + artist + "_trackinfo.json");
		}
	}

	public void FileWriter(String content, String fileName) {
		try {
			File metadataDir = new File(MetaDataFolder);
			if (!metadataDir.exists()) {
				metadataDir.mkdir();
			}

			File file = new File(fileName);
			FileOutputStream oStream = new FileOutputStream(file);
			byte[] buffer = content.getBytes();
			oStream.write(buffer);
			oStream.close();
		} catch (Exception e) {
			System.out.println(String.format("Could not write to file [%]", fileName));
		}

	}

	public ArrayList<HashMap<String, Object>> getTopSimilarArtistWrapper(String response, String parent,
			HashMap<String, Object> selfArtist) {
		String artist = "";
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		result.add(selfArtist);
		JSONObject obj = new JSONObject(response);
		if (parent != "") {
			try {
				JSONArray arr = obj.getJSONObject(parent).getJSONArray(Constants.Artist);
				for (int i = 0; i < arr.length(); i++) {
					HashMap<String, Object> info = new HashMap<String, Object>();
					HashMap<String, Object> artistInfo = new HashMap<String, Object>();
					JSONObject jsonobject = arr.getJSONObject(i);
					artist = jsonobject.getString(Constants.Name);
					System.out.println("Found an artist [" + artist + "]");
					info.put(Constants.Artist, artist);
					if (jsonobject.has(Constants.RefrenceID)) {
						info.put(Constants.ArtistID, jsonobject.getString(Constants.RefrenceID));
					}
					info.put(Constants.Match, jsonobject.getString(Constants.Match));
					JSONArray levelII = jsonobject.getJSONArray(Constants.Image);
					for (int j = 0; j < levelII.length(); j++) {
						JSONObject objecTII = levelII.getJSONObject(j);
						{
							String size = objecTII.getString(Constants.Size);
							if (size.equals(ImageSize)) {
								info.put(Constants.ImageURL, objecTII.getString("#text"));
								break;
							}
						}
					}
					artistInfo = getArtistInfo(artist, false);
					info.put(Constants.Bio, artistInfo.get(Constants.Bio));
					info.put(Constants.Genre, artistInfo.get(Constants.Genre));
					result.add(info);

				}

			} catch (Exception e) {
				return null;
			}
		}
		return result;
	}

	public HashMap<String, Object> getTrackInfoWrapper(String response) {
		String temp = "";
		String genre = "";
		HashMap<String, Object> trackInfo = new HashMap<String, Object>();
		JSONObject obj = new JSONObject(response);
		obj = obj.getJSONObject(Constants.Track);
		trackInfo.put(Constants.Duration, obj.getString(Constants.Duration));
		if (obj.has(Constants.Wiki)) {
			trackInfo.put(Constants.Summary, obj.getJSONObject(Constants.Wiki).getString(Constants.Summary));
		} else {
			trackInfo.put(Constants.Summary, "N/A");
		}
		if (obj.has(Constants.Album)) {
			trackInfo.put(Constants.Album, obj.getJSONObject(Constants.Album).get(Constants.Title));
		}
		JSONArray levelII = obj.getJSONObject(Constants.Toptags).getJSONArray(Constants.Tag);
		for (int j = 0; j < levelII.length(); j++) {
			genre = "";
			JSONObject objecTII = levelII.getJSONObject(j);
			genre = objecTII.getString(Constants.Name);
			genre = genre + ",";
			temp = temp + genre;
			genre = temp;

		}
		if (!genre.isEmpty()) {
			trackInfo.put(Constants.Genre, genre.substring(0, genre.lastIndexOf(",")));
		} else {
			trackInfo.put(Constants.Genre, "N/A");
		}
		return trackInfo;
	}

	public HashMap<String, Object> getArtistInfoWrapper(String response, Boolean isSelf) {
		String temp = "";
		String genre = "";
		HashMap<String, Object> artistInfo = new HashMap<String, Object>();
		JSONObject obj = new JSONObject(response);
		obj = obj.getJSONObject(Constants.Artist);
		if (isSelf) {
			artistInfo.put(Constants.Artist, obj.get(Constants.Name));
			artistInfo.put(Constants.Match, "1");
			if (obj.has(Constants.RefrenceID)) {
				artistInfo.put(Constants.ArtistID, obj.get(Constants.RefrenceID));
			}
		}
		if (obj.has(Constants.Bio)) {
			artistInfo.put(Constants.Bio, obj.getJSONObject(Constants.Bio).get(Constants.Summary));
		}
		JSONArray levelII = obj.getJSONObject(Constants.Tags).getJSONArray(Constants.Tag);
		for (int j = 0; j < levelII.length(); j++) {
			genre = "";
			JSONObject objecTII = levelII.getJSONObject(j);
			genre = objecTII.getString(Constants.Name);
			genre = genre + ",";
			temp = temp + genre;
			genre = temp;
		}
		if (!genre.isEmpty()) {
			artistInfo.put(Constants.Genre, genre.substring(0, genre.lastIndexOf(",")));
		} else {
			artistInfo.put(Constants.Genre, "N/A");
		}
		return artistInfo;
	}

	public ArrayList<HashMap<String, Object>> getTopSimilarArtists(String artist, HashMap<String, Object> artistInfo) {
		try {

			ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
			String URL = String.format("%smethod=%s&%s=%s&api_key=%s&limit=%s&format=json", baseURL, similarArtistAPI,
					Constants.Artist, URLEncoder.encode(artist, "UTF-8"), appID, lastFMLimit);
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			String input = "";
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
			result = getTopSimilarArtistWrapper(response, "similarartists", artistInfo);
			return result;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return null;

		}
	}

	public HashMap<String, Object> getTrackInfo(String artist, String track) {
		try {
			HashMap<String, Object> trackInfo = new HashMap<String, Object>();
			String URL = String.format("%smethod=%s&%s=%s&api_key=%s&%s=%s&format=json", baseURL, trackAPI,
					Constants.Artist, URLEncoder.encode(artist, "UTF-8"), appID, Constants.Track,
					URLEncoder.encode(track, "UTF-8"));
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			String input = "";
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
			trackInfo = getTrackInfoWrapper(response);
			return trackInfo;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return null;
		}
	}

	public HashMap<String, Object> getArtistInfo(String artist, Boolean isSelf) {
		try {
			HashMap<String, Object> artistInfo = new HashMap<String, Object>();
			String URL = String.format("%smethod=%s&%s=%s&api_key=%s&format=json", baseURL, artistAPI, Constants.Artist,
					URLEncoder.encode(artist, "UTF-8"), appID);
			URL url = new URL(URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			String input = "";
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
			artistInfo = getArtistInfoWrapper(response, isSelf);
			return artistInfo;
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return null;
		}
	}

	public ArrayList<HashMap<String, Object>> getTopTracksWrapper(String response, String parent,
			ArrayList<HashMap<String, Object>> result) {
		HashMap<String, Object> trackInfo = new HashMap<String, Object>();
		String track = "";
		String artist = "";
		JSONObject obj = new JSONObject(response);
		if (parent != "") {
			JSONArray arr = obj.getJSONObject(parent).getJSONArray(Constants.Track);
			for (int i = 0; i < arr.length(); i++) {
				try {
					HashMap<String, Object> info = new HashMap<String, Object>();
					JSONObject jsonobject = arr.getJSONObject(i);
					track = jsonobject.getString(Constants.Name);
					artist = jsonobject.getJSONObject(Constants.Artist).getString(Constants.Name);
					System.out.println(String.format("Fetching information for [%s]", track));
					info.put(Constants.Track, jsonobject.getString(Constants.Name));
					info.put(Constants.PlayCount, jsonobject.getString(Constants.PlayCount));
					info.put(Constants.Listeners, jsonobject.getString(Constants.Listeners));
					if (jsonobject.has(Constants.RefrenceID)) {
						info.put(Constants.TrackID, jsonobject.getString(Constants.RefrenceID));
					}
					info.put(Constants.Artist, artist);

					JSONObject jobj = jsonobject.getJSONObject(Constants.Artist);
					if (jobj.has(Constants.RefrenceID)) {
						info.put(Constants.ArtistID,
								jsonobject.getJSONObject(Constants.Artist).getString(Constants.RefrenceID));
					}
					JSONArray levelII = jsonobject.getJSONArray(Constants.Image);
					for (int j = 0; j < levelII.length(); j++) {
						JSONObject objecTII = levelII.getJSONObject(j);
						{
							String size = objecTII.getString(Constants.Size);
							if (size.equals(ImageSize)) {
								info.put(Constants.ImageURL, objecTII.getString("#text"));
								break;
							}
						}
					}
					trackInfo = getTrackInfo(artist, track);
					info.put(Constants.Duration, trackInfo.get(Constants.Duration));
					info.put(Constants.Genre, trackInfo.get(Constants.Genre));
					info.put(Constants.Summary, trackInfo.get(Constants.Summary));
					info.put(Constants.Album, trackInfo.get(Constants.Album));
					result.add(info);
				} catch (Exception e) {
					System.out.println("Exception while fetching [" + track + "]");
				}
			}
		}
		return result;
	}

	public ArrayList<HashMap<String, Object>> getTopTracks(ArrayList<HashMap<String, Object>> artistInfo) {
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		try {
			for (HashMap<String, Object> artists : artistInfo) {
				String artist = artists.get(Constants.Artist).toString();
				String URL = String.format("%smethod=%s&%s=%s&api_key=%s&limit=%s&format=json", baseURL, topTracksAPI,
						Constants.Artist, URLEncoder.encode(artist, "UTF-8"), appID, lastFMLimit);
				System.out.println(String.format("Fetching top tracks for [%s]", artist));
				URL url = new URL(URL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				String input = "";
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
				// System.out.println(response);
				result = getTopTracksWrapper(response, "toptracks", result);

			}
		} catch (Exception e) {
			System.out.println("Exception " + e.getMessage());
			return null;

		}
		return result;
	}
}
