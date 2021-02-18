//package filecrawler;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

public class ListaCartelle {
    private final LinkedList <File> listaDirectories = new LinkedList <> ();
    private boolean done = false;
    
    public synchronized void setDone(){     //metodo utilizzato dal produttore per segnalare di aver terminato la ricorsione sulle sotto-directories
        this.done = true;
        this.notifyAll();
    }
    
    public void addDirectory(File dir){     //aggiunge una directory alla lista
        synchronized(this){
            this.listaDirectories.add(dir);
            this.notifyAll();
        }
    }
    
    public void addDirectories(File [] dirs){       //aggiunge un array di directory alla lista
        synchronized(this){
            this.listaDirectories.addAll(Arrays.asList(dirs));
            this.notifyAll();
        }
    }
    
    public int processDirectory() throws InterruptedException{      //metodo utilizzato dai consumatori per processare una directory (elencare i file in essa contenuti)
        synchronized(this){
            while(this.listaDirectories.isEmpty() && !this.done){   //fintanto che la lista è vuota e il produttore non ha terminato di inserire cartelle nella lista, attende
                this.wait();
            }
            if(this.done && this.listaDirectories.isEmpty()){return -1;}    //risvegliatosi, se la lista è vuota e il produttore ha terminato, restituisce -1
            else{
                File directory = this.listaDirectories.removeFirst();       //preleva la prima cartella nella lista condivisa
                File[] listFiles = directory.listFiles();
                System.out.printf("Contenuto della directory %s: ",directory.getName());
                for(File file : listFiles){                                  //elenca i file contenuti nella cartella prelevata
                    System.out.printf("%s ; ",file.getName());
                }
                System.out.println("\n");
                return 0;
            }
        }
    }
    
}
