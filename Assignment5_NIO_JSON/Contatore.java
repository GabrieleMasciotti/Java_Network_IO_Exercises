public class Contatore {
    private int numeroBonifici;
    private int numeroAccrediti;
    private int numeroBollettini;
    private int numeroF24;
    private int numeroPagamentiBancomat;

    public Contatore(){
        this.numeroBonifici = 0;
        this.numeroAccrediti = 0;
        this.numeroBollettini = 0;
        this.numeroF24 = 0;
        this.numeroPagamentiBancomat = 0;
    }

    public synchronized void incrBonifici(int amount){
        this.numeroBonifici = this.numeroBonifici + amount;
    }

    public synchronized void incrAccrediti(int amount){
        this.numeroAccrediti = this.numeroAccrediti + amount;
    }

    public synchronized void incrBollettini(int amount){
        this.numeroBollettini = this.numeroBollettini + amount;
    }

    public synchronized void incrF24(int amount){
        this.numeroF24 = this.numeroF24 + amount;
    }

    public synchronized void incrPagamentiBancomat(int amount){
        this.numeroPagamentiBancomat = this.numeroPagamentiBancomat + amount;
    }

    public synchronized int getNumeroBonifici(){
        return this.numeroBonifici;
    }
    public synchronized int getNumeroAccrediti(){
        return this.numeroAccrediti;
    }
    public synchronized int getNumeroBollettini(){
        return this.numeroBollettini;
    }
    public synchronized int getNumeroF24(){
        return this.numeroF24;
    }
    public synchronized int getNumeroPagamentiBancomat(){
        return this.numeroPagamentiBancomat;
    }


}
