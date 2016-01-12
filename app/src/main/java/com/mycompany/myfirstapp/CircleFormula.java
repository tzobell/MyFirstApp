package com.mycompany.myfirstapp;
import android.util.Pair;
import java.util.Vector;

public class CircleFormula implements ShapeFormula {
    float h;
    float k;
    float radius;
    Vector<Formula> goldencircles;
    Vector<ShapeFormula> inside;
    Pair<Float,Float>[] keyPoints;
    Vector<ShapeFormula> connectedShapes;
    private ShapeFormula parent;


    public CircleFormula(float h, float k, float radius, boolean hasGoldenCircles) {
        this.h = h;
        this.k = k;
        this.radius = radius;
        keyPoints = new Pair[4];
        keyPoints[0] = new Pair<>(h + radius, k);
        keyPoints[1] = new Pair<>(h - radius, k);
        keyPoints[2] = new Pair<>(h, k + radius);
        keyPoints[3] = new Pair<>(h, k - radius);
        connectedShapes = new Vector<>();
        goldencircles = new Vector<>();
        inside = new Vector<>();

        if (hasGoldenCircles) {
            goldencircles.add(new PointFormula(h, k));
            for (Notes note : DiatonicScale.notes) {
                goldencircles.add(new CircleFormula(h, k, DiatonicScale.radiusCircle(note, radius), false));
            }

            for (int n = 3; n < 13; ++n) {
                if (n != 4 && n != 6) {
                    double apothem = radius * (Math.cos((Math.PI / n)));
                    double s = apothem * 2 * Math.tan((Math.PI / n));
                    float gradius = (float) (s / (2 * Math.tan(Math.PI / n)));
                    goldencircles.add(new CircleFormula(h, k, gradius, false));
                }
            }
        }
        else {
            goldencircles.add(this);
        }
    }

    public CircleFormula(float h, float k, float radius){
        this(h,k,radius,true);
    }

