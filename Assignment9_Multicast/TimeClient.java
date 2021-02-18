import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class TimeClient {

    public static void main(String[] args) throws IOException {
        System.out.print("Inserire indirizzo ip del gruppo di multicast: ");
        Scanner sc = new Scanner(System.in);
        String ip = sc.nextLine();              //lettura indirizzo ip di multicast da std input
        sc.close();
        InetAddress dategroup = null;
        try {
            dategroup = InetAddress.getByName(ip);
            System.out.println("Join al gruppo: "+dategroup.getHostAddress());
        }
        catch (UnknownHostException e){
            System.out.println("Errore nell'inserimento dell'indirizzo ip! Host sconosciuto.");
            System.exit(1);
        }
        MulticastSocket ms = new MulticastSocket(12345);    //socket di ricezione dati dal gruppo (reuse impostata a true di default)
        ms.setSoTimeout(8000);      //timeout di attesa impostato di default a 8 secondi
        byte [] data = new byte[50];
        DatagramPacket pck = new DatagramPacket(data, data.length);
        ms.joinGroup(dategroup);        //unisce il client al gruppo di multicast
        for(int i=0;i<10;i++){
            try{
                ms.receive(pck);        //ricezione dei 10 messaggi dal server
            }
            catch (SocketTimeoutException e){       //se il client resta in attesa di dati per un periodo che va oltre il timer, termina
                System.out.println("Waiting time out! Server probably went down...\nTerminating.");
                ms.close();
                System.exit(2);
            }
            System.out.println("Dati ricevuti: "+new String(pck.getData(),0,pck.getLength()));
        }
        System.out.println("Done!");
        ms.close();
    }
}
