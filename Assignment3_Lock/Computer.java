//package assignment3_lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Computer {
    int indice;
    boolean occupied;
    int tesistiInAttesa;
    final ReentrantLock lock;
    final Condition stWait;
    final Condition teWait;
    public Computer(int i){
        this.indice=i;
        this.occupied=false;
        this.tesistiInAttesa=0;
        lock=new ReentrantLock();
        stWait=lock.newCondition();
        teWait=lock.newCondition();
    }
}
