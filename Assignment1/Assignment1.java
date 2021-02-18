//package assignment1;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Assignment1 {
    
    public static void main(String[] args) {
        double accuracy;
        int waitingTime;
        System.out.println("Inserire accuratezza desiderata:");
        Scanner read = new Scanner(System.in);
        accuracy = read.nextDouble();
        System.out.println("Inserire numero di millisecondi di attesa massima:");
        waitingTime = read.nextInt();
        
        PiThread piCalculus = new PiThread(accuracy);
        Thread t = new Thread(piCalculus);
        t.start();
        
        try {
            t.join(waitingTime);
        } catch (InterruptedException ex) {
            Logger.getLogger(Assignment1.class.getName()).log(Level.SEVERE, null, ex);
        }
        t.interrupt();
    }
    
}

