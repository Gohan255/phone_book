package phonebook2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PhoneBookInterface extends Remote {
    void addContact(String name, ContactType type, String value) throws RemoteException;
    Contact getContact(String name) throws RemoteException;
    void removeContact(String name) throws RemoteException;
}

