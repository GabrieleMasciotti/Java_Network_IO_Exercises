import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Server {

    public static void main(String[] args) throws IOException {
        int porta;
        if(args.length!=0) porta = Integer.parseInt(args[0]);          //porta passata come parametro
        else porta = 6919;          //porta di default
        System.out.println("Porta selezionata per l'ascolto: "+porta);
        System.out.printf("LocalHost: %s.\nServer in attesa di richieste di connessione...\n", InetAddress.getLocalHost().getHostAddress());
        ServerSocketChannel serverChannel = ServerSocketChannel.open();     //apertura canale del server
        Selector selector = Selector.open();        //apertura selettore
        ServerSocket socket = serverChannel.socket();       //server socket associata al canale aperto precedentemente
        socket.bind(new InetSocketAddress(porta));          //collega la socket del server al local host alla porta specificata
        serverChannel.configureBlocking(false);             //canale del server impostato a non bloccante
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);   //registra il canale del server nel selettore con operazione di accept
        WritableByteChannel out = Channels.newChannel(System.out);  //canale di NIO per riportare in standard output il contenuto dei buffer
        while(true){
            System.out.println("Attesa di operazioni pronte.");
            int numCanali = selector.select();          //si blocca finché almeno uno dei canali non è pronto o il programma viene terminato
            if(numCanali==0) continue;
            Set <SelectionKey> readyKeys = selector.selectedKeys();     //ottiene un set di chiavi associate a canali che hanno operazioni pronte
            Iterator <SelectionKey> iter = readyKeys.iterator();
            while(iter.hasNext()){
                SelectionKey chiave = iter.next();      //estrazione della prima chiave
                iter.remove();          //rimozione della chiave dal set di chiavi pronte (Selected Set)
                try{ if(chiave.isAcceptable()){     //se si deve accettare una connessione
                    ServerSocketChannel server = (ServerSocketChannel) chiave.channel();
                    SocketChannel client = server.accept();         //canale associato al client
                    System.out.println("Accettata connessione dal client "+client.getRemoteAddress());
                    client.configureBlocking(false);                    //client settato a non bloccante
                    client.register(selector,SelectionKey.OP_READ);     //canale del client registrato nel selettore
                    }
                    else if(chiave.isReadable()){           //se il canale è pronto per essere letto
                        SocketChannel client = (SocketChannel) chiave.channel();    //recupero il canale del client
                        ByteBuffer buf = ByteBuffer.allocateDirect(2048);           //buffer di lettura
                        client.read(buf);           //lettura della stringa scritta dal client remoto
                        buf.flip();
                        if(!buf.hasRemaining()) {       //se la connessione è stata interrotta
                            System.out.println("Ottenuta stringa vuota o segnale di chiusura. Chiusura connessione del client "+client.getRemoteAddress()+"...");
                            chiave.cancel();            //cancellazione della chiave
                            client.close();             //chiusura del canale di comunicazione
                        }
                        else{
                            System.out.print("Lettura messaggio da parte del server: ");
                            out.write(buf);             //visualizzazione stringa letta, su std output del server
                            System.out.println();
                            buf.flip();
                            chiave.attach(buf);         //aggiunto il buffer di lettura come attachment della chiave
                            chiave.interestOps(SelectionKey.OP_WRITE);      //impostata operazione d'interesse della chiave (come scrittura)
                        }
                    }
                    else if(chiave.isWritable()){           //se il canale è pronto per ricevere la stringa di risposta del server
                        SocketChannel client = (SocketChannel) chiave.channel();
                        ByteBuffer reply = ByteBuffer.allocateDirect(2048);         //buffer di risposta
                        reply.put((ByteBuffer) chiave.attachment());                //mette nel buffer di risposta il contenuto dell'attachment della chiave (il messaggio ricevuto dal client)
                        reply.put("      ..echoed by server\n".getBytes());         //aggiunge al messaggio di risposta la stringa del server
                        reply.flip();
                        System.out.print("Messaggio rispedito dal server: ");
                        out.write(reply);                                           //visualizzazione della risposta su std output del server
                        reply.flip();
                        client.write(reply);                        //trasferimento della risposta al client per mezzo del canale
                        chiave.interestOps(SelectionKey.OP_READ);   //impostata operazione d'interesse della chiave (come lettura)
                    }
                }
                catch (IOException e){
                    chiave.cancel();
                    chiave.channel().close();
                }
            }
        }
    }
}
