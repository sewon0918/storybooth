/*
    Project 1
    Name of Project: Storybooth
    Author: Sewon Lim
    Date: 2020 May
*/


import processing.video.*;
import websockets.*;
import java.util.Iterator;

WebsocketServer ws;
String path="";
PImage img;
boolean upload;
String currentUpload;
int n=0;
ArrayList<String> filename;
String filenameString;
Movie myMovie;

int padding = 80;
int buttonsize = 50;
int videoScale = 1;
int cols, rows;
Capture video;
TextButton aa;
CameraButton cb;
DeleteButton db;
ArrayList<Text> textArr;
Text text;
PImage myImage;

String[] fontList;
private ArrayList<EachFont> fonts;
FontSelection fontselection;



void setup() {  
    size(960, 720);
    fontList = new String[]
    {"Andale Mono.ttf", "Arial Bold.ttf", "BlackoakStd.otf", "Bodoni 72 Smallcaps Book.ttf", "Chalkduster.ttf", 
    "CharlemagneStd-Bold.otf", "Comic Sans MS Bold.ttf", "Courier New Bold.ttf", "Courier New.ttf", "HoboStd.otf", 
    "LetterGothicStd-Bold.otf", "LithosPro-Black.otf", "LithosPro-Regular.otf", "MinionPro-Bold.otf", "MyriadArabic-Bold.otf",
    "OCRAStd.otf", "OratorStd.otf", "PoplarStd.otf", "PrestigeEliteStd-Bd.otf", "TektonPro-Bold.otf"};
    
    ws = new WebsocketServer(this, 8025, "/storybooth");
	filename = new ArrayList<String>();

    int a = width - buttonsize/2;
    int b = buttonsize;
    aa = new TextButton(width - buttonsize/2, 3*buttonsize/4, buttonsize);
    cb = new CameraButton(width-buttonsize/2, height-2*buttonsize/3, buttonsize);
    db = new DeleteButton(width/2, height-2*buttonsize/3, buttonsize);

    textArr = new ArrayList<Text>(); 
    fonts = new ArrayList<EachFont>();
    fontselection = new FontSelection(width - 5*buttonsize/2, 2*buttonsize, 5*buttonsize/2, 4*buttonsize/2, buttonsize/2);
}

void webSocketServerEvent(String msg) 
{
    println(msg);
    if (!msg.equals("received")){
		if (msg.charAt(0)=='m'){
			path = "/Users/user/Downloads/" + msg ;
		}
	}
	else{
		upload=false;
	}
    
}

void draw() {
    
    if (upload){
        try {
            ws.sendMessage(filenameString);
        } catch (Exception e) {
            
        }
        path="";
	}
	if (path == ""){
		img = null;
	}else{
		img = loadImage(path);
	}
	if (img == null){
		fill(200);
		rectMode(CORNER);
		rect(0,0,width,height);
	}
    else {
        image(img, width/2, height/2, 960, 720);
       
        cb.draw();
        aa.draw();
        db.draw();
        
        for(Text text: textArr){
            text.draw();
        }
         if (cb.isInvisible() && db.isInvisible() && aa.isInvisible()){
            cb.draw();
            save("../Javascript_Code/assets/" + currentUpload);
            cb.visible();
            db.visible();
            aa.visible();
            textArr.clear();
            text = null ; 
            File[] files = listFiles("../Javascript_Code/assets/");
            
            filename.clear();
            filenameString = "";
            for (int i = 0; i < files.length; i++) {
                File f = files[i];    
                filename.add(f.getName());
                filenameString = f.getName() + filenameString;
                if (i!=files.length-1){
                    filenameString = "," + filenameString;
                }
            }
            print(filename);
            upload=true;
            
            n++;
        }else{
            if (text!=null){
                text.sizeSlider().draw();
                text.rotationSlider().draw();
                text.fontButton().draw();
                text.colorButton().draw();
                if (text.fontButton().isOnState()){
                    fontselection.draw();
                }   
                if (text.colorButton().isOnState()){
                    text.colorselection().draw();
                }
            }
        }
    }
}


