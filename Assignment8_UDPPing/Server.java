import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 0;
        if(args.length==0){
            System.out.println("Passare un numero di porta da linea di comando!");
            System.exit(1);
        }
        try {
            port = Integer.parseInt(args[0]);
            if(port<0 || port>65535){
                System.out.println("ERR -arg 1");
                System.out.println("Scegliere un valore intero compreso tra 1 e 65535.");
                System.exit(1);
            }
            System.out.println("Porta selezionata per il server: "+port);
            System.out.println("Indirizzo Ip del server: "+InetAddress.getLocalHost().getHostAddress());
        }
        catch(NumberFormatException e){
            System.out.println("ERR -arg 1");
            System.out.println("Scegliere un valore intero compreso tra 1 e 65535.");
            System.exit(1);
        }
        DatagramSocket server = new DatagramSocket(port);       //socket di connessione del server
        byte [] buffer = new byte[100];                         //buffer utilizzato per il trattamento del messaggio
        DatagramPacket pck = new DatagramPacket(buffer, buffer.length);
        int latency;
        int loss;

        while(true){
            System.out.println("Server pronto per ricevere richieste di PING...");
            server.receive(pck);        //si blocca finché non riceve un pacchetto sulla socket
            String msg = new String(pck.getData(),0,pck.getLength());
            loss = (int) ((Math.random()*4)+1);         //25% di probabilità (circa) di perdere i pacchetti
            if(loss == 1){
                System.out.println(pck.getAddress()+"/"+pck.getPort()+"> "+msg+" ACTION: PING non inviato.");
            }
            else{
                latency = (int) ((Math.random() * 2000)+1);             //generazione casuale latenza di rete simulata (da 1 a 2000 millisecondi)
                Thread.sleep(latency);          //simulazione latenza di rete
                server.send(pck);
                System.out.println(pck.getAddress()+"/"+pck.getPort()+"> "+msg+" ACTION: PING ritardato di "+latency+" millisecondi.");

            }
        }
    }
}
