import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        System.out.println("Inserisci l'indirizzo del server con cui effettuare la connessione.");
        Scanner sc = new Scanner(new InputStreamReader(System.in));
        String serverIP = sc.nextLine();
        System.out.println("Inserisci il numero di porta desiderato.");
        int port = sc.nextInt();
        System.out.printf("Tentativo di connessione al server: %s : %d\n",serverIP,port);
        SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress(serverIP,port));
        ByteBuffer buffer = ByteBuffer.allocateDirect(2048);
        System.out.println("Connesso! Digitare le stringhe da inviare al server. Scrivere 'exit' per terminare.");
        ReadableByteChannel in = Channels.newChannel(System.in);
        WritableByteChannel out = Channels.newChannel(System.out);
        while(true){
            buffer.clear();
            in.read(buffer);            //lettura da tastiera della stringa da passare al server
            buffer.flip();
            if(buffer.limit()!=1){      //se la stringa non è vuota (non contiene solo il carattere new line) rimuove il new line
                buffer.limit(buffer.limit()-1);
            }

            byte[] arr = new byte[buffer.limit()];
            buffer.get(arr);            //ottiene un array di bytes contenuti nel buffer
            buffer.flip();

            if(new String(arr).equals("exit")){         //se passata la stringa 'exit' chiude la connessione col server e termina il programma
                System.out.println("Chiusura connessione...");
                clientChannel.close();
                System.out.println("Terminazione del programma.");
                in.close();
                out.close();
                break;
            }
            clientChannel.write(buffer);        //trasferimento tramite la rete della stringa al server
            buffer.clear();
            clientChannel.read(buffer);         //ricezione della risposta del server
            buffer.flip();
            System.out.print("Stringa ottenuta: ");
            out.write(buffer);          //visualizzazione, su std output del client, della risposta del server
        }
    }
}
