package phone.book.config;

import phone.book.enums.ContactType;
import phone.book.model.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(name = "nowak")
    public Contact getA(){
        return new Contact("Nowak", ContactType.E_MAIL, "nowak@gmail.com");
    }

    @Bean(name = "kowalski")
    public Contact getB(){
        return new Contact("Kowalski", ContactType.PHONE, "123-456-789");
    }

}
