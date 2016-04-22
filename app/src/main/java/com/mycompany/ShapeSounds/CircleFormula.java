package com.mycompany.ShapeSounds;
import android.util.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


//class that describes a circle and has methods for finding
//the closest point to this circle, and determining if a Shapeformula
//is inside or intersects this circle
public class CircleFormula implements ShapeFormula {
    //(h,k) is the point that lies in the center of the circle
    float h;
    float k;
    float radius;
    //vector that holds circles where the ratio of this circle's radius: the golden circle is equal to the frequency of a major scale note
    Vector<Formula> goldencircles;
    //vector that holds the shapes that are completely inside of this circle
    Vector<ShapeFormula> inside;
    //keypoints are the points that describe the circle
    Pair<Float,Float>[] keyPoints;
    //vector to hold the ShapeFormula's that this shape is connected to/intersects with
    Vector<ShapeFormula> connectedShapes;
    //the tangent circle described the circle that if two other circles that did not intersect this circle or eachother
    //  with the same radius as this circle were all equal distance from eachother (including this circle, so thats 3 circles all equal distance from eachother),
    // they would all lie on the same tangent line, and the two other circles would have a
    // distance (distance of circle centers - (radius*2)) of 0 from this circles tangent circle,
    // and like wise this circle would have a distance of 0 from the other circle's tangent circles
    //CircleFormula tangentcircle;

    //constructor that creates a circle with a center at (h,k) and the radius passed. I
    // if hasGoldenCircles is true, then add to the vector goldencircles the appropriate circle for each of the notes in  DiatonicScale.
    // If hasTangent is true then initialize tangentCircle, other wise leave it null
    public CircleFormula(float h, float k, float radius, boolean hasGoldenCircles, boolean hasTangentCircle) {
        try {
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
            //tangentcircle = null;
            if (hasTangentCircle) {
               // tangentcircle = new CircleFormula(h, k, (float) ((4 - (Math.sqrt(3) * radius)) / (float) Math.sqrt(3)), false, false);
            }
            if (hasGoldenCircles) {
               goldencircles.add(new PointFormula(h, k));
                for (Notes note : DiatonicScale.notes) {
                    goldencircles.add(new CircleFormula(h, k, DiatonicScale.radiusCircle(note, radius), false));
                }
                for (int n = 3; n < 7; ++n) {
                    //if (n != 4 && n != 6) {
                        double apothem = radius * (Math.cos((Math.PI / n)));
                        double s = apothem * 2 * Math.tan((Math.PI / n));
                        float gradius = (float) (s / (2 * Math.tan(Math.PI / n)));
                        goldencircles.add(new CircleFormula(h, k, gradius, false, false));
                    //}
                }
            } else {
                goldencircles.add(this);
            }
        }
        catch (Exception e) {
            String a = e.getMessage();
            System.out.println(a);
        }
    }

    public CircleFormula(float h, float k, float radius, boolean hasGoldenCircles){
        this(h,k,radius,hasGoldenCircles,hasGoldenCircles);
    }

    public CircleFormula(float h, float k, float radius){
        this(h,k,radius,true,true);
    }

    //use the points (startx,starty) and (endx,endy) to determine the points (h,k)
    public CircleFormula(float startx,float starty,float endx,float endy, boolean hasGoldenCircles){

        this(((startx + endx)/2),((starty+endy)/2),(float)(Maths.GetDistance(startx,starty,endx,endy)/2),hasGoldenCircles);
    }
    public CircleFormula GetCircumCircle(){
        return this;
    }
    public CircleFormula GetInCircle(){
        return this;
    }
    public CircleFormula GetTangentCircle(){
        return this;//tangentcircle;
    }

