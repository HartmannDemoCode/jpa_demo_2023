package entities;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "person")
@NamedQueries({
        @NamedQuery(name = "Person.deleteById", query = "delete from Person p where p.id = :id"),
        @NamedQuery(name = "Person.deleteAllRows", query = "delete from Person p")
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "birth_date", nullable = false)
//    @Temporal(TemporalType.TIMESTAMP) // not needed, but it's good to be explicit.
    private LocalDateTime birthDate;

    @Transient // not persisted because it is derived from birthDate.
    private Integer age;

    @ElementCollection
    @Column(name = "address")
    @CollectionTable(name = "person_addresses", joinColumns = @JoinColumn(name = "owner_id") )
    private Set<String> addresses = new HashSet<>();

    @OneToMany(mappedBy = "person", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Phone> phones = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "person_cars",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "cars_id"))
    private Set<Car> cars = new LinkedHashSet<>();

    @Column(name = "created", updatable = false)
    private LocalDateTime created;

    @Column(name = "modified")
    private LocalDateTime editted;

    @Column(name = "salary")
    private Double salary;

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    // Database is not set to handle dates, so I do it with JPA lifecycle methods. For more see: https://www.baeldung.com/jpa-entity-lifecycle-events
    @PreUpdate
    public void onUpdate() {
        editted = LocalDateTime.now(ZoneId.of("GMT+01:00"));
    }

    @PrePersist
    public void onPersist(){
        editted = LocalDateTime.now(ZoneId.of("GMT+01:00"));
        created = LocalDateTime.now(ZoneId.of("GMT+01:00"));
    }

    public Person() { }
    public Person(String name) {
        this.name = name;
    }
    public Person(String name, String birthDate) {
        this.name = name;
        setBirthDate(birthDate);
        setAge();
    }
    public Person(String name, String birthDate, Double salary) {
        this.name = name;
        setBirthDate(birthDate);
        setAge();
        this.salary = salary;
    }

    public Set<String> getAddresses() {
        return addresses;
    }

    public void addAddress(String address) {
        this.addresses.add(address);
    }

    public void removeAddress(String address) {
        for (String a : this.addresses)
            if (a == address)
                this.addresses.remove(a);
    }
    public Set<Car> getCars() {
        return cars;
    }

    public void addCar(Car car) {
        this.cars.add(car);
        car.getPersons().add(this);
    }
    public void removeCar(Car car) {
        this.cars.remove(car);
        car.getPersons().remove(this);
    }

    public Integer getAge() {
        return age;
    }

    @PostLoad
    public void setAge( ) {
        java.time.Duration duration = java.time.Duration.between(this.birthDate, LocalDateTime.now());
        this.age = (int) duration.toDays() / 365;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void addPhone(Phone phone) {
        this.phones.add(phone);
        phone.setPerson(this);
    }

    public void removePhone(Phone phone) {
        this.phones.remove(phone);
        phone.setPerson(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate bd = LocalDate.parse(birthDate, formatter);
        this.birthDate = bd.atStartOfDay();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "birthDate = " + birthDate + ", " +
                "age = " + this.age + ", " +
                "addresses = " + addresses + ")";
    }
}