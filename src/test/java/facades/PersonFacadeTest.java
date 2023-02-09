package facades;

import entities.Car;
import entities.Person;
import entities.Phone;
import entities.PhonePersonDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonFacadeTest {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("testpu");
    PersonFacade facade = new PersonFacade(emf);
    Car car1;
    Car car2;
    Person person1;
    Person person2;
    Person person3;
    Phone phone1;
    Phone phone2;
    Phone phone3;
    Phone phone4;

    @BeforeEach
    void setUp() {
        System.out.println("SETUP BeforeEach");
        EntityManager em = emf.createEntityManager();
        car1 = new Car("Volvo", "V70", 2000);
        car2 = new Car("Saab", "S100", 1998);
        person1 = new Person("Jesper", "1930-01-01", 100000.0);
        person2 = new Person("Jens", "1966-05-13", 150000.0);
        person3 = new Person("Jannie", "1961-03-23", 250000.0);
        phone1 = new Phone("10101010", "Home");
        phone2 = new Phone("20202020", "Work");
        phone3 = new Phone("30303030", "Work");
        phone4 = new Phone("40404040", "Home");
        person1.addPhone(phone1);
        person2.addPhone(phone2);
        person2.addPhone(phone3);
        person2.addPhone(phone4);
        person1.addCar(car1);
        person1.addCar(car2);
        person2.addCar(car1);
        try {

            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Car.deleteAllRows").executeUpdate();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.persist(car1);
            em.persist(car2);
            em.persist(person1);
            em.persist(person2);
            em.persist(person3);
            em.persist(phone1);
            em.persist(phone2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createPerson() {
    }

    @Test
    void getAllPersons() {
    }

    @Test
    void getPerson() {
    }

    @Test
    void updatePerson() {
    }

    @Test
    void deletePerson() {
    }

    @Test
    void getAllPeople() {
    }

    @Test
    void getPersonById() {
    }

    @Test
    void getPersonByPhone() {
        System.out.println("Get person by phone number");
        String expected = "Jesper";
        String actual = facade.getPersonByPhone("10101010").getName();
        assertEquals(expected, actual);
    }

    @Test
    void getPeopleByCar() {
        System.out.println("Get people by car");
        String expected = "Jesper";
        Set<Person> result = facade.getPeopleWithCar(car1);
        boolean test = result.stream().map(Person::getName).anyMatch(name -> name.equals(expected));
        assertTrue(test);
    }

    @Test
    void getPeopleAboveAvgSalary() {
        System.out.println("Get people above average age");
        String expected = "Jannie";
        Set<Person> result = facade.getPeopleAboveAvgSalary();
        System.out.println("result = " + result);
        boolean test = result.stream().map(Person::getName).filter(name -> name.equals(expected)).findFirst().isPresent();
        assertTrue(test);

    }

    @Test
    void getPeopleByBirthday() {
        System.out.println("Get people by birthday");
        String expected = "Jannie";
        Set<Person> result = facade.getPeopleByBirthday("1961-03-23");
        System.out.println("result = " + result);
        boolean test = result.stream().map(Person::getName).filter(name -> name.equals(expected)).findFirst().isPresent();
        assertTrue(test);
    }


    @Test
    void testGetAllPeople() {
    }

    @Test
    void testGetPersonById() {
    }

    @Test
    void testGetPeopleByBirthday() {
    }

    @Test
    void getPersonHighestSalary() {
    }

    @Test
    void getPhonePersonDTOs() {
        System.out.println("Get phone person DTOs");
        String expected = "Jesper";
        Set<PhonePersonDTO> result = facade.getPhonePersonDTOs();
        assertTrue(result.size()==4);
        result.stream().forEach(System.out::println);
    }
}