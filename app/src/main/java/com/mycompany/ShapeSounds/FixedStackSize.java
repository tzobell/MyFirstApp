package com.mycompany.ShapeSounds;

import java.util.Collection;
import java.util.Stack;

/**
 * Created by Owner on 4/26/2016.
 */


public class FixedStackSize<E> extends Stack<E> {
    OnEmptyListener onempty = null;
    OnFirstElementAdded onFirst = null;
    final int stacksize;
    FixedStackSize(int size){
        super();
        if(size >0) {
            stacksize = size;
        }
        else{
            stacksize = 1;
        }
    }

    public void setOnEmptyListener(OnEmptyListener onemptylistener){
        onempty = onemptylistener;
    }

    public void setOnFirstElementAddedListener(OnFirstElementAdded firstadded){
        onFirst = firstadded;
    }

    private void checkSize(){
        if(this.size() > stacksize){
            while(this.size() > stacksize) {
                this.remove(0);
            }
        }
    }

    @Override
    public E push(E object) {
        int zb = this.size();
        E e = super.push(object);
        int za  = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }
        checkSize();
        return e;

    }

    @Override
    public synchronized E pop(){
        int zb = this.size();
        E e = super.pop();
        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }
        return e;
    }


    @Override
    public synchronized boolean add(E object){
        int zb = this.size();
        boolean added = super.add(object);
        int za = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }
        checkSize();
        return added;
    }

    @Override
    public void add(int location, E object){
        int zb = this.size();
        super.add(location,object);
        int za = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }
        checkSize();
    }

    @Override
    public synchronized boolean addAll(int location, Collection<? extends E> collection) {
        int zb = this.size();
        boolean addedall = super.addAll(location, collection);
        int za = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }
        checkSize();
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
        checkSize();
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
        checkSize();
    }

    @Override
    public synchronized void insertElementAt(E object, int location) {
        int zb = this.size();
        super.insertElementAt(object,location);
        int za  = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }
        checkSize();
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
