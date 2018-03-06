package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.Hackathon.TheMuse.*;

public class TheMuseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TheMuseServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String artist = request.getParameter("artistName");
		String type = request.getParameter("type");
		System.out.println(artist);
		new ArrayList<HashMap<String, Object>>();
		String json = "";
		SpotifyHelper spotify = new SpotifyHelper();
		PrintWriter out = response.getWriter();

		if (type != null) {
			if (type.equalsIgnoreCase("singer")) {
				try {
					if (artist != null && artist != "") {
						json = spotify.discoverArtist(artist);
						out.print(json);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (type.equalsIgnoreCase("facebook")) {
				try {
					out.print(spotify.getFaceBookData());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (type.equalsIgnoreCase("playListUrl") && (artist!=null && artist!="")) {
				
				out.print(spotify.createPlaylistWrapper(artist));
			}

		} else {

		}

	}

}
