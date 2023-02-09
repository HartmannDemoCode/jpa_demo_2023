package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "car")
@NamedQueries({
        @NamedQuery(name = "Car.deleteAllRows", query = "delete from Car c")
})
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String brand;
    private String make;
    private int year;

    @ManyToMany(mappedBy = "cars")
    private Set<Person> persons = new LinkedHashSet<>();

    public Car(String brand, String make, int year) {
        this.brand = brand;
        this.make = make;
        this.year = year;
    }

    public Car() {
    }
    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "brand = " + brand + ", " +
                "make = " + make + ", " +
                "year = " + year + ")";
    }
}