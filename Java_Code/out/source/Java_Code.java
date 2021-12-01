import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.video.*; 
import websockets.*; 
import java.util.Iterator; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Java_Code extends PApplet {

/*
    Project 1
    Name of Project: Storybooth
    Author: Sewon Lim
    Date: 2020 May
*/






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



public void setup() {  
    
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

public void webSocketServerEvent(String msg) 
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

public void draw() {
    
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


public void mousePressed()
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

public void mouseClicked() {
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

public void keyPressed()
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

public int getIndex(Text text, ArrayList<Text> textArr)
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
            int updatedSize = 20 + PApplet.parseInt((text.sizeSlider().startingPosition-mouseY)/(text.sizeSlider().h - text.sizeSlider().r) * 130);
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
                int updatedRed = PApplet.parseInt((mouseX - text.colorselection().rSlider().startingPosition)/(text.colorselection().rSlider().w - text.colorselection().rSlider().r) * (255));
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
                int updatedGreen = PApplet.parseInt((mouseX - text.colorselection().gSlider().startingPosition)/(text.colorselection().gSlider().w - text.colorselection().gSlider().r) * (255));
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
                int updatedBlue = PApplet.parseInt((mouseX - text.colorselection().bSlider().startingPosition)/(text.colorselection().bSlider().w - text.colorselection().bSlider().r) * (255));
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
abstract class CircleButton{
    private boolean onState;
    private int x, y, r;
    private int original;
    CircleButton(int x, int y, int r){
        this.x = x;
        this.y = y;
        this.r = r;
        onState = false;
        original = r;
    }
    public void draw()
    {
        noStroke();
        ellipseMode(CENTER);
        if (this.isOver()) fill(255, 100, 100);
        else noFill();
        ellipse(x, y, r, r);
    }
    public boolean isOver(){
        if (sqrt(sq(mouseX-x)+ sq(mouseY-y)) > r/2) return false;
        else {
            return true;
        }
    }  
    public void invisible(){
        this.r=0;
    }

    public boolean isInvisible(){
        return this.r==0;
    }

    public void visible(){
        this.r=original;
    } 
}

abstract class SquareButton{
    private boolean onState;
    private int x, y, d;
    private int c;
    SquareButton(int x, int y, int d, int c){
        this.x = x;
        this.y = y;
        this.d = d;
        this.c = c;
        onState = false;
    }
    public void draw()
    {
        stroke(0);
        strokeWeight(1);
        rectMode(CENTER);
        if (this.isOver()) fill(255, 100, 100);
        else fill(c);
        rect(x, y, d, d);   
    }
    public boolean isOver(){
        if ((Math.abs(mouseX-this.x) > this.d/2) || (Math.abs(mouseY-this.y) > this.d/2)) return false;
        return true;
    }   
    public void onState(){
        onState = true;
    }
    public void notOnState(){
        onState = false;
    }
    public boolean isOnState(){
        return onState;
    }
}

class FontButton extends SquareButton
{
    private boolean onState;
    private PImage icon;
    FontButton(int x, int y, int d, int c){
        super(x, y, d, c);
        icon = loadImage("f.png");
    }
    public void draw(){
        fill(super.c);
        super.draw();
        imageMode(CENTER);
        int iconSize1 = 3* super.d/5;
        int iconSize2 = super.d;
        image(icon, super.x, super.y, iconSize1, iconSize2);
    }
    public void onState(){
        super.onState();
    }
    public void notOnState(){
        super.notOnState();
    }
    public boolean isOnState(){
        return super.isOnState();
    }
}

class ColorButton extends SquareButton
{
    private boolean onState;
    ColorButton(int x, int y, int d, int c){
        super(x, y, d, c);
    }
    public void draw(){
        super.draw();
    }
    public void onState(){
        super.onState();
    }
    public void notOnState(){
        super.notOnState();
    }
    public boolean isOnState(){
        return super.isOnState();
    }
    public void setR(int r){
        float g = green(super.c);
        float b = blue(super.c);
        super.c = color(r, g, b);
    }
    public void setG(int g){
        float r = red(super.c);
        float b = blue(super.c);
        super.c = color(r, g, b);
    }
    public void setB(int b){
        float r = red(super.c);
        float g = green(super.c);
        super.c = color(r, g, b);
    }
}

class CameraButton extends CircleButton
{
    private boolean onState;
    private PImage icon;
    CameraButton(int x, int y, int r){
        super(x, y, r);
        icon = loadImage("upload.png");
    }
    public void draw(){
        super.draw();
        imageMode(CENTER);
        int iconSize = 4* super.r/5;
        image(icon, super.x, super.y ,iconSize, iconSize);
    }

    public int size(){
        return super.r;
    }
}

class TextButton extends CircleButton
{
    private boolean onState;
    private PImage icon;

    
    TextButton(int x, int y, int r){
        super(x, y, r);
        icon = loadImage("Aa.png");
    }

    public void draw()
    {
        super.draw();
        int iconSize = 3* super.r/5;
        image(icon, super.x, super.y ,iconSize, iconSize);
        
    }   
    public void textOnState(){
        this.onState = true;
    }
    public boolean textActivate(){
        return onState;
    }
    
}
class DeleteButton extends CircleButton
{
    private boolean onState;
    private PImage icon;
    DeleteButton(int x, int y, int r){
        super(x, y, r);
        icon = loadImage("trashcan.png");
    }
    public void draw()
    {
        noStroke();
        ellipseMode(CENTER);
        if (this.isOver() && text!=null && text.firstXposition!=-1 && text.firstYposition!=-1) fill(255, 100, 100); // 텍스트를 드래그하다가 지나간 경우만 색깔 바뀌게
        else noFill();
        ellipse(super.x, super.y, super.r, super.r);
        imageMode(CENTER);
        int iconSize = 3* super.r/5;
        image(icon, super.x, super.y ,iconSize, iconSize);
    }   
}

class EachFont extends SquareButton
{
    private boolean onState;
    private PFont font;
    
    EachFont(int x, int y, int d, String font){
        super(x, y, d, color(255));
        if (font.equals("BlackoakStd.otf")) this.font = createFont(font, PApplet.parseInt(super.d/3));
        else this.font = createFont(font, PApplet.parseInt(3*super.d/5));
    }
    public void draw(){
        super.draw();
        textAlign(CENTER);
        textFont(font);
        fill(0);
        text("Aa", super.x, super.y); 
    }
    public PFont getFont(){ return font; }
}
class FontSelection
{
    private int x, y, w, h, d;
    private boolean onState;
    private PFont font;
    
    FontSelection(int x, int y, int w, int h, int d){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.d = d;
        for (int i=0 ; i<fontList.length; i++){
            int x1 = PApplet.parseInt(x-2*d + (i%5)*d);
            int y1 = PApplet.parseInt(y-3*d/2 + (i/5)*d);
            EachFont eachFont = new EachFont(x1, y1, d, fontList[i]);
            fonts.add(eachFont);
        }
    }
    public void draw(){
        stroke(0);
        strokeWeight(1);
        noFill();
        rectMode(CENTER);
        rect(x, y, w, h);
        for (EachFont eachfont: fonts){
            eachfont.draw();
        }
    }
    public boolean isOver(){
        if ((Math.abs(mouseX-this.x) > this.w/2) || (Math.abs(mouseY-this.y) > this.h/2)) return false;
        return true;
    }
}

class ColorSelection
{
    private int x, y, w, h;
    private boolean onState;
    private Slider rSlider;
    private Slider gSlider;
    private Slider bSlider;
    
    ColorSelection(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        rSlider = new Slider(x, y-buttonsize/2, color(255,0,0), false, true);
        gSlider = new Slider(x, y, color(0, 255, 0), false, true);
        bSlider = new Slider(x, y+buttonsize/2, color(0,0,255), false, true);
    }
    public void draw(){
        stroke(0);
        strokeWeight(1);
        fill(100);
        rectMode(CENTER);
        rect(x, y, w, h);
        // rect(x, y, 5*buttonsize/2, 2*buttonsize);
        rSlider.draw();
        gSlider.draw();
        bSlider.draw(); 
    }
    public boolean isOver(){
        if ((Math.abs(mouseX-this.x) > this.w/2) || (Math.abs(mouseY-this.y) > this.h/2)) return false;
        return true;
    }   
    public Slider rSlider(){ return rSlider;}
    public Slider gSlider(){ return gSlider;}
    public Slider bSlider(){ return bSlider;}
}
class Slider
{
    private float x, y_bar, circle, w, h;
    private float startingPosition;
    private int r;
    private int c;
    private boolean vertical;
    private boolean onState;

    Slider(float x, float y_bar, int c, boolean vertical, boolean fromtheLowest){
        this.vertical = vertical;
        r= 10;
        this.x = x;
        this.y_bar = y_bar;
        this.c =c;
        if (vertical){
            w=10;
            h=3*height/5;
            startingPosition = y_bar+h/2-r/2;
            if (fromtheLowest) circle = startingPosition;
            else circle = startingPosition - PApplet.parseInt((h-r)/13);
        }
        else{
            h=10;
            w = height/5;
            startingPosition = x - w/2+r/2;
            circle = startingPosition;
        }
    }
    
    public void draw()
    {
        noStroke();
        fill(255);
        rectMode(CENTER);
        rect(x, y_bar, w, h, r );
        fill(c);
        ellipseMode(CENTER);
        if (vertical) ellipse(x, circle, r, r);
        else ellipse(circle, y_bar, r, r);
    }
    public void moveSlider(float p)
    {
        circle = p;
    }
    public boolean isOver()
    {
        if (vertical){
            if (sqrt(sq(mouseX-x)+ sq(mouseY-circle)) > r/2) return false;
            else {
                return true;
            }
        }else{
            if (sqrt(sq(mouseX-circle)+ sq(mouseY-y_bar)) > r/2) return false;
            else {
                return true;
            }
        }
    }
}
class Text
{
    private PFont font;
    private float x, y;
    private String content;
    private int size;
    private int c;
    private boolean onState;
    private boolean ifDone;
    private float sw;
    private Slider slider;
    private Slider rotationSlider;

    private float firstXposition;
    private float firstYposition;
    private float textX;
    private float textY;
    private boolean sliderOn;
    private boolean sliderOn2;
    private boolean[] sliderOn3;
    private float rotation;

    private float originalposition;
    private float originalposition2;
    private float[] originalposition3;

    private ColorSelection colorselection;
    private FontButton fontButton;
    private ColorButton colorButton;

    Text(String content,float x, float y, int size, int c){
        this.content = content;
        this.x = x;
        this.y = y;
        this.size = size;
        this.c = c;
        this.onState = true; //쓰는중인지
        this.rotation = 0;
        slider = new Slider(width-2*buttonsize/3, 13*height/24, color(0), true, false);
        rotationSlider = new Slider(width-buttonsize/3, 13*height/24, color(0), true, true);
        this.font = createFont("LithosPro-Regular.otf", 30);

        int a = width - buttonsize/2;
        int b = buttonsize;
        colorselection = new ColorSelection(width - 5*buttonsize/2, 2*buttonsize, 6*buttonsize/2, 2*buttonsize);
        fontButton = new FontButton(PApplet.parseInt(a-buttonsize/4), PApplet.parseInt(b+3*buttonsize/4), PApplet.parseInt(buttonsize/3), color(255));
        colorButton = new ColorButton(PApplet.parseInt(a+buttonsize/4), PApplet.parseInt(b+3*buttonsize/4), PApplet.parseInt(buttonsize/3), color(0));

        firstXposition = -1;
        firstYposition = -1;
        textX = -1;
        textY = -1;
        sliderOn = false;
        sliderOn2 = false;
        sliderOn3 = new boolean[]{false, false, false};
        originalposition = 0;
        originalposition2 = 0;
        originalposition3 = new float[]{0, 0, 0};
    }
    public FontButton fontButton(){ return fontButton; }

    public ColorButton colorButton(){ return colorButton; }

    public void setFont(PFont font){ this.font = font; }

    public ColorSelection colorselection(){ return colorselection; }

    public void keepWriting() { onState = true; }

    public void setDone() { onState = false; }

    public boolean ifDone() { return !onState; }

    public String getContent() { return content; }

    public float getStringWidth() { return sw;}

    public int length() { return content.length(); }

    public void setSize(int s) { size = s; }

    public void setRotation(float a){ rotation = a;}

    public int size() { return size; }

    public void setContent(String update){ this.content = update; }

    public void changePosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setR(int r){
        float g = green(c);
        float b = blue(c);
        this.c = color(r, g, b);
    }
    public void setG(int g){
        float r = red(c);
        float b = blue(c);
        this.c = color(r, g, b);
    }
    public void setB(int b){
        float r = red(c);
        float g = green(c);
        this.c = color(r, g, b);
    }

    public void cursor(int c, float x, float y)
    {
        if ((millis()/500)%2==0) noStroke();
        else {
            strokeWeight(2);
            stroke(c);
        } 
        line(x, y-size, x, y);
    }

    public void draw()
    {
        textFont(font);
        fill(c);
        if (content == "" && onState){
            cursor(c, x, y);
        }else{
            textSize(size);
            textAlign(CENTER);
            sw = textWidth(content);
            if (! onState && rotation!=0){
                pushMatrix();
                translate(x,y);
                rotate(rotation);
                text(content, 0, 0);
                popMatrix(); 
            }else{
                text(content, x, y);
            }
            if (onState){
                cursor(c, x+sw/2, y);
            }
        }
    }   
    public void textRotation(int angle) {rotation = angle; }
    public Slider sizeSlider(){ return slider; }
    public Slider rotationSlider(){ return rotationSlider; }
    public boolean isOver()
    {
        boolean isOver;
        pushMatrix();
        translate(x,y);
        rotate(rotation);
        float mx = mouseX-x;
        float my = mouseY-y;
        if (Math.abs(my*sin(rotation)+mx*cos(rotation))<sw/2 && Math.abs(my*cos(rotation)-mx*sin(rotation))<size/2){
            isOver = true;
        }else{
            isOver = false;
        }
        popMatrix();
        return isOver;  
    }
}

  public void settings() {  size(960, 720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Java_Code" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
