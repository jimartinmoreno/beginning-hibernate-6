package chapter03.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class PersonTest {

    SessionFactory factory;

    @BeforeClass
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Test
    public void testSavePerson() {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Person person = new Person();
            person.setName("J. C. Smell");

            Long personId = (Long) session.save(person);
            assertNotNull(personId, "Person not saved correctly");
            System.out.println("personId = " + personId);

            tx.commit();

            Query<Person> query = session.createQuery("from Person p where p.name=:name", Person.class);
            query.setParameter("name", "J. C. Smell");
            person = query.uniqueResult();
            System.out.println("person = " + person);
            assertNotNull(person, "Person not saved correctly");

        }
    }
}
