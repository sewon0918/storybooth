class Slider
{
    private float x, y_bar, circle, w, h;
    private float startingPosition;
    private int r;
    private color c;
    private boolean vertical;
    private boolean onState;

    Slider(float x, float y_bar, color c, boolean vertical, boolean fromtheLowest){
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
            else circle = startingPosition - int((h-r)/13);
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