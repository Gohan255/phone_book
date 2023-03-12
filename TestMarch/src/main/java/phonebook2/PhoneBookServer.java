package phonebook2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhoneBookServer extends UnicastRemoteObject implements PhoneBookInterface {

    private static final long serialVersionUID = 1L;
    private ConcurrentHashMap<String, Contact> contacts = new ConcurrentHashMap<>();
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    protected PhoneBookServer() throws RemoteException {
        super();
    }

    @Override
    public void addContact(String name, ContactType type, String value) throws RemoteException {
        executor.submit(() -> {
            Contact contact = new Contact(name, type, value);
            contacts.put(name, contact);
        });
    }

    @Override
    public Contact getContact(String name) throws RemoteException {
        return contacts.get(name);
    }

    @Override
    public void removeContact(String name) throws RemoteException {
        executor.submit(() -> {
            contacts.remove(name);
        });
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            PhoneBookServer phoneBookServer = new PhoneBookServer();
            registry.rebind("PhoneBookServer", phoneBookServer);
            System.out.println("PhoneBookServer is running.");
        } catch (Exception e) {
            System.err.println("PhoneBookServer exception: " + e.getMessage());
            e.printStackTrace();
        }

    }
}