void mousePressed()
{
    if (cb.isOver()) {
        print("presssed");
		currentUpload = str(n) + ".png";
		cb.invisible();
        db.invisible();
        aa.invisible();
        
    }
    
    if (aa.isOver()) {
        if (text!=null){
            if (text.colorButton().isOnState()) text.colorButton().notOnState();
            if (text.fontButton().isOnState()) text.fontButton().notOnState();
        }
        
        aa.textOnState();
    
        for (Text text:textArr){
            text.setDone();
        }
    
        Text newText = new Text("", width/2, height/2, 30, color(0)); 
        if (textArr.size()>0 && textArr.get(textArr.size()-1).length()==0){
            textArr.remove(textArr.size()-1);
        }
        textArr.add(newText);
        print(textArr.size());
        text = newText;
        
    }
    if (text!=null){
        if (text.fontButton().isOver()) {
            if (text.colorButton().isOnState()) text.colorButton().notOnState();
            text.setDone();
            if (!text.fontButton().isOnState()) text.fontButton().onState(); // 닫혀있으면 열고 열려있으면 닫기
            else text.fontButton().notOnState();
        }
        if (text.colorButton().isOver()) {
            text.setDone();
            if (text.fontButton().isOnState()) text.fontButton().notOnState();
            if (!text.colorButton().isOnState()) text.colorButton().onState(); // 닫혀있으면 열고 열려있으면 닫기
            else text.colorButton().notOnState();
        }
        for (int i=0; i<fonts.size();i++){
            if(text.fontButton().isOnState() && fonts.get(i).isOver()){
                text.setFont(fonts.get(i).getFont());
            }   
        }
        for (int i=0; i<textArr.size();i++){
            if(textArr.get(i).isOver() && !text.colorselection().isOver() && !text.colorButton().isOver() && !text.fontButton().isOver()){
                text = textArr.get(i);
                for (Text exisitngtext:textArr){
                    if (exisitngtext != text){
                        exisitngtext.setDone();
                    }
                }
            }   
        }
    }   
}

void mouseClicked() {
  int count = mouseEvent.getClickCount();
//   println(count);  // count is 2 for double click
  if (count==2){
      for (int i=0; i<textArr.size();i++){
        if(textArr.get(i).isOver()){
            text = textArr.get(i);
            text.keepWriting();
            for (Text exisitingtext:textArr){
                if (exisitingtext != text){
                    exisitingtext.setDone();
                }
            }
        }   
    }
  }
}

void keyPressed()
{
    if (aa != null && aa.onState){
        
        if (key == ENTER) {  
            text.setDone();  
            if (text.length()==0){
                int index = getIndex(text, textArr);
                textArr.remove(index);
            }  
            return;
        }
        if (key == BACKSPACE) {
            if (text.length() > 0) {
                text.setContent(text.getContent().substring(0, text.length() - 1));
            }
            return;
        }
        if (text!=null && text.length()==0){
            text.setContent(Character.toString(key));
        }else{
            if (!text.ifDone()) text.setContent(text.getContent()+key); 
        }
    }
}

int getIndex(Text text, ArrayList<Text> textArr)
{
    int index = 0;
    for (int i=0; i<textArr.size(); i++){
        if(text==textArr.get(i)){
            index = i;
            break;
        }
    }
    return index;
}

