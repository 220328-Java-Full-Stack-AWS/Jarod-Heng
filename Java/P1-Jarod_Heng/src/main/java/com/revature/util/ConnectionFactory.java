package com.revature.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 * <p>This ConnectionFactory class follows the Singleton Design Pattern and facilitates obtaining a connection to a Database for the ERS application.</p>
 * <p>Following the Singleton Design Pattern, the provided Constructor is private, and you obtain an instance via the {@link ConnectionFactory#getInstance()} method.</p>
 */
public class ConnectionFactory {

    private static ConnectionFactory instance;
    private Connection connection;
    private ConnectionFactory() {
        super();
    }

    /**
     * <p>This method follows the Singleton Design Pattern to restrict this class to only having 1 instance.</p>
     * <p>It is invoked via:</p>
     *
     * {@code ConnectionFactory.getInstance()}
     */
    public static ConnectionFactory getInstance() {
        if(instance == null) {
            instance = new ConnectionFactory();
        }

        return instance;
    }

    /**
     * <p>The {@link ConnectionFactory#getConnection()} method is responsible for leveraging a specific Database Driver to obtain an instance of the {@link java.sql.Connection} interface.</p>
     * <p>Typically, this is accomplished via the use of the {@link java.sql.DriverManager} class.</p>
     */
    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*
        jdbc:postgresql://hostname:port/databaseName//?currentSchema=schemaName
        This is the string we need to use to connect to our database. We will build this string with each of the
        variables filled out and qualified.
        */

        /*
        Old File IO method to load properties
            Properties props = new Properties();
            FileReader fr = new FileReader("src/main/resources/application.properties");
            props.load(fr);
         */

        //New method grabbing the properties from the JAR classpath
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("application.properties");
        try {
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String connectionString = "jdbc:postgresql://" +
                props.getProperty("hostname") + ":" +
                props.getProperty("port") + "/" +
                props.getProperty("dbname");

        String username = props.getProperty("username");
        String password = props.getProperty("password");

        try {
            connection = DriverManager.getConnection(connectionString, username, password);
            System.out.println("Connection String: " + connectionString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}
