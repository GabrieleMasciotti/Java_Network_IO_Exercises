//package assignment3_monitor;

import java.util.ArrayList;

public class Laboratorio {
    private final ArrayList <Computer> computers = new ArrayList<>();       //struttura dati con l'insieme di tutti i pc del laboratorio
    private boolean prenotato;          //variabile che indica se il laboratorio è prenotato da un professore (inutilizzabile da studenti e tesisti)
    private boolean profInLab;          //variabile che indica se c'è un professore nel laboratorio in un dato istante
    private int presenze;               //numero di utenti nel laboratorio in un dato istante
    private int profInAttesa;           //numero dei professori in attesa di accedere al lab in un dato instante
    public Laboratorio(){
        for(int i=1;i<21;i++){
            this.computers.add(new Computer(i));
        }
        this.prenotato=false;
        this.presenze=0;
        this.profInAttesa=0;
        this.profInLab=false;
    }
    
    public void prenotaLab()throws InterruptedException{
        synchronized(this){
            while(prenotato && profInLab){      //fintanto che il laboratorio è prenotato ed utilizzato da un altro professore, attende
                profInAttesa++;
                wait();
                profInAttesa--;
            }
            prenotato=true;
            profInLab=true;           //professore entra nel laboratorio
            while(presenze>0){
                wait();                 //il prof attende che tutti escano prima di usufruire del laboratorio
            }
        }
        Thread.sleep(1500);           //il professore utilizza il laboratorio
        synchronized(this){
            profInLab=false;          //professore esce dal laboratorio
            if(this.profInAttesa>0){    //si dà la precedenza agli altri professori
                notifyAll();
            }
            else{
                prenotato=false;        //se non ci sono altri professori in attesa il professore comunica che il laboratorio non è più prenotato per l'uso dei prof
                notifyAll();
            }
        }
    }
    
    public void richiestaPCTesista(int i) throws InterruptedException{
        Computer pc = computers.get(i-1);       //computer che necessita al tesista
        synchronized(pc){
            while(pc.occupied==true){
                pc.tesistiInAttesa++;
                pc.wait();                //il tesista attende che il pc sia libero
                pc.tesistiInAttesa--;
            }
            pc.occupied=true;         // il tesista prenota il computer
        }
        synchronized(this){
            while(prenotato==true){
                wait();             //attende gli eventuali professori che devono utilizzare il laboratorio
            }
            presenze++;         //il tesista entra nel laboratorio
        }
        Thread.sleep(1000);         //il tesista utilizza il computer
        synchronized(this){
            presenze--;             //il tesista esce dal laboratorio
            if(presenze==0){
                notifyAll();        //se è l'ultimo ad uscire segnala ad un eventuale professore che può utilizzare il laboratorio
            }
        }
        synchronized(pc){
            pc.occupied=false;      //libera il pc utilizzato
            pc.notifyAll();
        }
    }
    
    public void richiestaPCStudente(int i) throws InterruptedException{
        Computer pc = computers.get(i-1);     //computer che necessita allo studente
        synchronized(pc){
            while(pc.occupied==true || pc.tesistiInAttesa>0){
                pc.wait();                    //attende che il pc sia libero e che non ci siano tesisti in attesa (i quali hanno la precedenza)
            }
            pc.occupied=true;                 //lo studente prenota il computer
        }
        synchronized(this){
            while(prenotato==true){
                wait();                       //attende che eventuali professori abbiano terminato di utilizzare il laboratorio
            }
            presenze++;             //lo studente entra nel laboratorio
        }
        Thread.sleep(500);          //lo studente utilizza il computer
        synchronized(this){
            presenze--;             //esce dal laboratorio
            if(presenze==0){
                notifyAll();        //se è l'ultimo ad uscire segnala ad un eventuale prof in attesa che può usufruire del laboratorio
            }
        }
        synchronized(pc){
            pc.occupied=false;      //lo stidente libera il pc utilizzato
            pc.notifyAll();
        }
    }
    
}
