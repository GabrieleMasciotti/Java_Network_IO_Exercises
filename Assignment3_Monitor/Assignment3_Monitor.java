//package assignment3_monitor;

import java.util.ArrayList;
import java.util.Scanner;

public class Assignment3_Monitor {

    public static void main(String[] args) throws InterruptedException{
        Laboratorio lab = new Laboratorio();
        int professori;
        int studenti;
        int tesisti;
        System.out.println("Inserire numero professori:\n(Notare che, con le impostazioni attuali, se si inseriscono più di 6 professori questi utilizzano il laboratorio tutti per k volte prima di permettere agli altri utenti di accedere al lab.\nSe l'effetto è indesiderato, modificare i parametri di attesa delle entità nel programma.)");
        Scanner read = new Scanner(System.in);
        professori = read.nextInt();
        System.out.println("Inserire numero tesisti:");
        tesisti = read.nextInt();
        System.out.println("Inserire numero studenti:");
        studenti = read.nextInt();
        int k = (int) ((Math.random() * 4) + 1);            //numero degli accessi degli utenti (casuale tra 1 e 4)
        int i = (int) (Math.random()*20)+1;           //numero del pc ad uso dei tesisti
        
        System.out.println("INIZIO SIMULAZIONE!");
        System.out.printf("Numero accessi da parte degli utenti (k): %d.\n", k);
        System.out.printf("Computer ad utilizzo dei tesisti: %d.\n\n",i);
        
        ArrayList <Thread> prof = new ArrayList<>();
        for(int j=0;j<professori;j++){
            prof.add(new Thread(new Professore(lab,k)));
            prof.get(j).start();
        }
        ArrayList <Thread> tes = new ArrayList<>();
        for(int j=0;j<tesisti;j++){
            tes.add(new Thread(new Tesista(lab,k,i)));
            tes.get(j).start();
        }
        ArrayList <Thread> stud = new ArrayList<>();
        int indice;
        for(int j=0;j<studenti;j++){
            indice=(int) (Math.random()*20)+1;          //gli studenti scelgono un pc a caso
            stud.add(new Thread(new Studente(lab,k,indice)));
            stud.get(j).start();
        }
         
        for(int j=0;j<professori;j++){
            prof.get(j).join();
        }
        for(int j=0;j<tesisti;j++){
            tes.get(j).join();
        }
        for(int j=0;j<studenti;j++){
            stud.get(j).join();
        }
     
        System.out.println("FINE SIMULAZIONE!");
    }
    
}
