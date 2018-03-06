$(document)
		.ready(
				function() {
					var artistName;
					var url;
					//var wrapper = $("#artistList");
					$("#artistList").off("click").on("click", 'li',function() {
						var band = $("#bandName",this).text().trim();
						var e = jQuery.Event("keypress");
						e.which = 13; // # Some key code value
						
						$('#searchInput').val(band).trigger(e);
					});
					
					$("#playlistBtn").off("click").on("click", function() {
						var $loader = $("#loader");
						var $songList = $("#songList");
						var $imgWrapper = $("#imageWrapper");
						var $artistDiv = $("#artistList");
						$artistDiv.addClass("hidden");
						$imgWrapper.addClass("hidden");
						$songList.addClass("hidden");
						
						$loader.removeClass("hidden");
						$.get("TheMuseServlet", {
							type : "facebook"
						}, function(data) {
							data = $
							.parseJSON(data);
							var $artistList = $("#artistList ul");
							if($artistList.children().length > 0)
								 $artistList.empty();
							
							$.map(data,function(bdata,i) {
								//var $bandName = bdata.Band_Name;
								
								$li = $('<li class="listOfSongs"><div class="row" style="cursor: pointer;"><div id="bandName" class="col-md-4">'+bdata.Band_Name+'</div><div class="col-md-4">'+bdata.Band_Category+'</div><div class="col-md-4 ">'+
										bdata.Band_Added_At+'</div></div></li>');
								$li
								.appendTo($artistList);
							});
							$artistDiv.removeClass("hidden");
							$loader.addClass("hidden");
						});
					});
					$("#albumWrapper a")
							.off("click")
							.on(
									"click",
									function(event) {
										
										event.preventDefault();
										window.open(url, '_blank');
										/*
										 * $("#iframeTag iframe").attr("src", );
										 * $("#iframeTag").removeClass("hidden");
										 * $("#mainWrapper").addClass("hidden");
										 */
									});

					$('#searchInput')
							.keypress(
									function(e) {
										if (e.which == 13) {
											var $loader = $("#loader");
											var $songList = $("#songList");
											var $imgWrapper = $("#imageWrapper");
											$imgWrapper.addClass("hidden");
											$songList.addClass("hidden");
											var $artistDiv = $("#artistList");
											$artistDiv.addClass("hidden");
											/*$("body").css("background-clolor",
													"white");*/
											$loader.removeClass("hidden");
											artistName = $('#searchInput')
													.val().trim();
											$
													.get(
															"TheMuseServlet",
															{
																artistName : artistName,
																type : "singer"
															},
															function(data) {
																$.get("TheMuseServlet", {
																	artistName : artistName,
																	type : "playListUrl"
																}, function(data1) {

																url = data1;
																
																

																$loader
																		.addClass("hidden");
																$songList
																		.removeClass("hidden");
																var songsInfo = $
																		.parseJSON(data);
																var artist = songsInfo[0].artist;
																$(
																		'#playListName')
																		.text(
																				artist);
																var imageUrl = songsInfo[0].ImageURL;
																$("#albumImg")
																		.css(
																				'background-image',
																				'url('
																						+ imageUrl
																						+ ')');
																var $trackList = $("#trackList ul");
																if($trackList.children().length > 0)
																	$trackList.empty();
																$
																		.map(
																				songsInfo,
																				function(
																						song,
																						indx) {
																					var songTrack = song.track;
																					var album = (typeof song.album === 'undefined' ? 'Not Available'
																							: song.album)
																							+ " ";

																					

																					var artist = " "
																							+ (song.artist === 'undefined' ? 'N\A'
																									: song.artist);
																					var listeners = song.listeners === 'undefined' ? 'N\A'
																							: (song.listeners / 1000).toFixed(2);
																							
																					+" ";
																					var playCount = " "
																							+ song.playcount === 'undefined' ? 'N\A'
																									: (song.playcount / 1000000).toFixed(2);
																					var duration = (song.duration === 'undefined' ? 'N\A'
																							: Math
																									.round(song.duration / 60000));
																					var image = (song.ImageURL === 'undefined' ? 'N\A'
																							: song.ImageURL);
																					var summary = song.summary;
																					var $li = $('<li class="tracklist-row"><div class="row"><div class="noCount col-md-1">'
																							+ (indx + 1)
																							+ '.</div><div class="col-md-1"><div id="imgIcon" class="songIcon"></div></div><div class="col-md-10"><div class="row"><div id="song" style="cursor:default" class="songName col-md-7">'
																							+ songTrack
																							+ '</div><div style="font-size:15px;" class="col-md-5 songDetails">'
																							+ duration
																							+ " min"
																							+ '</div></div><div class="row"><div class="col-md-7 albumName">'
																							+ artist
																							+ ' . '
																							+ album
																							+ '</div><div class="col-md-5 songDetails style">'
																							+ playCount
																							+ 'M Hits'
																							+" | "
																							+ listeners
																							+ "K listeners"
																							+ '</div></div></div></div></li>');
																					$(
																							"#imgIcon",
																							$li)
																							.css(
																									"background-image",
																									'url('
																											+ image
																											+ ')');
																					$("#song",$li).attr('title',summary);
																					$li
																							.appendTo($trackList);

																				});
															});
															});
											return false;
										}
									});

				});
