class FontButton extends SquareButton
{
    private boolean onState;
    private PImage icon;
    FontButton(int x, int y, int d, color c){
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
    ColorButton(int x, int y, int d, color c){
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
        if (font.equals("BlackoakStd.otf")) this.font = createFont(font, int(super.d/3));
        else this.font = createFont(font, int(3*super.d/5));
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