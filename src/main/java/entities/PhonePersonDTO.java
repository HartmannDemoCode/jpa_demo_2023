package entities;

import java.time.LocalDateTime;

public class PhonePersonDTO {
    private String name;
    private java.time.LocalDateTime birthdate;
    private String phone;
    private String description;

    public PhonePersonDTO(String name, LocalDateTime birthdate, String phone, String description) {
        this.name = name;
        this.birthdate = birthdate;
        this.phone = phone;
        this.description = description;
    }

    public PhonePersonDTO() {
    }

    @Override
    public String toString() {
        return "PhonePersonDTO{" +
                "name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
