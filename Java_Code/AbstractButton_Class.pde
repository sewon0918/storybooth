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
    private color c;
    SquareButton(int x, int y, int d, color c){
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

