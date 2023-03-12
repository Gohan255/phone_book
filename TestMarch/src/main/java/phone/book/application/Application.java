package phone.book.application;

import phone.book.enums.ContactType;
import phone.book.model.Contact;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import phone.book.config.dao.ContactDao;


@ComponentScan(basePackages = {"phone.book"})
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);

        ContactDao contactDao = ctx.getBean(ContactDao.class);

        Contact contact = new Contact("asd", ContactType.PHONE, "Asd");
        Contact contact2 = new Contact("asdasd", ContactType.PHONE, "Asd");

        contactDao.save((Contact) ctx.getBean("nowak"));
        contactDao.save(contact);

        contact.setSurname("ZMEINONY");

        contactDao.update(contact);
        contactDao.update(contact2);

        contactDao.delete((Contact) ctx.getBean("nowak"));
        contactDao.delete((Contact) ctx.getBean("nowak"));


        ctx.close();
    }
}
