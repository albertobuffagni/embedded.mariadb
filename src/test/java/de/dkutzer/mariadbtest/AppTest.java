package de.dkutzer.mariadbtest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import mockit.Deencapsulation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.ibatis.common.jdbc.ScriptRunner;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfiguration;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
	 private static Connection connection;
	 private static DB db; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
		configBuilder.setPort(0); //=> autom. detect free port
		DBConfiguration config = configBuilder.build();
		db = DB.newEmbeddedDB(config);
		db.start();
		db.createDB("test");
        connection = DriverManager.getConnection(configBuilder.getURL("test"), "root", "");
        Deencapsulation.setField(MariaDBPersonController.class, "connection", connection);
	
        String path = AppTest.class.getClassLoader().getResource("testMockDB.sql").getPath();
        ScriptRunner sr = new ScriptRunner(connection, false, false);
        Reader reader = new BufferedReader(new FileReader(path));
        sr.runScript(reader);
	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		connection.close();
	}



	@After
	public void tearDown() throws Exception {
		PreparedStatement prepareStatement = connection.prepareStatement("drop table persons");
		prepareStatement.execute();
	}

	@Before
	public void before() throws Exception {
		
		db.createDB("test");
		MariaDBPersonController.createTablePersons();

	}

	@org.junit.Test
	public void test() throws Exception {

		List<PersonDTO> allPersons = MariaDBPersonController.findAllPersons();

		MariaDBPersonController.insertPerson(new PersonDTO("Achim", "Menzel", 1));
		allPersons = MariaDBPersonController.findAllPersons();
		assertThat(allPersons).hasSize(3);

		MariaDBPersonController.insertPerson(new PersonDTO("Dietmar",
				"Wischmeyer", 2));
		allPersons = MariaDBPersonController.findAllPersons();
		assertThat(allPersons).hasSize(4);

		List<PersonDTO> findPersonByName = MariaDBPersonController
				.findPersonByName("Achim");
		assertThat(findPersonByName).hasSize(1);
		final PersonDTO personDTO = findPersonByName.get(0);
		assertThat(personDTO.lastName).isEqualTo("Menzel");
		assertThat(personDTO.name).isEqualTo("Achim");
		assertThat(personDTO.age).isEqualTo(1);

	}
}
