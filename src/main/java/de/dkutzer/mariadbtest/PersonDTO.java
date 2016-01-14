package de.dkutzer.mariadbtest;

public class PersonDTO {

    String name;
    String lastName;
    int age;

    public PersonDTO(String name, String lastName, int age) {
        super();
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PersonDTO [name=");
        builder.append(name);
        builder.append(", lastName=");
        builder.append(lastName);
        builder.append(", age=");
        builder.append(age);
        builder.append("]");
        return builder.toString();
    }

}