public void mouseDragged() 
{
    if (text != null){
        if (!text.sliderOn && !text.sliderOn2 && !text.sliderOn3[0] && !text.sliderOn3[1] && !text.sliderOn3[2]){
            if (text.ifDone() && text.isOver() && !text.colorselection.isOver() && !fontselection.isOver()){
                if (text.colorButton().isOnState()) text.colorButton().notOnState();
                if (text.fontButton().isOnState()) text.fontButton().notOnState();
                if (text.firstXposition==-1 && text.firstYposition==-1){
                    text.firstXposition = mouseX;
                    text.firstYposition = mouseY;
                    text.textX = text.x;
                    text.textY = text.y;
                }
            } 
            if (text.firstXposition!=-1 && text.firstYposition!=-1 && mouseX>0 && mouseX<width && mouseY>0 && mouseY<height){
                float diffX = text.firstXposition-text.textX;
                float diffY = text.firstYposition-text.textY;
                text.sliderOn = false;
                text.sliderOn2 = false;
                text.changePosition(mouseX-diffX, mouseY-diffY);
            }
        }
        
        if (text.sizeSlider().isOver())
        {
            text.colorButton().notOnState();
            text.fontButton().notOnState();
            text.sliderOn = true;
            text.originalposition = text.sizeSlider().circle;
        }
        if (text.sliderOn && mouseY >= text.sizeSlider().y_bar-text.sizeSlider().h/2 +text.sizeSlider().r/2 && mouseY<text.sizeSlider().startingPosition)
        {
            text.sizeSlider().moveSlider(mouseY);
            int updatedSize = 20 + int((text.sizeSlider().startingPosition-mouseY)/(text.sizeSlider().h - text.sizeSlider().r) * 130);
            text.setSize(updatedSize);
        }

        if (text.ifDone() && text.rotationSlider().isOver())
        {
            text.colorButton().notOnState();
            text.fontButton().notOnState();
            text.sliderOn2 = true;
            text.originalposition2 = text.rotationSlider().circle;
        }
        if (text.ifDone() && text.sliderOn2 && mouseY >= text.rotationSlider().y_bar-text.rotationSlider().h/2+text.rotationSlider().r/2 && mouseY <= text.rotationSlider().startingPosition)
        {
            text.rotationSlider().moveSlider(mouseY);
            float updatedRotation = radians((text.rotationSlider().startingPosition-mouseY)/(text.rotationSlider().h - text.rotationSlider().r) * (360));
            text.setRotation(updatedRotation);
        }
        if (text.colorButton().isOnState()){
            if (text.colorselection().rSlider().isOver())
            {
                text.firstXposition = -1;
                text.firstYposition = -1;
                text.sliderOn3[0] = true;
                text.originalposition3[0] = text.colorselection().rSlider().circle;
            }
            if (text.sliderOn3[0] && mouseX <= text.colorselection().rSlider().x + text.colorselection().rSlider().w/2 - text.colorselection().rSlider().r/2 && mouseX >= text.colorselection().rSlider().startingPosition)
            {
                text.firstXposition = -1;
                text.firstYposition = -1;
                text.colorselection().rSlider().moveSlider(mouseX);
                int updatedRed = int((mouseX - text.colorselection().rSlider().startingPosition)/(text.colorselection().rSlider().w - text.colorselection().rSlider().r) * (255));
                text.setR(updatedRed);
                text.colorButton().setR(updatedRed);
            }
            if (text.colorselection().gSlider().isOver())
            {
                text.firstXposition = -1;
                text.firstYposition = -1;
                text.sliderOn3[1] = true;
                text.originalposition3[1] = text.colorselection().gSlider().circle;
            }
            if (text.sliderOn3[1] && mouseX <= text.colorselection().gSlider().x + text.colorselection().gSlider().w/2 - text.colorselection().gSlider().r/2 && mouseX >= text.colorselection().gSlider().startingPosition)
            {
                text.firstXposition = -1;
                text.firstYposition = -1;
                text.colorselection().gSlider().moveSlider(mouseX);
                int updatedGreen = int((mouseX - text.colorselection().gSlider().startingPosition)/(text.colorselection().gSlider().w - text.colorselection().gSlider().r) * (255));
                text.setG(updatedGreen);
                text.colorButton().setG(updatedGreen);
            }
            if (text.colorselection().bSlider().isOver())
            {
                text.firstXposition = -1;
                text.firstYposition = -1;
                text.sliderOn3[2] = true;
                text.originalposition3[2] = text.colorselection().bSlider().circle;
            }
            if (text.sliderOn3[2] && mouseX <= text.colorselection().bSlider().x + text.colorselection().bSlider().w/2 - text.colorselection().bSlider().r/2 && mouseX >= text.colorselection().bSlider().startingPosition)
            {
                text.firstXposition = -1;
                text.firstYposition = -1;
                text.colorselection().bSlider().moveSlider(mouseX);
                int updatedBlue = int((mouseX - text.colorselection().bSlider().startingPosition)/(text.colorselection().bSlider().w - text.colorselection().bSlider().r) * (255));
                text.setB(updatedBlue);
                text.colorButton().setB(updatedBlue);
            }
        }
        
    }
}
public void mouseReleased() {
    if (db!=null && db.isOver() && text!=null && text.firstXposition!=-1 && text.firstYposition!=-1){
        for (int i=0;i<textArr.size();i++){
            if (text == textArr.get(i)){
                textArr.remove(i);
            }
        }
    }
    if (text != null){
        text.firstXposition = -1;
        text.firstYposition = -1;
        text.textX = -1;
        text.textY = -1;
        text.sliderOn = false;
        text.sliderOn2 = false;
        text.sliderOn3[0] = false;
        text.sliderOn3[1] = false;
        text.sliderOn3[2] = false;
    }
}


// your code down here
// feel free to crate other .pde files to organize your code
