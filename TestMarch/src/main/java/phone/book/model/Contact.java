package phone.book.model;

import lombok.*;
import phone.book.enums.ContactType;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Contact {
    @Id
    @GeneratedValue
    private long id;

    @Version
    private long version;
    private String surname;
    @Enumerated(EnumType.STRING)
    private ContactType contactType;
    private String value;

    public Contact(String surname, ContactType contactType, String value) {
        this.surname = surname;
        this.contactType = contactType;
        this.value = value;
    }

    public Contact(long id, String surname, ContactType contactType, String value) {
        this.id = id;
        this.surname = surname;
        this.contactType = contactType;
        this.value = value;
    }
}