    //return the point on the circles perimeter/circumference that is closest to the point (x,y)
    //in CircleFormula GetClosestToPerimeter and GetClosestToCircumCircle return the same values
    public Pair<Float,Float> GetClosestToPerimeter(float x,float y){
        return GetClosestToCircumCircle(x, y);
    }
    public Pair<Float,Float> GetClosestToPerimeter(float xstart,float ystart,float x,float y){
        return GetClosestToCircumCircle(xstart, ystart, x, y);
    }
    public Pair<Float,Float> GetClosestToPerimeter(Pair<Float,Float> p){
        return GetClosestToCircumCircle(p);
    }
    public Pair<Float,Float> GetClosestToPerimeter(Pair<Float,Float> pstart,float x,float y){
        return GetClosestToCircumCircle(pstart, x, y);
    }
    public Pair<Float,Float> GetClosestToPerimeter(float xstart,float ystart,Pair<Float,Float> p){
        return GetClosestToCircumCircle(xstart, ystart, p);
    }

    ///return the point on the circles perimeter/circumference that is closest to the point (x,y)
    //in CircleFormula GetClosestToPerimeter and GetClosestToCircumCircle return the same values
    public Pair<Float,Float> GetClosestToCircumCircle(float x,float y){
        return GetClosestPoint(x, y, false, false);
    }
    public Pair<Float,Float> GetClosestToCircumCircle(Pair<Float,Float> p){
        return GetClosestToCircumCircle(p.first, p.second);
    }

