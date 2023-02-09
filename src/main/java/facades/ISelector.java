package facades;

import entities.Car;
import entities.Person;
import entities.PhonePersonDTO;

import java.util.Set;

public interface ISelector {
    Set<Person> getAllPeople();
    Person getPersonById(Long id);
    Person getPersonByPhone(String phoneNumber);
    Set<Person> getPeopleWithCar(Car car);
    Set<Person> getPeopleByBirthday(String birthday);
    Person getPersonHighestSalary();
    Set<Person> getPeopleAboveAvgSalary();
    Set<PhonePersonDTO> getPhonePersonDTOs();

}
