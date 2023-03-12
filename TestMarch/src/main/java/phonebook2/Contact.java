package phonebook2;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Contact {

    private String name;
    private ContactType contactType;
    private String value;
}
