import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        CongrRemoteInterface congress;
        Remote remoteStub;
        Registry reg = LocateRegistry.getRegistry(6989);
        remoteStub = reg.lookup("Congress-MNG");            //stub research on registry
        congress = (CongrRemoteInterface) remoteStub;          //stub cast
        String out;
        String in;

        System.out.println("Accessing remote congress management!");
        System.out.println("Please enter commands:\n   Use 'register' to start a speaker registration;\n   use 'program' to get a day program;\n   use 'done' to terminate execution.");
        Scanner sc = new Scanner(System.in);
        in = sc.next();
        int day;
        int session;
        String name;
        while(!in.equals("done")){
            if(in.equals("register")){
                System.out.print("Insert the desidered day: ");
                day = sc.nextInt();
                System.out.print("Insert the session: ");
                session = sc.nextInt();
                System.out.print("Insert the speaker name: ");
                name = sc.next();
                System.out.println("Ok! Submitting to server...");
                out = congress.registerAtSession(day,session,name);     //remote method invocation to register the speaker
                System.out.println("SERVER SAYS:\n"+out);
            }
            else {
                if (in.equals("program")) {
                    System.out.print("Insert the day whose program is desidered: ");
                    day = sc.nextInt();
                    System.out.println("Ok! Submitting to server...");
                    out = congress.getProgram(day);                     //remote method invocation to get the program of the specified day
                    System.out.println("SERVER SAYS:\n" + out);
                } else {
                    if (in.equals("help")) {
                        System.out.println("Use 'register' to start a speaker registration; use 'program' to get a day program; use 'done' to terminate execution.");
                    } else {
                        System.out.println("ERROR! Command not recognised. Try again or type 'help'.");
                    }
                }
            }
            in = sc.next();
        }
        System.out.println("Shutting down...");
        sc.close();
    }
}
