import java.io.IOException;
import java.net.*;

public class Client {

    public static void main(String[] args) throws IOException {
        String serverName = null;
        int serverPort = 0;
        if(args.length == 0){
            System.out.println("Passare da linea di comando nome e porta del server!");
            System.exit(1);
        }
        else{
            serverName = args[0];
            try{
                serverPort = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e){
                System.out.println("ERR -arg 2");
                System.out.println("Scegliere un valore intero compreso tra 1 e 65535.");
                System.exit(1);
            }
            System.out.println("Richiesta connessione al server: "+serverName+"/"+serverPort);
        }

        DatagramSocket client = new DatagramSocket();           //socket UDP del client
        client.setSoTimeout(2000);             //timeout di attesa del socket del client impostato a 2 secondi
        DatagramPacket pckSent = null;         //pacchetto per l'invio di dati
        DatagramPacket pckRec;          //pacchetto per la ricezione dei dati
        long initTStamp;
        byte [] buffer;
        long RTT;
        int pacchettiTrasmessi = 0;
        int pacchettiPersi = 0;
        long minRTT = 0;
        long maxRTT = 0;
        double sumRTT = 0;

        boolean ilPrimoPacchettoAndatoPerso = false;        //variabile settata a true se il primo pacchetto viene perso (permette il corretto calcolo del RTT minimo)
        for(int i=0;i<10;i++){
            initTStamp = System.currentTimeMillis();
            String msg = "PING "+i+" "+initTStamp;
            buffer = msg.getBytes();
            try {
                pckSent = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverName), serverPort);
            }
            catch (UnknownHostException e){
                System.out.println("ERR -arg 1");
                System.out.println("Host name sconosciuto!");
                System.exit(1);
            }
            catch(IllegalArgumentException e){
                System.out.println("ERR -arg 2");
                System.out.println("Scegliere un valore intero compreso tra 1 e 65535.");
                System.exit(1);
            }
            pacchettiTrasmessi++;
            client.send(pckSent);
            pckRec = new DatagramPacket(buffer, buffer.length);     //ricevo lo stesso messaggio nello stesso buffer
            try{
                client.receive(pckRec);
                RTT = System.currentTimeMillis() - initTStamp;
                if(i==0 || ilPrimoPacchettoAndatoPerso) {minRTT=RTT; ilPrimoPacchettoAndatoPerso=false;}
                if(RTT < minRTT) minRTT = RTT;
                if(RTT > maxRTT) maxRTT = RTT;
                sumRTT += RTT;
                System.out.println(msg+" RTT: "+RTT+" ms");
            }
            catch (SocketTimeoutException e){
                if(i==0) ilPrimoPacchettoAndatoPerso=true;
                pacchettiPersi++;
                System.out.println(msg+" RTT: *");
            }
        }

        int pacchettiRicevuti = pacchettiTrasmessi-pacchettiPersi;
        int percentuale = pacchettiPersi*10;
        double avgRTT = sumRTT/pacchettiRicevuti;
        System.out.println("\n PING Statistics...");
        System.out.println(pacchettiTrasmessi+" pacchetti trasmessi, "+pacchettiRicevuti+" pacchetti ricevuti, "+ percentuale+"% pacchetti persi");
        System.out.printf("Round-trip (ms) min/avg/max = %d/%.2f/%d\n",minRTT,avgRTT,maxRTT);

    }
}
