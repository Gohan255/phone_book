package phone.book.config.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Ejb3TransactionAnnotationParser;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import phone.book.model.Contact;

import javax.persistence.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Repository
public class ContactDaoImpl implements ContactDao {

    @PersistenceUnit
    private EntityManagerFactory factory;

    private final ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<>();


    private EntityManager getEntityManager() {
        EntityManager entityManager = entityManagerThreadLocal.get();
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
            entityManagerThreadLocal.set(entityManager);
        }
        return entityManager;
    }

   /* public void save(Contact contact){
        System.out.println("Thread: " + Thread.currentThread().getId());
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(contact);
        tx.commit();
        entityManager.close();
    }*/

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void save(Contact contact) {
        System.out.println("Thread: " + Thread.currentThread().getId());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CompletableFuture.runAsync(() -> {
            System.out.println("Thread: " + Thread.currentThread().getId());
            EntityManager entityManager = getEntityManager();
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                entityManager.persist(contact);
                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
            } finally {
                entityManager.close();
            }
        }, executorService).join();
        executorService.shutdown();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void update(Contact contact) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CompletableFuture.runAsync(() -> {
            System.out.println("Thread: " + Thread.currentThread().getId());
            EntityManager entityManager = getEntityManager();
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                Contact managedContact = entityManager.find(Contact.class, contact.getId());
                if (managedContact.getVersion() != contact.getVersion()) {
                    throw new OptimisticLockException();
                }
                entityManager.merge(contact);
                tx.commit();
            } catch (OptimisticLockException e) {
                if (tx != null && tx.isActive()) {
                    e.printStackTrace();
                    tx.rollback();
                }
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    e.printStackTrace();
                    tx.rollback();
                }
            } finally {
                entityManager.close();
            }
        }, executorService).join();
        executorService.shutdown();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void delete(Contact contact) {
        CompletableFuture.runAsync(() -> {
            EntityManager entityManager = getEntityManager();
            EntityTransaction tx = entityManager.getTransaction();
            try {
                tx.begin();
                Contact managedContact = entityManager.find(Contact.class, contact.getId(), LockModeType.PESSIMISTIC_WRITE);
                entityManager.remove(managedContact);
                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
            } finally {
                entityManager.close();
            }
        }).join();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Contact load(Long id) {
        EntityManager entityManager = factory.createEntityManager();
        Contact contact = entityManager.find(Contact.class, id);
        entityManager.close();
        return contact;
    }
}
