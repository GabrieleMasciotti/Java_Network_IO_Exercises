//package assignment3_lock;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Tesista implements Runnable{
    Laboratorio lab;
    int k;
    int i;
    public Tesista(Laboratorio l, int k, int i){
        this.lab=l;
        this.k=k;
        this.i=i;
    }
    @Override
    public void run() {
        for(int t=0;t<k;t++){
            try {
                lab.richiestaPCTesista(i);
                System.out.printf("Tesista %s ha utilizzato il laboratorio per la %d° volta.\n",Thread.currentThread(),t+1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Professore.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(t<k-1){try {
                Thread.sleep(2500);             //attesa prima di rientrare nel laboratorio
            } catch (InterruptedException ex) {
                Logger.getLogger(Professore.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
    }
    
}
