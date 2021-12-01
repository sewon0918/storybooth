/*
	Project 2
	Name of Project: Storybooth 
	Author: Sewon Lim
	Date: 2020 June
*/

let camera;
let cnv;
let decorated;
let story;
let stories;
let base64;
let cnt = 0;
var video;
var videonow;
var videoList;
var audioFile;
let takingPicture = false;
let n = 0;
let grid = 1;
let gray = false;
const ws = new WebSocket("ws://localhost:8025/storybooth");

function setup() {
    cnv = createCanvas(960, 720); //canvas
    background(51);

    video = createCapture(VIDEO); //camera 
    video.size(960, 720);
    // make a list of video to make it possible to store multiple video
    videoList = [video];
    videoList.forEach(function(e) {
        e.hide();
    })
    $('#file_route').hide();
    // camera sound
    audioFile = new Audio('data/photo_sound.mp3');

}

function draw() { //canvas

    if (story != null && n == 0) {
        // print(story);
        stories = split(story, ",");
        print(stories);
        ws.send("received");

        stories.forEach(function(e) {
            var photoFrame = document.createElement("img");
            photoFrame.src = "assets/" + e;
            photoFrame.className = "photoFrame";
            photoFrame.setAttribute("onclick", "bigImg(this, 'bigimg')");
            document.getElementById("story").appendChild(photoFrame);
        })
        n++;
    }

    if (document.getElementById("file_route").value == "") {
        takePhoto();
    } else {
        selectFromGallery();
    }

    // hide file_route
    $('#file_route').hide();

}



function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        document.getElementById("file_route").value = input.value;
        reader.onload = function(e) {
            base64 = e.target.result;
            $('#selectedImage')
                .attr('src', e.target.result)
                .width(960)
                .height(720);
        };

        reader.readAsDataURL(input.files[0]);
    }
}



function deleteImage() {
    document.getElementById("file_route").value = "";
    $('#selectedImage')
        .attr('src', "");
}

function selectFromGallery() {
    $('#gray').hide();
    $('#camera').hide();
    $('#selectedImage').show();
    $('#delete').show();
    cnv.hide();
}

function takePhoto() {
    // cnv = createCanvas(960, 720);
    cnv.show();
    $('#gray').show();
    var story = document.getElementById('story');
    story.style.position = "absolute";
    story.style.top = "50px";
    story.style.left = "1000px";
    story.style.width = "200px";
    story.style.height = "500px";
    story.style.borderColor = color(0);
    story.style.borderWidth = "5px";
    story.style.borderBottom = "3px";
    story.style.overflow = "scroll";
    $('#selectedImage').hide();
    $('#delete').hide();
    translate(width, 0); // move to far corner
    scale(-1.0, 1.0); // flip x-axis backwards
    if (videoList.length != grid * grid) {
        videoList = [];
        for (let m = 0; m < grid * grid; m++) {
            var newVideo = createCapture(VIDEO);
            // newVideo.size(960, 720);
            videoList.push(newVideo);
            newVideo.hide();
        }
    }

    for (let i = 0; i < grid; i++) {
        for (let j = 0; j < grid; j++) {
            let a = grid - 1 - i;
            image(videoList[grid * i + j], width / grid * a, height / grid * j, width / grid, height / grid);
            if (gray) {
                filter(GRAY);

            }
        }
    }
}

// gray filter
function changeFilter() {
    if (gray) {
        var string = document.getElementById("gray").innerHTML;
        var replacedString = string.replace("NORMAL", "GRAY");
        document.getElementById("gray").innerHTML = replacedString;

    } else {
        var string = document.getElementById("gray").innerHTML;
        var replacedString = string.replace("GRAY", "NORMAL");
        document.getElementById("gray").innerHTML = replacedString;
    }
    gray = !gray;
}

function photo() {
    print(cnt);
    if (cnt % 2 == 0) {
        timerphoto(0);

    } else {
        videoList = [];
        for (let m = 0; m < grid * grid; m++) {
            var newVideo = createCapture(VIDEO);
            newVideo.size(960, 720);
            videoList.push(newVideo);
            newVideo.hide();
        }
    }
    cnt++;
}

function keyTyped() {
    if (key === 's') {
        let photo = "myPhoto" + String(Math.random()) + ".jpg"
        if (document.getElementById("file_route").value != "") {
            print("BASE^$: ", base64);
            var a = document.createElement('a');
            a.style = 'display: none';
            a.href = base64;
            a.download = photo;

            a.click();
            ws.send(photo);
        } else {
            print(photo);
            if (cnt % 2 == 0) {
                alert('take a photo or select from gallery')
            } else {
                save(photo);
                ws.send(photo);
            }
        }
    }
}

function sendToServer() {
    let photo = "myPhoto" + String(Math.random()) + ".jpg"
    if (document.getElementById("file_route").value != "") {
        print("BASE^$: ", base64);
        var a = document.createElement('a');
        a.style = 'display: none';
        a.href = base64;
        a.download = photo;

        a.click();
        ws.send(photo);
    } else {
        print(photo);
        if (cnt % 2 == 0) {
            alert('take a photo or select from gallery')
        } else {
            save(photo);
            ws.send(photo);
        }
    }
}

// This function self call itself every REFRESH_RATE
// take multiple photos
function timer(n, photo) {
    print(n);
    videoList[n].stop();
    n++;
    // Keep calling myself
    let timerId = setTimeout(timer, 1000, n, photo);
    if (n == grid * grid) {
        clearTimeout(timerId);
        save(photo);
        ws.send(photo);
    }
}

function timerphoto(n) {
    print(n, "th picture");
    $('#camera').hide();
    audioFile.play();
    videoList[n].stop();
    n++;
    // Keep calling myself
    let timerId = setTimeout(timerphoto, 1000, n);
    if (n == grid * grid) {
        $('#camera').show();
        clearTimeout(timerId);

    }
}

function mousePressed() {
    if (mouseX > width - 50 && mouseX < width && mouseY > height / 3 && mouseY < 2 * height / 3) {
        print("a");
        cnt = 0;
        grid++;
    }
    if (mouseX > 0 && mouseX < 50 && mouseY > height / 3 && mouseY < 2 * height / 3) {
        print("b");
        cnt = 0;
        if (grid >= 2) grid--;
    }
}

// when clicking a story
function bigImg(a, b) {
    print("clickss");
    a.style.borderColor = color(200, 200, 200);
    document.getElementById(b).getElementsByTagName("img")[0].src = a.src;
    document.getElementById(b).style.display = 'block';
}

function hideBigImg(image) {
    document.getElementById(image).style.display = 'none';
}



// called when loading the page
$(function() {

    ws.onopen = function() {
        // Web Socket is connected, send data using send()
        console.log("Ready...");
    };

    ws.onmessage = function(evt) {

        var received_msg = evt.data;
        console.log("Message is received..." + received_msg);
        story = received_msg;


    };

    ws.onclose = function() {
        // websocket is closed.
        console.log("Connection is closed...");
    };
});