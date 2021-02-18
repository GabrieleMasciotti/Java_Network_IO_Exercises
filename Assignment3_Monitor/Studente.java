//package assignment3_monitor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Studente implements Runnable{
    Laboratorio lab;
    int k;
    int i;
    public Studente(Laboratorio l, int k, int i){
        this.lab=l;
        this.k=k;
        this.i=i;
    }
    @Override
    public void run() {
        for(int t=0;t<k;t++){
            try {
                lab.richiestaPCStudente(i);
                System.out.printf("Studente %s ha utilizzato il laboratorio per la %d° volta (computer %d).\n",Thread.currentThread(),t+1,i);
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
