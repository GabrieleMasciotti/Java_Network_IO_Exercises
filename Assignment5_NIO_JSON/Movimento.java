public class Movimento {
    public String data;
    public String causale;
    public Movimento(String data, String causale){
        this.data = data;
        this.causale = causale;
    }

    public Movimento(){}        //costruttore inserito per il corretto funzionamento della deserializzazione
}
