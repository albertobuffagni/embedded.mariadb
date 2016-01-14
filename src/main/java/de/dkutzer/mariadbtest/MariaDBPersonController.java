package de.dkutzer.mariadbtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.lambda.SQL;
import org.jooq.lambda.Unchecked;

public class MariaDBPersonController {
	private static Connection  connection ;
	public static void init() throws SQLException {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");
			createTablePersons();
	}

	public static void createTablePersons() throws SQLException {
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("CREATE TABLE  IF NOT EXISTS persons (id MEDIUMINT NOT NULL AUTO_INCREMENT, name varchar(20), lastname varchar(20), age integer,PRIMARY KEY (id))");
		stmt.close();
	}

	public static void close() throws SQLException {
		connection.close();
	}

	public static void insertPerson(PersonDTO person) throws SQLException {

		PreparedStatement prepareStatement = connection.prepareStatement("insert into persons(name,lastname,age) values (?,?,?)");
		prepareStatement.setString(1, person.name);
		prepareStatement.setString(2, person.lastName);
		prepareStatement.setInt(3, person.age);
		prepareStatement.execute();
	}

	public static List<PersonDTO> findAllPersons() throws SQLException {
		PreparedStatement prepareStatement = connection
				.prepareStatement("select name, lastname, age from persons");

		List<PersonDTO> result = SQL.seq(
				prepareStatement,
				Unchecked.function(
						rs -> new PersonDTO(
								rs.getString("name"), 
								rs.getString("lastname"), 
								rs.getInt("age"))))
								.collect(Collectors.toList());

		return result;
	}

	 public static List<PersonDTO> findPersonByName(String name) throws SQLException {
			PreparedStatement prepareStatement = connection
					.prepareStatement("select name, lastname, age from persons where name like ?");
			prepareStatement.setString(1, name);

			List<PersonDTO> result = SQL.seq(
					prepareStatement,
					Unchecked.function(
							rs -> new PersonDTO(
									rs.getString("name"), 
									rs.getString("lastname"), 
									rs.getInt("age"))))
									.collect(Collectors.toList());

			return result;
	 }

}
