package chapter06.joined;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity(name = "JoinedBook")
@Inheritance(strategy = InheritanceType.JOINED)
public class Book {
    // contents common to all Books go here
    @Id
    Long bookId;
    String title;
    // imagine many more
}
