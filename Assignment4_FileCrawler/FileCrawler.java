//package filecrawler;

import java.io.*;

public class FileCrawler {

    public static void main(String[] args) throws IOException, InterruptedException {
        String name;
        System.out.println("Digitare il pathname desiderato (può essere un qualsiasi path del vostro file system oppure semplicemente '.' per sfruttare l'esempio fornito) poi premere invio:");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            name = reader.readLine();                 //lettura da tastiera del path desiderato
        }
        System.out.printf("\nFilepath ottenuto: '%s'.\n\n",name);
        File D = new File(name);
        ListaCartelle lista = new ListaCartelle();
        Thread prod = new Thread(new Produttore(lista,D));
        Thread cons1 = new Thread(new Consumatore(lista));      //creazione di 3 thread consumatori (k=3)
        Thread cons2 = new Thread(new Consumatore(lista));
        Thread cons3 = new Thread(new Consumatore(lista));
        prod.start();
        cons1.start();
        cons2.start();
        cons3.start();
        prod.join();
        cons1.join();
        cons2.join();
        cons3.join();
    }
    
}
