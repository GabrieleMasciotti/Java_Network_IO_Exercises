//package assignment2;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente implements Runnable{
    int tempoOperazione = 0;
    int numero;
    public Cliente(int i){
        this.numero = i;
    }
    @Override
    public void run() {
        try {
            this.tempoOperazione = (int) (Math.random()*5000);      //i clienti impiegano da 0 a 5 secondi per concludere l'operazione allo sportello
            Thread.sleep(tempoOperazione);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.printf("Servito dallo sportello %s il cliente con numero %d in %d secondi.\n",Thread.currentThread(),numero,tempoOperazione/1000);
    }
    
}
