//package assignment2;

import java.util.ArrayList;

public class Assignment2 {

    public static void main(String[] args) {
        Cliente task;
        SalaUfficio salaSportelli = new SalaUfficio();
        ArrayList <Cliente> salaAttesa = new ArrayList <>();        //lista che implementa la coda d'attesa dei clienti nella sala grande
        for(int i=1;i<51;i++){
            task = new Cliente(i);
            salaAttesa.add(task);       //tutti i clienti entrano nel supermercato (supponiamo che debbano essere serviti 50 clienti)
        }
        while(salaAttesa.size()>0){
            task = salaAttesa.remove(0);
            salaSportelli.executeTask(task);
        }
        salaSportelli.chiusuraUfficio();
    }   
}

