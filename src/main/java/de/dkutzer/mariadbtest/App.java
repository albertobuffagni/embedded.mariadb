package de.dkutzer.mariadbtest;

import java.sql.SQLException;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
        try {
			MariaDBPersonController.init();
			MariaDBPersonController.insertPerson(new PersonDTO("John", "Lennon", 40));
			MariaDBPersonController.insertPerson(new PersonDTO("Paul", "McCartney", 73));
			MariaDBPersonController.insertPerson(new PersonDTO("Geroge", "Harrison", 58));
			MariaDBPersonController.insertPerson(new PersonDTO("Ringo", "Starr", 75));

			final List<PersonDTO> allPersons = MariaDBPersonController.findAllPersons();
			allPersons.forEach(p -> {
			    System.out.println(p);
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
}
