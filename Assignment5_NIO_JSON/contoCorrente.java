import java.util.ArrayList;

public class contoCorrente {
    public int idCorrentista;
    public ArrayList <Movimento> listaMovimenti = new ArrayList<>();

    public contoCorrente(int nome){
        this.idCorrentista = nome;
    }

    public contoCorrente(){}        //costruttore inserito per il corretto funzionamento della deserializzazione

    public void addMovement(Movimento mov){
        this.listaMovimenti.add(mov);
    }


}
