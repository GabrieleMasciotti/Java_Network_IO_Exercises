//package assignment1;

public class PiThread implements Runnable{
    double accuracy;
    double pi;
    public PiThread(double accuracy){
        this.accuracy=accuracy;
    }
    
    @Override
    public void run() {
        pi = 4;
        double i = 1;
        while(true){
            pi = pi + (Math.pow(-1, i)*4)/(2*i+1);
            System.out.printf("Intermedio: %f.\n",pi);
            i++;
            if(Math.abs(pi-Math.PI)<accuracy){
                break;
            }
            if(Thread.interrupted()){
                break;
            }
        }
        System.out.printf("Finito di calcolare. Risultato: %f.\n",pi);
        
    }
    
}

