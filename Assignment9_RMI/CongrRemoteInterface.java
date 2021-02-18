import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CongrRemoteInterface extends Remote {
    String registerAtSession(int day, int session, String name) throws RemoteException;
    String getProgram(int day) throws RemoteException;
}
