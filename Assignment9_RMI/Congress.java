import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.ArrayList;
import java.util.Iterator;

public class Congress  extends RemoteServer implements CongrRemoteInterface {
private static final long serialVersionUID = 1L;
    ArrayList <Session> day1;
    ArrayList <Session> day2;
    ArrayList <Session> day3;

    public Congress(){
        this.day1 = new ArrayList<>(12);
        this.day2 = new ArrayList<>(12);
        this.day3 = new ArrayList<>(12);
        for(int i=0;i<12;i++){
            this.day1.add(new Session());
            this.day2.add(new Session());
            this.day3.add(new Session());
        }
    }

    @Override
    public String registerAtSession(int day, int session, String name) throws RemoteException {             //allows the registration of a speaker
        if(day<1 || day>3){return "Choose a correct day, between 1 and 3.";}
        if(session<1 || session>12){return "Choose a correct session to register to, between 1 and 12.";}
        if(day==1){
            if(day1.get(session-1).speakers.size()==5){return "Session max number of speakers reached. Try with another.";}
            this.day1.get(session-1).speakers.add(name);        //registration of the speaker
            return "Speaker "+name+" successfully added to session "+session+" of congress day "+day+". "+(day1.get(session-1).speakers.indexOf(name)+1)+"° speech assigned.";
        }
        if(day==2){
            if(day2.get(session-1).speakers.size()==5){return "Session max number of speakers reached. Try with another.";}
            this.day2.get(session-1).speakers.add(name);        //registration of the speaker
            return "Speaker "+name+" successfully added to session "+session+" of congress day "+day+". "+(day2.get(session-1).speakers.indexOf(name)+1)+"° speech assigned.";
        }
        if(day3.get(session-1).speakers.size()==5){return "Session max number of speakers reached. Try with another.";}
        this.day3.get(session-1).speakers.add(name);        //registration of the speaker
        return "Speaker "+name+" successfully added to session "+session+" of congress day "+day+". "+(day3.get(session-1).speakers.indexOf(name)+1)+"° speech assigned.";
    }

    @Override
    public String getProgram(int day) throws RemoteException {                  //allows to get the program of a day
        if(day<1 || day>3){return "Choose a correct day, between 1 and 3.";}
        if(day==1) {
            StringBuilder response = new StringBuilder("Congress Day 1 program:\n");
            Iterator<Session> iter = this.day1.iterator();
            int i = 1;
            int j = 1;
            while (iter.hasNext()) {
                response.append("   Session ").append(i).append(":");
                for (String s : iter.next().speakers) {
                    response.append("Speaker ").append(j).append(": ").append(s).append(" ");
                    j++;
                }
                i++;
                response.append("\n");
            }
            return new String(response);
        }
        if(day==2){
            StringBuilder response = new StringBuilder("Congress Day 2 program:\n");
            Iterator<Session> iter = this.day2.iterator();
            int i = 1;
            int j = 1;
            while (iter.hasNext()) {
                response.append("   Session ").append(i).append(":");
                for (String s : iter.next().speakers) {
                    response.append("Speaker ").append(j).append(": ").append(s).append(" ");
                    j++;
                }
                i++;
                response.append("\n");
            }
            return new String(response);
        }
        StringBuilder response = new StringBuilder("Congress Day 3 program:\n");
        Iterator<Session> iter = this.day3.iterator();
        int i = 1;
        int j = 1;
        while (iter.hasNext()) {
            response.append("   Session ").append(i).append(":");
            for (String s : iter.next().speakers) {
                response.append("Speaker ").append(j).append(": ").append(s).append(" ");
                j++;
            }
            i++;
            response.append("\n");
        }
        return new String(response);
    }

}
