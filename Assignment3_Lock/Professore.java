//package assignment3_lock;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Professore implements Runnable{
    Laboratorio lab;
    int k;
    public Professore(Laboratorio l, int k){
        this.lab=l;
        this.k=k;
    }
    @Override
    public void run(){
        for(int t=0;t<k;t++){
            try {
                lab.prenotaLab();
                System.out.printf("Professore %s ha utilizzato il laboratorio per la %d° volta.\n",Thread.currentThread(),t+1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Professore.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(t<k-1){try {
                Thread.sleep(8000);             //attesa prima di rientrare nel laboratorio
            } catch (InterruptedException ex) {
                Logger.getLogger(Professore.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
        
    }
}
