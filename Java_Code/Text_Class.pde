class Text
{
    private PFont font;
    private float x, y;
    private String content;
    private int size;
    private color c;
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

    Text(String content,float x, float y, int size, color c){
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
        fontButton = new FontButton(int(a-buttonsize/4), int(b+3*buttonsize/4), int(buttonsize/3), color(255));
        colorButton = new ColorButton(int(a+buttonsize/4), int(b+3*buttonsize/4), int(buttonsize/3), color(0));

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

    public void cursor(color c, float x, float y)
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

