package com.revature.repositories;

import com.revature.models.User;
import com.revature.util.ConnectionFactory;

import java.io.InputStream;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;

public class UserDAO {

    /** Column Names in Database "users" - static implementation
     * user_id
     * username
     * password
     * role
     * first name
     * last name
     * email
     * phone
     */
    private String UT_TABLENAME = "users_db";
    private static String UT_ID = "user_id";
    private static String UT_USERNAME = "username";
    private static String UT_PASSWORD = "password";
    private static String UT_ROLE = "role";
    private static String UT_FIRSTNAME = "firstname";
    private static String UT_LASTNAME = "lastname";
    private static String UT_EMAIL = "email";
    private static String UT_PHONE = "phone";

    /*******************************************************************
     * Constructors
     ******************************************************************/
    public UserDAO(String userTableName) {
        this();
        // OVERRIDE THE PROPERTIES FILE FOR THE TABLE NAME
        this.UT_TABLENAME = userTableName;
    }

    public UserDAO() {
        populateDBProperties();
    }

    /**
     * <ul>
     *     <li>Should Insert a new User record into the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a User object with an updated ID.</li>
     * </ul>
     */
    public User create(User userToBeRegistered) {
        String sql = "INSERT INTO " + UT_TABLENAME
                + " (" + UT_ID + ","  + UT_USERNAME + ","  + UT_PASSWORD + ","
                + "," + UT_ROLE + "," + UT_FIRSTNAME + "," + UT_LASTNAME + ","
                + UT_EMAIL + "," + UT_PHONE + ","
                +") VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, userToBeRegistered.getId());
            pstmt.setString(2, userToBeRegistered.getUsername());
            pstmt.setString(3, userToBeRegistered.getPassword());
            pstmt.setString(4, userToBeRegistered.getRole().toString());
            pstmt.setString(5, userToBeRegistered.getFirstName());
            pstmt.setString(6, userToBeRegistered.getLastName());
            pstmt.setString(7, userToBeRegistered.getEmail());
            pstmt.setString(8, userToBeRegistered.getPhoneNumber());
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            if(keys.next()) {
                int key = keys.getInt(1);
                userToBeRegistered.setId(key);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userToBeRegistered;
    }
    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
    public Optional<User> getByUsername(String username) {
        User model = new User();
        try {
            String SQL = "SELECT * FROM users_db WHERE username = ?";
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();


//            while (rs.next()) {
//                model.setItemId(rs.getInt("item_id"));
//                model.setTask(rs.getString("task"));
//                model.setDate(rs.getString("due"));
//                model.setCompleted(rs.getBoolean("completed"));
//                model.setUserId(rs.getInt("user_id"));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        return model;
        return Optional.empty();
    }


    // HELPER METHODS
    private void populateDBProperties() {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("reimbursements_table.properties");
        try {
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Using defaults due to properties load failure.");
            // just use the defaults
            return;
        }

        UT_TABLENAME = props.getProperty("UT_TABLENAME");
        UT_ID = props.getProperty("UT_ID");
        UT_USERNAME = props.getProperty("UT_USERNAME");
        UT_PASSWORD = props.getProperty("UT_PASSWORD");
        UT_ROLE = props.getProperty("UT_ROLE");
        UT_FIRSTNAME = props.getProperty("UT_FIRSTNAME");
        UT_LASTNAME = props.getProperty("UT_LASTNAME");
        UT_EMAIL = props.getProperty("UT_EMAIL");
        UT_PHONE = props.getProperty("UT_PHONE");
    }

}
