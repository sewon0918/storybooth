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
            int x1 = int(x-2*d + (i%5)*d);
            int y1 = int(y-3*d/2 + (i/5)*d);
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