package com.mycompany.ShapeSounds;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by Owner on 5/9/2016.
 */
public class ListeningVector<E> extends Vector<E> {
    OnEmptyListener onempty = null;
    OnFirstElementAdded onFirst = null;
    public ListeningVector() {
        super();
    }


    public ListeningVector(int capacity) {
        super(capacity, 0);
    }


    public ListeningVector(int capacity, int capacityIncrement) {
       super(capacity,capacityIncrement);
    }


    public ListeningVector(Collection<? extends E> collection) {
        super(collection.size(), 0);

    }
    public void setOnEmptyListener(OnEmptyListener onemptylistener){
        onempty = onemptylistener;
    }

    public void setOnFirstElementAddedListener(OnFirstElementAdded firstadded){
        onFirst = firstadded;
    }
    @Override
    public void add(int location, E object){
        int zb = this.size();
        super.add(location,object);
        int za = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }

    }


    @Override
    public synchronized boolean add(E object) {
        int zb = this.size();
        boolean added = super.add(object);
        int za = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }
        return added;
    }
    @Override
    public synchronized boolean addAll(int location, Collection<? extends E> collection) {
        int zb = this.size();
        boolean addedall = super.addAll(location, collection);
        int za = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }

        return addedall;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> collection) {
        int zb = this.size();
        boolean addedall = super.addAll(collection);
        int za = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }

        return addedall;
    }

    @Override
    public synchronized void addElement(E object) {
        int zb = this.size();
        super.addElement(object);
        int za  = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }

    }

    @Override
    public synchronized void insertElementAt(E object, int location) {
        int zb = this.size();
        super.insertElementAt(object,location);
        int za  = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }

    }

    @Override
    public void clear()
    {int zb = this.size();
        super.clear();
        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }

    }



    @Override
    public synchronized E remove(int location){
        int zb = this.size();
        E e = super.remove(location);
        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }
        return e;
    }


    @Override
    public boolean remove(Object object){
        int zb = this.size();
        boolean e = super.remove(object);
        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }
        return e;
    }

    @Override
    public synchronized boolean removeAll(Collection<?> collection){
        int zb = this.size();
        boolean e = super.removeAll(collection);
        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }
        return e;
    }

    @Override
    public synchronized void removeAllElements(){
        int zb = this.size();
        super.removeAllElements();
        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }

    }

    @Override
    public synchronized boolean removeElement(Object object) {
        int zb = this.size();
        boolean e = super.removeElement(object);
        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }
        return e;
    }

    @Override
    public synchronized void removeElementAt(int location) {
        int zb = this.size();
        super.removeElementAt(location);
        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }
    }

    @Override
    protected void removeRange(int start, int end) {
        int zb = this.size();
        super.removeRange(start,end);
        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }
    }
}
