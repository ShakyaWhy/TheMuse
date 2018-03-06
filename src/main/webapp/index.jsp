<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<title>The Muse</title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>

<link rel="stylesheet" type="text/css" href="css/freelancer.css"/>
<link rel="stylesheet" type="text/css" href="css/style.css"/>
</head>
<body>
<script src="js/jquery-3.2.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<script src="js/myScript.js"></script>

<!-- <div id="iframeTag" class="hidden container-fluid" style="
    height: 964px;
    width: 100%;
">
<iframe style="
    height: 100%;
    width: 100%;
"></iframe> -->
</div>
<div class="container-fluid" id="mainWrapper">
<div id="searchDiv" >
	<nav id="mainNav" class="navbar navbar-default navbar-fixed-top navbar-custom affix-top">
                <div class="container">
                                <!-- Brand and toggle get grouped for better mobile display -->
                                <div class="navbar-header page-scroll">
                                                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                                                                <span class="sr-only">Toggle navigation</span> Menu <i class="fa fa-bars"></i>
                                                </button>
                                                <img src="img/muse-logo.png"></img>
                                </div>
                                <!-- Collect the nav links, forms, and other content for toggling -->
                                <div class="navbar-collapse collapse" id="bs-example-navbar-collapse-1" aria-expanded="false" style="height: 1px;">
                                                <ul class="nav navbar-nav navbar-right" style="margin-top: 8px;">
                                                                <li style="margin-right: 10px;width: 500px;font-weight: normal;" class="">
                                                                                <input id="searchInput" class="form-control mr-sm-2" type="text" placeholder="Search with Artists">
                                                                </li>
                                                                <li> <abbr>or</abbr> </li>
                                                                <li class="active">
                                                                   <input id="playlistBtn" type="button" value="My Artists"></input>
                                                                </li>
                                                </ul>
                                </div>
                                <!-- /.navbar-collapse -->
                </div>
                <!-- /.container-fluid -->
</nav>
	
	</div>
	<div id="loader" class="center_it heightStyle hidden" style="position: relative;margin-top: 100%;text-align: center;"><div style="color:white;margin-bottom: 15px;">Analyzing Similar Artists..</div>
			<img width="20%" src="img/loader.gif">
	</div> 
	<div id="imageWrapper"  style="height: 600px;
    margin-top: 228px;
    width: 800px;background-image: url('img/music.png');"></div>
    <div id="artistList" class="hidden" style="
    height: 800px;
    margin-top: 140px;
    width: 1500px;
" class="row">	
<div class="scrollbar" id="style-4">
    <ul>
       
        </ul>
        </div>
    </div>
	<div id="songList" class="row col-md-12 hidden">
	<div id="albumWrapper" class="col-lg-4">
	<div id="albumImg"></div>
	<div id="playListName" style="
    margin-top: 4%;
    margin-left: 1%;
"></div>
	<a href="#" class="btn btn-success" style="margin-left: 40%; margin-top: 5%;">
      <span class="glyphicon glyphicon-triangle-right"></span> Play
</a> 
	<!-- <input class="margin-top-20" id="playBtn" type="button" value="Play"> -->
	
	</div>
	<div id="trackList">
	<div class="col-lg-8"><div class="scrollbar" id="style-4">
      <div class="force-overflow">
	
	<ul>
	
	
	</ul>
	</div>
	</div>
	</div>
	</div>
	
</div>
</div>
</body>
</html>