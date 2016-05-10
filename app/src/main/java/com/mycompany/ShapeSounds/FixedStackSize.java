package com.mycompany.ShapeSounds;

import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by Owner on 4/26/2016.
 */


public class FixedStackSize<E> extends ListeningVector<E> {

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



    private void checkSize(){
        if(this.size() > stacksize){
            while(this.size() > stacksize) {
                this.remove(0);
            }
        }
    }


    public E push(E object) {
        int zb = this.size();
        addElement(object);
        int za  = this.size();
        if(zb == 0 && za > 0 && onFirst!=null){
            onFirst.FirstAdded();
        }
        checkSize();
        return object;

    }


    public synchronized E pop(){
        int zb = this.size();
        if (elementCount == 0) {
            throw new EmptyStackException();
        }
        final int index = --elementCount;
        final E obj = (E) elementData[index];
        elementData[index] = null;
        modCount++;

        int za = this.size();
        if(zb >= 1 && za == 0 && onempty!=null){
            onempty.onEmpty();
        }
        return obj;
    }


    @Override
    public synchronized boolean add(E object){
        boolean added = super.add(object);
        checkSize();
        return added;
    }

    @Override
    public void add(int location, E object){
        super.add(location,object);
        checkSize();
    }

    @Override
    public synchronized boolean addAll(int location, Collection<? extends E> collection) {
        boolean addedall = super.addAll(location, collection);
        checkSize();
        return addedall;
    }

    @Override
    public synchronized boolean addAll(Collection<? extends E> collection) {
        boolean addedall = super.addAll(collection);
        checkSize();
        return addedall;
    }

    @Override
    public synchronized void addElement(E object) {
        super.addElement(object);
        int za  = this.size();
        checkSize();
    }

    @Override
    public synchronized void insertElementAt(E object, int location) {
        super.insertElementAt(object,location);
        checkSize();
    }
}
