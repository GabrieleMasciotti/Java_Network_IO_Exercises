//package assignment2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SalaUfficio {
    private final ThreadPoolExecutor executor;
    public SalaUfficio(){
        this.executor = new ThreadPoolExecutor(4,4,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(8));   //thread pool con 4 thread (uno per ogni sportello) e coda d'attesa con massimo 8 posti (k=12)
    }
    public void executeTask(Cliente task){
        try{
            executor.execute(task);
        }
        catch(RejectedExecutionException e){
                System.out.printf("CLIENTE %d IN ATTESA DI ENTRARE NELLA SALA PICCOLA!\n",task.numero);
                while(executor.getQueue().size()==8){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(SalaUfficio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
                executor.execute(task);
        }
    }
    public void chiusuraUfficio(){
        executor.shutdown();
    }
    
}
