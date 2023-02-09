package facades;

import entities.Person;
import entities.Phone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Populator {
    public void populate() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Person p1 = new Person("Jesper", "1991-01-01");
            Person p2 = new Person("Jannie", "2011-01-01");
            Person p3 = new Person("Holger", "2001-01-01");
            Phone ph1 = new Phone("10101010", "Home");
            Phone ph2 = new Phone("87654321", "Work");
            Phone ph3 = new Phone("54545455", "Home");
            Phone ph4 = new Phone("43956656", "Home");
            p1.addPhone(ph1);
            p2.addPhone(ph2);
            p3.addPhone(ph3);
            p3.addPhone(ph4);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        Populator pop = new Populator();
        pop.populate();
    }
}