    public CircleFormula(float startx,float starty,float endx,float endy, boolean hasGoldenCircles){

        this(((startx + endx)/2),((starty+endy)/2),(float)(Maths.GetDistance(startx,starty,endx,endy)/2),hasGoldenCircles);
    }
    public CircleFormula GetCircumCircle(){
        return this;
    }
    public void SetParent(ShapeFormula sf){
        parent = sf;
    }
    public Pair<Float,Float> GetClosestToPerimeter(float x,float y){
        return GetClosestPoint( x, y, false);
    }
    public Pair<Float,Float> GetClosestToPerimeter(float xstart,float ystart,float x,float y){
        return GetClosestPoint(xstart, ystart, x, y, false);
    }
    public Pair<Float,Float> GetClosestToPerimeter(Pair<Float,Float> p){
        return GetClosestToPerimeter(p.first, p.second);
    }
    public Pair<Float,Float> GetClosestToPerimeter(Pair<Float,Float> pstart,float x,float y){
        return GetClosestToPerimeter(pstart.first, pstart.second, x, y);
    }
    public Pair<Float,Float> GetClosestToPerimeter(float xstart,float ystart,Pair<Float,Float> p){
        return GetClosestToPerimeter(xstart, ystart, p.first, p.second);
    }

    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,float x,float y){
        return GetClosestPoint(xstart, ystart, x, y, false);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(float x,float y){
        return GetClosestPoint(x, y, false);
    }

    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,float x,float y,boolean lockintoKeyPoints){
        return GetClosestToCircumCircle(xstart, ystart, x, y);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(float x,float y,boolean lockintoKeyPoints){
        return GetClosestToCircumCircle(x, y);
    }

    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> pstart,Pair<Float,Float> p ){
        return GetClosestToCircumCircle(pstart.first, pstart.second, p.first, p.second);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,Pair<Float,Float> p ){
        return GetClosestToCircumCircle(xstart, ystart, p.first, p.second);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> pstart,float x,float y ){
        return GetClosestToCircumCircle(pstart.first, pstart.second, x, y);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> p){
        return GetClosestToCircumCircle(p.first, p.second);
    }


    public Pair<Float,Float> GetStart(){
        return new Pair<>(h+radius,k+radius);
    }
    public Pair<Float,Float> GetEnd(){
        return new Pair<>(h-radius,k-radius);
    }
    public Pair<Float,Float> GetClosestPoint(float x,float y){
        boolean in = goldencircles.size()> 1&&inBounds(x,y);
        return GetClosestPoint(x, y, in);
    }
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,float x,float y){
        boolean in = goldencircles.size()> 1 && inBounds(x,y);
        return GetClosestPoint(xstart, ystart, x, y, in);
    }

    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> p){
        return GetClosestPoint(p.first,p.second);
    }
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> pstart,Pair<Float,Float> p ){
        return GetClosestPoint(pstart.first,pstart.second,p.first,p.second);
    }
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,Pair<Float,Float> p ){
        return GetClosestPoint(xstart,ystart,p.first,p.second);
    }
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> pstart,float x,float y ){
        return GetClosestPoint(pstart.first,pstart.second,x,y);
    }


    //return point on the circumference in circle closest to point x,y
    public Pair<Float,Float> GetClosestPoint(float x,float y, boolean in){
        float closestX;
        float closestY;
        if(!in) {
            double den = (Math.sqrt((Math.pow((x-h),2)+Math.pow((y-k),2))));
            closestX = (float) (h + (radius *((x-h)/den)));//(x > (radius + h) || x < (h - radius)) ? (x > radius + h) ? radius + h : radius - h : x;
            closestY = (float) (k + (radius *((y-k)/den)));//((float) Math.sqrt((radius * radius) - Math.pow((closestX - h), 2)) * (y >= k ? 1.0f : -1.0f)) + k;
        }
        else{
            Pair<Float, Float> point = new Pair<>(h, k);

            double distance = Maths.GetDistance(x,y,point);
            for (Formula circle : goldencircles) {
                Pair<Float, Float> temppoint = circle.GetClosestPoint(x, y);
                double tempdis = Maths.GetDistance(x, y, temppoint);
                if (tempdis < distance ) {
                    distance = tempdis;
                    point = temppoint;

                }
            }
            closestX = point.first;
            closestY = point.second;
        }




        return new Pair<>(closestX,closestY);
    }
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,float x,float y, boolean in){
        LineFormula lf = new LineFormula(xstart,ystart,h,k,true);
        Pair<Float,Float> p = lf.GetClosestValue(x,y,false);
        x = p.first;
        y = p.second;

        return GetClosestPoint(x,y,in);
    }


    public double getDiamater(){
        return (2*radius);
    }


    //public float getH(){return h;}
    //public float getK(){return k;}
    //public float getRadius(){return radius;}


    public double Area(){
        return (Math.PI * Math.pow(radius,2));
    }

    //note the play is the frequency = area(this)/area(shapes inside this shape)
    public void Play(){
        double diamater = this.getDiamater();
        double area = Area();

        for(ShapeFormula sh:inside){

            double frequency = area/sh.Area();
            sh.Play();
            play(frequency);
        }
        for(ShapeFormula sh:connectedShapes){
            double pDiamater = sh.getDiamater();
            double frequency = pDiamater>diamater? pDiamater/diamater:diamater/pDiamater;
            play(frequency);
        }
    }

    private void play(double frequency ){
        Notes note = DiatonicScale.findNote(frequency);
        Sound.AddNote(note);
    }

    public Pair<Boolean,LineFormula> isKeyPoint(float x,float y){
        return new Pair<>(false,null);
    }
    public Pair<Boolean,LineFormula> isKeyPoint(float x,float y,float xend,float yend){
        return new Pair<>(false,null);
    }

    public Pair<Float,Float> getEndForArea(Pair<Float,Float> start,double area){
        Pair<Float,Float> end = new Pair<>(h,k);
        double newdiamater = 2 * Math.sqrt(area/Math.PI);
        return Maths.findDistantPoint(start,end,newdiamater,false);
    }

    public boolean inCircumCircle(Pair<Float,Float> p){
        return inBounds(p);
    }
    public boolean inCircumCircle(float x, float y){

       return inBounds(x,y);
    }
    public boolean inCircumCircle(Formula shape){
        return inBounds(shape);
    }
    public boolean inBounds(Pair<Float,Float> p){
        return inBounds(p.first, p.second);
    }



    public boolean inBounds(float x, float y){
        boolean isin = false;
        double dis = Maths.GetDistance(x,y,h,k);
        if(dis <= radius){
            isin = true;
        }
        return isin;
    }
    public boolean inBounds(Formula shape){
        boolean in = true;
        if(shape instanceof CircleFormula){
            in = false;
            CircleFormula csf = (CircleFormula) shape;

            double d = Math.abs(Maths.GetDistance(h,k,csf.h,csf.k));

            if(d <= Math.abs(radius - csf.radius) && radius > csf.radius){
                in = true;
            }
        }
        else {
            Pair<Float, Float>[] kp = shape.GetKeyPoints();
            for (int i = 0; i < kp.length; ++i) {
                if (!inBounds(kp[i])) {
                    in = false;
                    i = kp.length;
                }
            }
        }
        return in;
    }

    public ShapeFormula FindCircumShape(ShapeFormula shape){
        ShapeFormula sh = null;
        try{
            if(inBounds(shape)){
                sh = this;
                for(int i = 0; i < inside.size(); ++i){
                    if(inside.get(i).inBounds(shape)){
                        sh = inside.get(i).FindCircumShape(shape);
                        i = inside.size();
                    }
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return sh;
    }

    public ShapeFormula FindCircumShape(Pair<Float,Float> p){
     return FindCircumShape(p.first, p.second);
    }
    public ShapeFormula FindCircumShape(float x, float y){
        ShapeFormula sh = null;
        try{
            if(inBounds(x,y)){
                sh = this;
                for(int i = 0; i < inside.size(); ++i){
                    if(inside.get(i).inBounds(x,y)){
                        sh = inside.get(i).FindCircumShape(x, y);
                        i = inside.size();
                    }
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return sh;
    }

    public Pair<Boolean,ShapeFormula> AddShape(ShapeFormula shape){
        Pair<Boolean,ShapeFormula> bs = new Pair<>(false,null);
        boolean in = inBounds(shape.GetCircumCircle());
        if(in){
            boolean inshape = false;
            for(int i = 0; i < inside.size(); ++i){
                bs = inside.get(i).AddShape(shape);
                if(bs.first){
                    inshape = true;
                    i = inside.size();
                }

            }
            if(!inshape){
                bs = new Pair<Boolean,ShapeFormula>(true,this);
                boolean inthisshape = false;
                int index = -1;
                for(int i = 0; i < inside.size(); ++i){
                    if(shape.AddShape(inside.get(i)).first){
                        inthisshape = true;
                        index = i;
                        i = inside.size();
                    }
                }
                if(inthisshape){
                    inside.remove(index);
                }
                inside.add(shape);
            }



        }

        return bs;

    }

    public void RemoveShape(ShapeFormula shape){
        if(shape == parent){
            parent = null;
        }
        else{
            int size = inside.size();
            for(int i = 0; i < size; ++i){
                if(inside.get(i)==shape){
                    ShapeFormula s = inside.get(i);
                    inside.remove(i);
                    Vector<ShapeFormula> insideremoved = s.GetInsideShapes();
                    for(ShapeFormula sf:insideremoved){
                        AddShape(sf);
                    }
                    i = size;
                }
            }
        }
    }


    public Vector<ShapeFormula> GetInsideShapes(){
        return inside;
    }
    public Pair<Float,Float>[] GetKeyPoints(){
        return keyPoints;
    }


    public Formula[] GetGoldenPoints(){

        Formula[] p = new Formula[goldencircles.size()];
        return goldencircles.toArray(p);

    }

    //return true or false for if the line passed crosses through the circle
    //if the distance from the center of the circle (h,k) to the closest point on the line from point (h,k) is less than or equal to the
    //radius of the circle, then the line must cross through the circle
    public boolean doesLineCross(LineFormula lf){

        boolean cross = false;
        Pair<Float,Float> closestToLine = lf.GetClosestValue(h,k);
        double distance = Maths.GetDistance(closestToLine,h,k);
        if(distance < radius){
            cross = true;
        }
        return cross;
    }

    public void AddConnectedShape(ShapeFormula sf){

                connectedShapes.add(sf);


    }
    //returns vector of shapes that are connected to, but not completly inside of this shape
    public Vector<ShapeFormula> GetConnectedShapes(){
        return connectedShapes;

    }
}
