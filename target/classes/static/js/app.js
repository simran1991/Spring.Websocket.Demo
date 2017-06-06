/**
 * 
 * 
 */

var width = 60;    
var height = 60;     
var streaming = false;
var video = null;
var canvas = null;
var photo = null;
var startbutton = null;
var photoData=null;

var stompClient = null;
var trackClient = null;
var author = null;
var lastUserName = null;
var currentUser = null;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
		$('#messagePane').show();
		$('#registerationPane').hide();
		var name = $('#name').val();
		$('#regName').html(name);
		author = name;
		$("#buttons").show();
	} else {
		$('#registerationPane').show();
		$('#messagePane').hide();
		$("#conversation").hide();
		$("#buttons").hide();

	}
	$("#videoStream").hide();
	$("#conversation").html("");
	$("#typing").html("");
}

function connect() {

	var socket = new SockJS('/chat');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);

		stompClient.subscribe('/topic/chat', function(message) {
			showMessage(JSON.parse(message.body));
			
		});

		stompClient.subscribe('/topic/tracking', function(message) {
			showTrack(JSON.parse(message.body));
		});

		$('#message').focusin(function() {
			track();
		})

	});

}

function disconnect() {
	if (stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
	stopWebCam();	
	photoData=null;
	photo = null;
	console.log("Disconnected");
}

function sendMessage() {
	stompClient.send("/app/chat", {}, JSON.stringify({
		'sender' : author,
		'content' : $('#message').val(),
		'photoData':photoData
	}));
	
	if(photoData){
		stopWebCam();	
		photoData=null;
		photo = null;
	}
}

function track() {
	stompClient.send("/app/track", {}, JSON.stringify({
		'user' : author
	}));
}

function showMessage(message) {
	console.log("recieved" + message);
	if (lastUserName != message.sender) {
		lastUserName = message.sender;
		$("#conversation")
				.append(
						"<h2>"
								+ message.sender
								+ "</h2><br><div class='row'><div class='message col-sm-4'>"
								+ message.content
								+ "<canvas id='messagePhoto_"+message.timestamp+"'></canvas></div><div class='col-sm-4'>"
								+ message.timestamp + "</div></div>");
	} else {
		$("#conversation").append(
				"<br><div class='row'><div class='message col-sm-4'>"
				+ message.content
				+ "<canvas id='messagePhoto_"+message.timestamp+"'></canvas></div><div class='col-sm-4'>"
				+ message.timestamp + "</div></div>");
	}
	if(message.photoData){
		loadCanvas(message.photoData,message.timestamp);
	}
	
}


function showTrack(message) {
	console.log("got tracking message" + message.user);
	$("#typing").html(message.user + ",");
}

function takepicture() {
	startup();
}

function stopWebCam(){
	navigator.mediaDevices.getUserMedia({ video: true, audio: false })
    .then(function(stream) {
        video.srcObject = null;
        stream.getVideoTracks().forEach(function(element){
        	element.stop();
        });
    })
    .catch(function(err) {
        console.log("An error occured! " + err);
    });
	
	$("#videoStream").hide();
}

function startup() {
	video = document.getElementById('video');
    canvas = document.getElementById('canvas');
    photo = document.getElementById('photo');
    startbutton = document.getElementById('startbutton');

    navigator.mediaDevices.getUserMedia({ video: true, audio: false })
    .then(function(stream) {
        video.srcObject = stream;
        video.play();
    })
    .catch(function(err) {
        console.log("An error occured during webcam init " + err);
    });

    video.addEventListener('canplay', function(ev){
        if (!streaming) {
          height = video.videoHeight / (video.videoWidth/width);
        
          video.setAttribute('width', width);
          video.setAttribute('height', height);
          canvas.setAttribute('width', width);
          canvas.setAttribute('height', height);
          streaming = true;
        }
      }, false);
    
    startbutton.addEventListener('click', function(ev){
        takepicture();
        ev.preventDefault();
      }, false);
		startbutton.addEventListener('click', function(ev) {
		takepicture();
		ev.preventDefault();
	}, false);

	clearphoto();
}


function clearphoto() {
    var context = canvas.getContext('2d');
    context.fillStyle = "#FFF";
    context.fillRect(0, 0, canvas.width, canvas.height);
    var data = canvas.toDataURL('image/png');
    photo.setAttribute('src', data);
	    
}


function takepicture() {
 	var context = canvas.getContext('2d');
    if (width && height) {
      canvas.width = width;
      canvas.height = height;
      context.drawImage(video, 0, 0, width, height);
      var data = canvas.toDataURL('image/png');
      photo.setAttribute('src', data);
      photoData=data;
    } else {
      clearphoto();
    }
}

function takePhoto(){
	$("#videoStream").show();
	startup();
	//console.log("startinf");
}

function loadCanvas(dataURL,id) {
	var canvasId="messagePhoto_"+id;
	//console.log(canvasId);
	var canvas = document.getElementById(canvasId);
    var context = canvas.getContext('2d');
    var imageObj = new Image();
    imageObj.src = dataURL;
    imageObj.onload = function() {
        context.drawImage(this, 0, 0);
      };
}


$(function() {
	
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	
	$("#connect").click(function() {
		connect();
	});
	
	$("#disconnect").click(function() {
		disconnect();
	});
	
	$("#send").click(function() {
		sendMessage();
	});
	
	$("#takePic").click(function() {
		takePhoto();
	});

	$('#messagePane').hide();
	
	$('#conversation').hide();

	$("#videoStream").hide();
	
	$("#buttons").hide();
});