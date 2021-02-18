//package assignment3_lock;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Laboratorio {
    private final ArrayList <Computer> computers;       //struttura dati con l'insieme di tutti i pc del laboratorio
    private final ReentrantLock lockPrenotazione;
    private boolean prenotato;          //variabile che indica se il laboratorio è prenotato da un professore (inutilizzabile da studenti e tesisti)
    private int presenze;               //numero di utenti nel laboratorio in un dato istante
    private int profInAttesa;           //numero dei professori in attesa di accedere al lab in un dato instante
    private final Condition viaLiberaProf;          //variabile di condizione in cui i professori attendono che tutti escano dal lab prima di usufruirne
    private final Condition profInLab;              //variabile di condizione in cui studenti e tesisti attendono i professori che stanno usando il lab
    private final Condition profAttendeProf;        //variabile di condizione in cui i professori attendono altri professori
    public Laboratorio(){
        computers=new ArrayList <> ();
        lockPrenotazione=new ReentrantLock();
        prenotato=false;
        presenze=0;
        profInAttesa=0;
        viaLiberaProf=lockPrenotazione.newCondition();
        profInLab=lockPrenotazione.newCondition();
        profAttendeProf=lockPrenotazione.newCondition();
        for(int i=1;i<21;i++){
            computers.add(new Computer(i));
        }
    }
    
    public void prenotaLab()throws InterruptedException{
        lockPrenotazione.lock();
        try {
            while(prenotato==true){         //fintanto che il laboratorio è prenotato
                profInAttesa++;
                profAttendeProf.await();    //il professore si mette in attesa dell'altro professore che sta utilizzando il lab
                profInAttesa--;
            }
            prenotato=true;             //il prof prenota il laboratorio
            while(presenze>0){
                viaLiberaProf.await();      //attende che tutti escano
            }
        } finally {
            lockPrenotazione.unlock();
        }
        Thread.sleep(1500);           //il professore utilizza il laboratorio
        lockPrenotazione.lock();
        try{
            prenotato=false;
            if(profInAttesa>0){      //se ci sono prof in attesa gli dà la precedenza
                profAttendeProf.signal();   //il professore segnala di aver finito ad eventuali prof in attesa (I PROFESSORI HANNO LA PRECEDENZA SUGLI ALTRI UTENTI)
            }
            else{                   //altrimenti segnala a tutti gli altri che possono accedere
                profInLab.signalAll();      //segnala a tutti di aver terminato
            }
        }
        finally{lockPrenotazione.unlock();}
    }
    
    public void richiestaPCTesista(int i) throws InterruptedException{
        Computer pc = computers.get(i-1);     //computer che necessita al tesista
        pc.lock.lock();
        try{
            while(pc.occupied==true){
                pc.tesistiInAttesa++;
                pc.teWait.await();          //attende che il pc sia libero
                pc.tesistiInAttesa--;
            }
            pc.occupied=true;               //il tesista prenota il computer
        }
        finally{pc.lock.unlock();}
        
        lockPrenotazione.lock();   //prima di entrare ad utilizzare il computer il tesista controlla che un professore non voglia utilizzare il laboratorio
        try{
            while(prenotato==true){
                profInLab.await();      //attende un eventuale professore che sta utilizzando il laboratorio in quel momento
            }
            presenze++;         //il tesista entra nel laboratorio
        }
        finally{lockPrenotazione.unlock();}
        Thread.sleep(1000);                    //il tesista utilizza il computer
        lockPrenotazione.lock();
        try{
            presenze--;                 //esce dal laboratorio
            if(presenze==0) {viaLiberaProf.signal();}       //se è l'ultimo ad uscire segnala ad un eventuale prof in attesa che può usufruire del laboratorio
        }
        finally{lockPrenotazione.unlock();}
        pc.lock.lock();
        try{
            pc.occupied=false;          //libera il computer utilizzato
            if(pc.tesistiInAttesa>0){
                pc.teWait.signal();         //se ci sono altri tesisti in attesa, il tesista dà loro la precedenza rispetto agli studenti
            }
            else{
                pc.stWait.signal();         //altrimenti consente l'utilizzo del pc ad uno studente in attesa
            }
        }
        finally{pc.lock.unlock();}
    }
    
    public void richiestaPCStudente(int i) throws InterruptedException{
        Computer pc = computers.get(i-1);     //computer che necessita allo studente
        pc.lock.lock();
        try{
            while(pc.occupied==true){
                pc.stWait.await();          //attende che il pc sia libero
            }
            pc.occupied=true;               //lo studente prenota il computer
        }
        finally{pc.lock.unlock();}
        
        lockPrenotazione.lock();   //prima di entrare ad utilizzare il computer lo studente controlla che un professore non voglia utilizzare il laboratorio
        try{
            while(prenotato==true){
                profInLab.await();      //attende un eventuale professore che sta utilizzando il laboratorio in quel momento
            }
            presenze++;         //lo studente entra nel laboratorio
        }
        finally{lockPrenotazione.unlock();}
        Thread.sleep(500);                    //lo studente utilizza il computer
        lockPrenotazione.lock();
        try{
            presenze--;                 //esce dal laboratorio
            if(presenze==0) {viaLiberaProf.signal();}       //se è l'ultimo ad uscire segnala ad un eventuale prof in attesa che può usufruire del laboratorio
        }
        finally{lockPrenotazione.unlock();}
        pc.lock.lock();
        try{
            pc.occupied=false;          //libera il computer utilizzato
            if(pc.tesistiInAttesa>0){
                pc.teWait.signal();         //se ci sono tesisti in attesa, lo studente dà loro la precedenza rispetto agli altri studenti
            }
            else{
                pc.stWait.signal();         //altrimenti consente l'utilizzo del pc ad un altro studente in attesa
            }
        }
        finally{pc.lock.unlock();}
    }

}
