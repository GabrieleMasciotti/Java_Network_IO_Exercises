//package filecrawler;

import java.io.File;

public class Produttore implements Runnable{
    ListaCartelle listaDirectory;
    File D;
    Filter filtroCartelle = new Filter();               //filtro che restituisce true soltanto se l'elemento esaminato è una cartella

    public Produttore(ListaCartelle lista,File path){
        this.listaDirectory = lista;
        this.D = path;
    }
    
    public void getDir(File path){                      //funzione ricorsiva che visita tutto l'albero delle cartelle
        File [] aux = path.listFiles(filtroCartelle);
        if(aux!=null){
            this.listaDirectory.addDirectories(aux);    //inserimento delle cartelle nella lista condivisa
            for(File dir : aux){
                getDir(dir);                            //ricorsione su tutte le sotto-directories
            }
        }
    }
    
    @Override
    public void run() {
        this.listaDirectory.addDirectory(D);    //aggiunge il nome della prima directory nella lista condivisa
        getDir(D);                              //chiama la funzione ricorsiva che inserisce nella lista condivisa tutte le sotto-cartelle
        this.listaDirectory.setDone();          //segnala (nella struttura dati condivisa) che ha terminato di esaminare l'albero delle directories
    }
    
}
