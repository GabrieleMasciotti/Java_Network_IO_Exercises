import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Inserire il numero di porta desiderato (intero tra 0 e 65535): ");
        Scanner sc;
        sc = new Scanner(System.in);
        int port = sc.nextInt();                //letta una porta da standard input
        System.out.printf("LocalHost per effettuare la connessione: %s\n\n", InetAddress.getLocalHost().getHostAddress());     //stampa dell'host per effettuare la connessione
        System.out.println("USAGE: utilizzare preferibilmente lo stesso browser per le richieste; richiedere file con nomi che non contengano spazi.\n");
        ServerSocket listen = new ServerSocket(port);
        InputStream in;
        OutputStream out;
        DataOutputStream dataOut;
        while(true){
            System.out.println("Server ready! Waiting for new query...");
            Socket sock = listen.accept();                  //server in attesa di ricevere richieste di connessione
            System.out.println("Connection accepted!");

            in = sock.getInputStream();
            out = sock.getOutputStream();
            dataOut = new DataOutputStream(out);

            sc = new Scanner(in);
            String fileName = sc.nextLine();                        //lettura della query HTTP

            System.out.printf("Query: %s\n",fileName);
            int end = fileName.indexOf("HTTP");
            int beg = fileName.indexOf("/");
            if(end != -1 && beg != -1){fileName = fileName.substring(beg,end-1);}       //parsing del path del file richiesto

            File file = new File(fileName);
            byte [] byteArray = new byte [(int) file.length()];                     //array di bytes da trasferire al browser
            System.out.printf("File path: %s\n",file.getAbsolutePath());

            try(FileInputStream convert = new FileInputStream(fileName)){
                convert.read(byteArray);            //conversione del file in uno stream di bytes
                dataOut.writeBytes("HTTP/1.1 200 OK\n");        //risposte del server al browser
                dataOut.writeBytes("Connection: close\n");
                dataOut.writeBytes("Content-Type: \n\n");
                dataOut.write(byteArray,0,(int) file.length());         //trasferimento bytes del file sulla rete
            }
            catch(FileNotFoundException e){
                String error = "HTTP/1.1 404 Not Found\n\n";
                dataOut.writeBytes(error);                          //invio messaggio di errore
                dataOut.writeBytes("404 Not Found");
                System.out.println("File not found!");
            }
            sock.close();           //chiusura connessione non persistente
        }
    }
}