    //find the value on the line formed from the line segment ((xstart,ystart),(h,k)) closest to point (x,y) to get point p
    // then return the closest point on circle's circumference to point p
    public Pair<Float,Float> GetClosestToCircumCircle(float xstart,float ystart,float x,float y){
        return GetClosestPoint(xstart, ystart, x, y, false, false);
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

    public Pair<Float,Float> GetStart(){
        return new Pair<Float,Float>(h,k+radius);
    }
    public Pair<Float,Float> GetEnd(){
        return new Pair<Float,Float>(h,k-radius);
    }

    //gets closest point to (x,y) that lies on the circumference or tangent circle
    public Pair<Float,Float> GetBasicClosestPoint(float x,float y){
        return GetClosestPoint(x, y, false, true);
    }
    public Pair<Float,Float> GetBasicClosestPoint(Pair<Float,Float> p){
        return GetBasicClosestPoint(p.first, p.second);
    }

    //return closestpoint for the circle
    //if (in==false) then only search for points on the circumference of this circle
    //else if(in==true) also search points/goldencircles that are within the circumference of this circle
    //if (searchTangent == true) then search the tangent circle for the closest point
    public Pair<Float,Float> GetClosestPoint(float x,float y, boolean in, boolean searchTangent){
        float closestX = x;
        float closestY = y;
        try {
            double distance = Maths.GetDistance(x, y, h, k);
            //in==false
            if (!in) {
                float den = ((float)Math.sqrt(((float)Math.pow((x - h), 2) + (float)Math.pow((y - k), 2))));
                closestX = (float) (h + (radius * ((x - h) / den)));//(x > (radius + h) || x < (h - radius)) ? (x > radius + h) ? radius + h : radius - h : x;
                closestY = (float) (k + (radius * ((y - k) / den)));//((float) Math.sqrt((radius * radius) - Math.pow((closestX - h), 2)) * (y >= k ? 1.0f : -1.0f)) + k;
                double tempdis = Maths.GetDistance(closestX, closestY, x, y);
                if (tempdis > distance) {
                    closestX = h;
                    closestY = k;
                    distance = tempdis;
                }
            }
            //in==true
            else {
                Pair<Float, Float> point = new Pair<Float, Float>(h, k);
                for (Formula circle : goldencircles) {
                    Pair<Float, Float> temppoint = circle.GetClosestPoint(x, y);
                    double tempdis = Maths.GetDistance(x, y, temppoint);
                    if (tempdis < distance) {
                        distance = tempdis;
                        point = temppoint;
                    }
                }
                closestX = point.first;
                closestY = point.second;
            }

          /*  if (searchTangent && tangentcircle != null) {
                Pair<Float, Float> tpoint = tangentcircle.GetClosestPoint(x, y, false, false);
                if (Maths.GetDistance(tpoint, x, y) < distance) {
                    closestX = tpoint.first;
                    closestY = tpoint.second;
                }
            }*/
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return new Pair<Float,Float>(closestX,closestY);
    }
    public Pair<Float,Float> GetClosestPoint(float x,float y, boolean in){
        return GetClosestPoint(x, y, in, in);
    }
    public Pair<Float,Float> GetClosestPoint(float x,float y){
        boolean in = goldencircles.size()> 1&&inBounds(x,y);
        return GetClosestPoint(x, y, in, true);
    }
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> p){
        return GetClosestPoint(p.first, p.second);
    }

    //find the value on the line formed from the line segment ((xstart,ystart),(h,k)) closest to point (x,y) to get point p
    // then return the closest point on or in the circle to point p
    // //if (in==false) then only search for points on the circumference of this circle of the tangent circle
    //else if(in==true) also search points/goldencircles that are within the circumference of this circle
    //if (searchTangent == true) then search the tangent circle for the closest point
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,float x,float y, boolean in, boolean searchTangent){
        LineFormula lf = new LineFormula(xstart,ystart,h,k,true);
        Pair<Float,Float> p = lf.GetClosestValue(x,y,false);
        x = p.first;
        y = p.second;

        return GetClosestPoint(x, y, in, searchTangent);
    }
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,float x,float y, boolean in){
        return GetClosestPoint(xstart,ystart,x,y,in,true);
    }
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,float x,float y){
        boolean in = goldencircles.size()> 1 && inBounds(x,y);
        return GetClosestPoint(xstart, ystart, x, y, in);
    }
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> pstart,Pair<Float,Float> p ){
        return GetClosestPoint(pstart.first,pstart.second,p.first,p.second);
    }
    public Pair<Float,Float> GetClosestPoint(float xstart,float ystart,Pair<Float,Float> p ){
        return GetClosestPoint(xstart, ystart, p.first, p.second);
    }
    public Pair<Float,Float> GetClosestPoint(Pair<Float,Float> pstart,float x,float y ){
        return GetClosestPoint(pstart.first,pstart.second,x,y);
    }

    public double getDiamater(){
        return (2*radius);
    }
    public double Area(){
        return (Math.PI * Math.pow(radius,2));
    }

    //play notes based on the shapes that are insice this circle and the shapes this circle is connected to
    // if shape is inside this circule then the note = this.area/shape.circumcircle.area
    // //if shape is connected to this circle then note = diamater1/diamater2, where diamater1 >= diamater2
    public void Play(){
        try {
            double diamater = this.getDiamater();
            double area = Area();

            for (ShapeFormula sh : inside) {
                double frequency = area / (Math.PI * (Math.pow((sh.getDiamater() / 2), 2)));
                sh.Play();
                play(frequency);
                frequency = area / (area - (Math.PI * (Math.pow((sh.getDiamater() / 2), 2))) );
                play(frequency);
                PlayInside(sh.GetInsideShapes());
            }
            for (ShapeFormula sh : connectedShapes) {
                double distance = Maths.CircleDistance(this, sh.GetCircumCircle());
                double areaOverLap = Math.abs(Maths.FindAreaOverlappingCircles(this, sh.GetCircumCircle()));
                if (areaOverLap > 0) {
                    double frequency = area / areaOverLap;
                    if(frequency <= DiatonicScale.MAXFREQENCY) {
                        play(frequency);
                    }
                }
                double pDiamater = sh.getDiamater();
                double frequency = pDiamater > diamater ? pDiamater / diamater : diamater / pDiamater;
                play(frequency);

                if(sh.GetInCircle()!=sh.GetCircumCircle()){
                    pDiamater = sh.GetInCircle().getDiamater();
                    frequency = pDiamater > diamater ? pDiamater / diamater : diamater / pDiamater;
                    play(frequency);
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }

    private void PlayInside(Vector<ShapeFormula> in) {
        double area = Area();
        for (ShapeFormula sh : in) {
            double frequency = area / (Math.PI * (Math.pow((sh.getDiamater() / 2), 2)));

            play(frequency);
            frequency = area / (area - (Math.PI * (Math.pow((sh.getDiamater() / 2), 2))));
            play(frequency);
            PlayInside(sh.GetInsideShapes());
        }
    }

    private void play(double frequency ){
        Pair<Notes,Octave> note = DiatonicScale.findOctave(frequency);
        Sound.AddNote(note);
    }

    //key points don't mean anything is a circle so always return Pair<Float,Float>(false,null)
    public Pair<Boolean,LineFormula> isKeyPoint(float x,float y){
        return new Pair<>(false,null);
    }
    public Pair<Boolean,LineFormula> isKeyPoint(float x,float y,float xend,float yend){
        return new Pair<>(false,null);
    }

    //return true if (x,y) is  on the circumference of the circle or inside of the circle
    //return false if(x,y) is not on the circumference of the circle or inside of the circle
    // if the distance from (x,y) to the center of the circle (h,k) is less than or equal to the radius of this circle
    //then the point (x,y) lies on the circumference of the circle or inside of the circle
    //other wise the point (x,y) lies outside of the circumference of the circle
    public boolean inBounds(float x, float y){
        boolean isin = false;
        float dis = (float)Maths.GetDistance(x, y, h, k);
        if(dis <= radius){
            isin = true;
        }
        return isin;
    }
    public boolean inBounds(Pair<Float,Float> p){
        return inBounds(p.first, p.second);
    }

    //inCircumCircle returns same values as InBounds
    public boolean inCircumCircle(Pair<Float,Float> p){
        return inBounds(p);
    }
    public boolean inCircumCircle(float x, float y){       return inBounds(x,y);    }
    public boolean inCircumCircle(Formula shape){
        return inBounds(shape);
    }


    //return true if shape is inside of this circle, else return false
    //if shape is a ShapeFormula return true if the ShapeFormula's circumcircle
    //is completly inside of this circle
    //if shape is not a ShapeFormula return true if each of the shapes keypoints are with in
    //the circle, else return false
    public boolean inBounds(Formula shape){
        boolean in = true;
        try {
            if (shape instanceof ShapeFormula) {
                in = false;
                CircleFormula csf = ((ShapeFormula) shape).GetCircumCircle();
                double d = Math.abs(Maths.GetDistance(h, k, csf.h, csf.k));
                if (d <= Math.abs(radius - csf.radius) && radius > csf.radius) {
                    in = true;
                }
            } else {
                Pair<Float, Float>[] kp = shape.GetKeyPoints();
                for (int i = 0; i < kp.length; ++i) {
                    if (!inBounds(kp[i])) {
                        in = false;
                        i = kp.length;
                    }
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return in;
    }

    //find the shape in the vector inside, that shape is inBounds of.
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
    ///find the shape in the vector inside, that the points (x,y) is inBounds of.
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

    //returns a Pair<Boolean,ShapeFormula>
    //if shape was added to this shape or one of the shapes in the vector inside
    //then return Pair<Float,Float>(true,the shapeFormula that shape was added to) else return Pair<Float,Float>(false,null)
    public Pair<Boolean,ShapeFormula> AddShape(ShapeFormula shape){
        Pair<Boolean,ShapeFormula> bs = new Pair<>(false,null);
        try {

            boolean in = inBounds(shape.GetCircumCircle());
            //if shape's circumcircle is inBounds of this shape
            if (in) {
                if(inside.size() > 0) {
                    boolean inshape = false;
                    //try adding shape to one of the ShapeFormula's in the Vector<> inside.
                    //if able to add shape to one of the ShapeFormula's in the Vector<> inside then set inshape to true
                    for (int i = 0; i < inside.size(); ++i) {
                        bs = inside.get(i).AddShape(shape);
                        if (bs.first) {
                            inshape = true;
                            i = inside.size();
                        }
                    }
                    //if not able to add shape to one of the ShapeFormula's in the Vector<> inside
                    if (!inshape) {
                        bs = new Pair<>(true, (ShapeFormula) this);
                        Vector<Integer> remove = new Vector<>();
                        //try each of the ShapeFormula's in the Vector<> inside to shape
                        //if able to add a ShapeFormula's in the Vector<> inside to shape
                        //then add the index that the Shapeformula was at to the begining
                        //of the Vector<> remove so that integers in remove go from largest to smallest
                        for (int i = 0; i < inside.size(); ++i) {
                            if (shape.AddShape(inside.get(i)).first) {
                                remove.add(0, i);
                            }
                        }
                        //go through each index value in remove and remove the shapeFormula at that index value
                        //in the Vector<> inside. Values in remove go from largest to smallest, so the ShapeFormula's
                        //are removed from inside at the end first, so that the ShapeFormula at index i+1 doesn't change after removing the ShapeFormula at index i
                        for (int i = 0; i < remove.size(); ++i) {
                            int j = remove.get(i);
                            inside.remove(j);
                        }
                        //finally add the shape to the Vector<> inside
                        int addindexval = 0;
                        while (addindexval < inside.size() && inside.get(addindexval).getDiamater() > shape.getDiamater()) {
                            ++addindexval;
                        }
                        inside.add(addindexval, shape);
                    }
                }
                else{
                    inside.add(shape);
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }


        return bs;

    }

    //if ShapeFormula shape is in the Vector<> inside or connectedShapes, then remove it from that vector
    public void RemoveShape(ShapeFormula shape){
        try {
            if (inBounds(shape)) {
                RemoveShapeInside(shape);
            } else {
                RemoveShapeConnecting(shape);
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }

    }
    //if ShapeFormula shape is in the Vector<> inside then remove it from that vector
    private void RemoveShapeInside(ShapeFormula shape){
        int size = inside.size();
        try {
            for (int i = 0; i < size; ++i) {
                if (inside.get(i) == shape) {
                    ShapeFormula s = inside.get(i);
                    inside.remove(i);
                    //add any Shapes inside of shape to Vector<> inside
                    Set<ShapeFormula> insideremoved = new HashSet<>();// = s.GetInsideShapes();
                    insideremoved.addAll(s.GetInsideShapes());
                   // Vector<ShapeFormula> insideremoved = s.GetInsideShapes();
                    insideremoved.addAll(s.GetCircumCircle().GetInsideShapes());
                    insideremoved.addAll(s.GetInCircle().GetInsideShapes());
                    for (ShapeFormula sf : insideremoved) {
                       if(sf!=shape.GetInCircle()){
                           AddShape(sf);
                       }
                    }
                    i = size;
                }
                else{
                    inside.get(i).RemoveShape(shape);
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }
    //if ShapeFormula shape is in the Vector<> connectedShapes, then remove it from that vector
    private void RemoveShapeConnecting(ShapeFormula shape){
        int size = connectedShapes.size();
        try {
            for (int i = 0; i < size; ++i) {
                if (connectedShapes.get(i) == shape) {
                    connectedShapes.remove(i);
                    i = size;
                    for (ShapeFormula sfi : inside) {
                        sfi.GetCircumCircle().RemoveShapeConnecting(shape);
                    }
                    shape.RemoveShape(this);
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
    }

    public Vector<ShapeFormula> GetInsideShapes(){
        return inside;
    }

    //returns vector of shapes that are connected to, but not completly inside of this shape
    public Vector<ShapeFormula> GetConnectedShapes(){
        return connectedShapes;
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
        try {
            Pair<Float, Float> closestToLine = lf.GetClosestValue(h, k);
            double distance = Maths.GetDistance(closestToLine, h, k);
            if (distance < radius) {
                cross = true;
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return cross;
    }

    public void AddConnectedShape(ShapeFormula sf){
        try {
            float dis = (float) Maths.CircleDistance(this,sf.GetCircumCircle());

            if ( !inBounds(sf) &&  dis <=0.001)
            {
                int addindexval = 0;
                while (addindexval < connectedShapes.size() && connectedShapes.get(addindexval).getDiamater() > sf.getDiamater()) {
                    ++addindexval;
                }
                connectedShapes.add(addindexval, sf);
                for (ShapeFormula sfi : inside) {
                    sfi.AddConnectedShape(sf);
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }

    }

    //if returns true if cf == this or if cf dimensions are  with in .1f of this circle
    public boolean equals(CircleFormula cf){
        boolean equal = false;
        try {
            if (cf == this) {
                equal = true;
            } else {
                double distance = Maths.GetDistance(h,k,cf.h,cf.k);
                if (Math.abs(cf.h - h) <= .1 && Math.abs(cf.GetCircumCircle().k - k) <= .1 && Math.abs(cf.GetCircumCircle().radius - radius) <= .1) {
                    equal = true;
                }
            }
        }
        catch (Exception e){
            String a  = e.getMessage();
            System.out.println(a);
        }
        return equal;
    }
}
