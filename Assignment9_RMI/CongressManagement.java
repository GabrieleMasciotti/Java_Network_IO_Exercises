import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CongressManagement{

    public static void main(String[] args) throws RemoteException {
        Congress cong = new Congress();         //new congress object
        CongrRemoteInterface stub = (CongrRemoteInterface) UnicastRemoteObject.exportObject(cong,0);    //stub to be exported
        LocateRegistry.createRegistry(6989);
        Registry reg = LocateRegistry.getRegistry(6989);
        reg.rebind("Congress-MNG",stub);                    //registration of stub on the registry
        System.out.println("Congress management server ready!");
    }
}
