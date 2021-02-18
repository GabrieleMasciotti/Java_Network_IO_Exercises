//package filecrawler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumatore implements Runnable{
    ListaCartelle listaDirectory;
    public Consumatore(ListaCartelle lista){
        this.listaDirectory = lista;
    }
    
    @Override
    public void run() {
        int ok=0;
        while(ok!=-1){
            try {
                ok = this.listaDirectory.processDirectory();    //processa le directories una alla volta
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumatore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
