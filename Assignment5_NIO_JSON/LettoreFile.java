import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LettoreFile implements Runnable{
    private final ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newCachedThreadPool();         //thread pool di thread che processano i conti correnti
    private final Contatore contatore;
    public LettoreFile(Contatore cont){
        this.contatore = cont;
    }

    @Override
    public void run() {
        File fileConti = new File("ContiCorrenti.json");
        ObjectMapper obj = new ObjectMapper();
        try {
            ReadableByteChannel ch = Channels.newChannel(new FileInputStream(fileConti));       //apertura canale di lettura
            ByteBuffer buf = ByteBuffer.allocateDirect(((int) fileConti.length())+100);         //allocazione buffer di dimensione (lunghezza del file da leggere + 100)
            int aux=0;
            while (aux != -1) {aux = ch.read(buf);}                     //lettura di tutto il file (salvato nel buffer)
            ch.close();                                                 //chiusura canale
            byte [] s = new byte[(int) fileConti.length()];
            buf.flip();
            buf.get(s);                                                 //trasferimento bytes dal buffer ad un array, poi utilizzato per la deserializzazione
            ArrayList<contoCorrente> listaConti = obj.readValue(s,new TypeReference<ArrayList<contoCorrente>>(){});         //deserializzazione con creazione di tipo generico per sottoclassi
            contoCorrente contoEstratto;
            while(listaConti.size() > 0){
                contoEstratto = listaConti.remove(0);                   //estrazione dei conti correnti dalla lista deserializzata
                exec.execute(new Calcolatore(contoEstratto,contatore));       //passa ogni conto ad un thread del thread pool
            }
            exec.shutdown();                                                  //chiusura thread pool
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);     //attesa terminazione del thread pool
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
