package facades;

import entities.Car;
import entities.Person;
import entities.Phone;
import entities.PhonePersonDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonFacade implements ISelector{

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");

    public PersonFacade(EntityManagerFactory emf) {
          this.emf = emf;
    }

    public Person createPerson(Person p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            p.getCars().forEach(car -> {
                if(car.getId()==null)
                    em.persist(car);
                else
                    em.merge(car);
            });
            em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return p;
    }

    public Car createCar(Car c) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return c;
    }

    public Set<Person> getAllPersons(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p JOIN FETCH p.addresses", Person.class);
        Set<Person> persons = tq.getResultList().stream().collect(Collectors.toSet());
        return persons;
    }

    public Person getPerson(Long id){
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, id);
        return p;
    }

    public Person updatePerson(Person p){
        EntityManager em = emf.createEntityManager();
        Person found = em.find(Person.class, p.getId());
        if (found == null) {
            throw new IllegalArgumentException("No person with that id");
        }
        try {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return p;
    }

    public boolean addCarToPerson(Person person, Car car){
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, person.getId());
        if (p == null) {
            throw new IllegalArgumentException("No person with that id");
        }
        em.getTransaction().begin();
        if(car.getId()==null){
            em.persist(car);
        } else {
            car = em.find(Car.class, car.getId());
//            em.merge(car);
        }
//        car = em.find(Car.class, car.getId());

        p.addCar(car);
        em.getTransaction().commit();
        em.close();
        return true;
    }

    public Person deletePerson(Long id){
        EntityManager em = emf.createEntityManager();
        Person p = em.find(Person.class, id);
        if (p == null) {
            throw new IllegalArgumentException("No person with that id: " + id + " exists");
        }
        try {
            em.getTransaction().begin();
            em.remove(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return p;
    }

    @Override
    public Set<Person> getAllPeople() {
        return getAllPersons();
    }

    @Override
    public Person getPersonById(Long id) {
        return getPerson(id);
    }

    @Override
    public Person getPersonByPhone(String phoneNumber) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p JOIN p.phones ph WHERE ph.number = :number", Person.class);
        tq.setParameter("number", phoneNumber);
        Person p = tq.getSingleResult();
        return p;
    }

    @Override
    public Set<Person> getPeopleWithCar(Car car) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p JOIN p.cars c WHERE c.id = :id", Person.class);
        tq.setParameter("id", car.getId());
        Set<Person> persons = tq.getResultList().stream().collect(Collectors.toSet());
        return persons;
    }

    @Override
    public Set<Person> getPeopleByBirthday(String birthday) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.birthDate = :birthday", Person.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime birthdayDate = LocalDate.parse(birthday, formatter).atStartOfDay();
        tq.setParameter("birthday", birthdayDate);
        Set<Person> persons = tq.getResultList().stream().collect(Collectors.toSet());
        return persons;
    }

    @Override
    public Person getPersonHighestSalary() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.salary = (SELECT MAX(p.salary) FROM Person p)", Person.class);
        Person p = tq.getSingleResult();
        return p;
    }

    @Override
    public Set<Person> getPeopleAboveAvgSalary() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> tq = em.createQuery("SELECT p FROM Person p WHERE p.salary > (SELECT AVG(p.salary) FROM Person p)", Person.class);
        Set<Person> persons = tq.getResultList().stream().collect(Collectors.toSet());
        return persons;
    }

    @Override
    public Set<PhonePersonDTO> getPhonePersonDTOs() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<PhonePersonDTO> tq = em.createQuery("SELECT new entities.PhonePersonDTO(p.name,p.birthDate, ph.number, ph.description) FROM Person p JOIN p.phones ph", PhonePersonDTO.class);
        Set<PhonePersonDTO> persons = tq.getResultList().stream().collect(Collectors.toSet());
        return persons;
    }

    public static void main(String[] args) {
        PersonFacade pf = new PersonFacade(Persistence.createEntityManagerFactory("pu"));
        Person p = new Person("Jesper", "1968-01-01 00:00");
        Person h = new Person("Henriette", "1968-01-01 00:00");
        Car car1 = new Car("Jeep", "V70", 2000);
        car1 = pf.createCar(car1);
        System.out.println("car1 = " + car1);
        p.addAddress("Hovedgaden 1");
        Phone phone = new Phone("12345678", "Home");
        p.addPhone(phone);
        p.addCar(new Car("Volvo", "V70", 2000));
        Person person = pf.createPerson(p);
        Car car2 = new Car("Saab", "AA", 2000);
        pf.addCarToPerson(person, car1);
        pf.addCarToPerson(person, car2);

//        System.out.println("The person got this new id: " + p.getId());
//        pf.getAllPersons().forEach((person)-> System.out.println(person));
//        System.out.println("GET SINGLE PERSON BY ID:");
//        Person p = pf.getPerson(1L);
//        System.out.println(p);
//        p.setName("Jannie");
//        pf.updatePerson(p);
//        System.out.println("");
//        pf.getAllPersons().forEach((person)-> System.out.println(person));
//        System.out.println("DELETE PERSON WITH ID 1");
//        pf.deletePerson(1L);
        pf.getAllPersons().forEach((element)-> System.out.println(element));
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        TypedQuery<Person> tq = em.createNamedQuery("Person.deleteById", Person.class);
//        tq.setParameter("id", 1L);
//        tq.executeUpdate();
//        em.getTransaction().commit();
//        em.close();
    }
}
