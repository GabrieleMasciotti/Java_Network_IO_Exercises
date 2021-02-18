import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class TimeServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.print("Inserire l'ip del gruppo multicast:");
        Scanner sc = new Scanner(System.in);
        String ip = sc.nextLine();              //lettura indirizzo ip di multicast da std input
        sc.close();
        InetAddress dategroup = null;
        try {
            dategroup = InetAddress.getByName(ip);
            System.out.println("Invio di dati al gruppo: "+dategroup.getHostAddress());
        }
        catch (UnknownHostException e){
            System.out.println("Errore nell'inserimento dell'indirizzo ip! Host sconosciuto.");
            System.exit(1);
        }
        byte [] datehour;
        DatagramPacket pck;
        DatagramSocket socket = new DatagramSocket();       //socket per l'invio dei dati
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");     //formattatore di data e ora da inviare ai clients

        while(true){
            datehour = dtf.format(LocalDateTime.now()).getBytes();      //inserisce nel buffer di invio i bytes dell'ora e dalla data attuali formattati
            pck = new DatagramPacket(datehour,datehour.length,dategroup,12345);
            socket.send(pck);       //invio del pacchetto al gruppo di multicast
            System.out.println("Dati inviati: "+new String(pck.getData(),0, pck.getLength()));
            Thread.sleep(2500);     //attesa
        }
    }
}
