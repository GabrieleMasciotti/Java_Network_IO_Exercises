public class Calcolatore implements Runnable{
    private final Contatore contatore;
    private final contoCorrente conto;
    private int numBonifici;
    private int numAccrediti;
    private int numBollettini;
    private int numF24;
    private int numPagamentiBancomat;
    public Calcolatore(contoCorrente c, Contatore cont){
        this.contatore = cont;
        this.conto = c;
        this.numBonifici = 0;
        this.numAccrediti = 0;
        this.numBollettini = 0;
        this.numF24 = 0;
        this.numPagamentiBancomat = 0;
    }

    @Override
    public void run() {
        Movimento mov;
        for(int i=0;i<conto.listaMovimenti.size();i++){         //conta le occorrenze di ogni causale per tutti i movimenti del conto
            mov=conto.listaMovimenti.get(i);
            if(mov.causale.equals("Bonifico")){this.numBonifici++;}
            if(mov.causale.equals("Accredito")){this.numAccrediti++;}
            if(mov.causale.equals("Bollettino")){this.numBollettini++;}
            if(mov.causale.equals("F24")){this.numF24++;}
            if(mov.causale.equals("PagoBancomat")){this.numPagamentiBancomat++;}
        }
        /* Aggiornamento del numero delle occorrenze di tutte le causali nel contatore globale condiviso. */
        this.contatore.incrBonifici(this.numBonifici);
        this.contatore.incrAccrediti(this.numAccrediti);
        this.contatore.incrBollettini(this.numBollettini);
        this.contatore.incrF24(this.numF24);
        this.contatore.incrPagamentiBancomat(this.numPagamentiBancomat);

    }
}
