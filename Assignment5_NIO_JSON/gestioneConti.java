import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;

public class gestioneConti {

    public static void main(String[] args) throws IOException, InterruptedException {
        int numeroConti = (int) ((Math.random() * 150) + 1);        //possiamo avere da 1 a 150 conti
        System.out.printf("Generati %d conti correnti.\n",numeroConti);
        File fileConti = new File("ContiCorrenti.json");   //creazione file con i conti correnti
        fileConti.createNewFile();
        ArrayList <contoCorrente> listaConti = new ArrayList<>();   //lista di conti correnti che verrà serializzata nel file
        int movimenti;
        int movimentiTot = 0;
        String [] causaliPossibili = {"Bonifico","Accredito","Bollettino","F24","PagoBancomat"};    //array di causali possibili per i movimenti
        Contatore contatore = new Contatore();          //contatore globale condiviso
        for(int i=0;i<numeroConti;i++){         //generazione dei conti correnti
            contoCorrente conto = new contoCorrente(i+1);             //crea un nuovo conto
            movimenti = (int) ((Math.random() * 250) + 1);          //possiamo avare tra 1 e 250 movimenti per ogni conto
            movimentiTot = movimentiTot + movimenti;
            int anno;
            int mese;
            int giorno;
            String data;
            for(int j=0;j<movimenti;j++){
                if( ((int) ((Math.random() * 2) + 1)) == 1 ){anno = 2019;}        //genera randomicamente un intero (1 o 2) in base al quale settare l'anno del movimento (2019 o 2020)
                else{anno = 2020;}
                mese = (int) ((Math.random() * 12) + 1);                          //genera random un mese dell'anno
                if(mese == 2 && anno == 2019){giorno = (int) ((Math.random() * 28) + 1);}         //se il mese selezionato è 2 (febbraio) e l'anno è 2019 allora genera un giorno tra 1 e 28
                else{
                    if(mese == 2 && anno == 2020){giorno = (int) ((Math.random() * 29) + 1);}         //se il mese selezionato è 2 (febbraio) e l'anno è 2020 (bisestile) allora genera un giorno tra 1 e 29
                    else{
                        if(mese == 1 || mese == 3 || mese == 5 || mese == 7 || mese == 8 || mese == 10 || mese == 12){giorno = (int) ((Math.random() * 31) + 1);}       //selezionato un mese con 31 giorni
                        else{giorno = (int) ((Math.random() * 30) + 1);}        //selezionato un mese con 30 giorni
                    }
                }
                data = giorno+"/"+mese+"/"+anno;        //setta la data del movimento
                Movimento mov = new Movimento(data,causaliPossibili[(int) (Math.random() * 5)]);         //crea un nuovo movimento
                conto.addMovement(mov);         //aggiunge il movimento al conto
            }
            listaConti.add(conto);              //aggiunge il conto alla lista da serializzare
        }
        System.out.printf("Movimenti totali: %d.\n",movimentiTot);

        ObjectMapper obj = new ObjectMapper();
        WritableByteChannel fileContiChannel = Channels.newChannel(new FileOutputStream(fileConti));    //apertura canale di NIO
        ByteBuffer bufferConti = ByteBuffer.wrap(obj.writeValueAsBytes(listaConti));                    //buffer wrapper che sfrutta la serializzazione in JSON
        while(bufferConti.hasRemaining()){
            fileContiChannel.write(bufferConti);            //scrittura dei dati del buffer (serializzazione objectMapper) nel file
        }
        fileContiChannel.close();       //chiusura canale

        Thread lettore = new Thread(new LettoreFile(contatore));     //creazione ed attivazione del thread lettore del file di conti correnti
        lettore.start();
        lettore.join();         //attende la terminazione del thread lettore (e quindi anche del thread pool di calcolatori)

        System.out.println("Esecuzione terminata!");
        System.out.printf("Numero totale di BONIFICI eseguiti: %d.\n",contatore.getNumeroBonifici());
        System.out.printf("Numero totale di ACCREDITI effettuati: %d.\n",contatore.getNumeroAccrediti());
        System.out.printf("Numero totale di BOLLETTINI pagati: %d.\n",contatore.getNumeroBollettini());
        System.out.printf("Numero totale di F24 compilati: %d.\n",contatore.getNumeroF24());
        System.out.printf("Numero totale di PAGAMENTI BANCOMAT effettuati: %d.\n",contatore.getNumeroPagamentiBancomat());
    }
}
