package phone.book.config.dao;

import phone.book.model.Contact;

public interface ContactDao {

    void save(Contact contact);

    void delete(Contact contact);

    void update(Contact contact);

    Contact load(Long id);

}
