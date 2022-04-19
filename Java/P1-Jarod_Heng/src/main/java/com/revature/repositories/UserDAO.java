package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
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
                + " (" + UT_USERNAME + ","  + UT_PASSWORD + ","
                + "," + UT_ROLE + "," + UT_FIRSTNAME + "," + UT_LASTNAME + ","
                + UT_EMAIL + "," + UT_PHONE + ","
                +") VALUES (?,?,?,?,?,?,?)";

        

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
        User user = new User();
        try {
            String SQL = "SELECT * FROM " + UT_TABLENAME + " WHERE " + UT_USERNAME + " = ?";
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                user.setId(rs.getInt(UT_ID));
                user.setUsername(rs.getString(UT_USERNAME));
                user.setPassword(rs.getString(UT_PASSWORD));
                user.setRole(Role.valueOf(rs.getString(UT_ROLE)));
                user.setFirstName(rs.getString(UT_FIRSTNAME));
                user.setLastName(rs.getString(UT_LASTNAME));
                user.setEmail(rs.getString(UT_EMAIL));
                user.setPhoneNumber(rs.getString(UT_PHONE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public Optional<User> getByID(int id) {
        User user = new User();
        try {
            String SQL = "SELECT * FROM " + UT_TABLENAME + " WHERE " + UT_ID + " = ?";
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                user.setId(rs.getInt(UT_ID));
                user.setUsername(rs.getString(UT_USERNAME));
                user.setPassword(rs.getString(UT_PASSWORD));
                user.setRole(Role.valueOf(rs.getString(UT_ROLE)));
                user.setFirstName(rs.getString(UT_FIRSTNAME));
                user.setLastName(rs.getString(UT_LASTNAME));
                user.setEmail(rs.getString(UT_EMAIL));
                user.setPhoneNumber(rs.getString(UT_PHONE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(user);
    }

    // UPDATE
    public User updateUser(User user) {
        User processedUser;
        String sql =
                "UPDATE " + UT_TABLENAME + " SET " +
                        UT_ID + " = ?, " + //1
                        UT_USERNAME + " = ?, " + //2
                        UT_PASSWORD + " = ?, " + //3
                        UT_ROLE + " = ?, " + //4
                        UT_FIRSTNAME + " = ?, " + //5
                        UT_LASTNAME + " = ?, " + //6
                        UT_EMAIL + " = ? " + //7
                        UT_PHONE + " = ? " + //8
                        " WHERE " + UT_ID + " = ?"; // 9
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            // ID
            pstmt.setInt(1, user.getId());
            // Username
            pstmt.setString(2, user.getUsername());
            // Password
            pstmt.setString(3, user.getPassword());
            // Role
            pstmt.setString(4, user.getRole().toString());
            // First Name
            pstmt.setString(5, user.getFirstName());
            // Last Name
            pstmt.setString(6, user.getLastName());
            // Email
            pstmt.setString(7, user.getEmail());
            // Phone Number
            pstmt.setString(8, user.getPhoneNumber());
            // image
//            pstmt.setString(9, unprocessedReimbursement.getResolver().getImage());


            pstmt.setInt(9, user.getId());

            pstmt.executeUpdate();
            processedUser = getByID(user.getId()).get();

            return processedUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    // DELETE
    public boolean delete(int id) {
        String sql = "DELETE FROM " + UT_TABLENAME + " WHERE " + UT_ID + " = ?";
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String username) {
        String sql = "DELETE FROM " + UT_TABLENAME + " WHERE " + UT_USERNAME + " = ?";
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // HELPER METHODS
    // Returns true if properties file is read in successfully, false otherwise
    private boolean populateDBProperties() {
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("reimbursements_table.properties");
        try {
            props.load(input);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Using defaults due to properties load failure.");
            // just use the defaults
            return false;
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
        return true;
    }

    // Returns true if the table was created or already exists
    private boolean createUserTable(String tableName) {
        // TODO: Fix this
        String sql = "";
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            pstmt.setString(1, UT_TABLENAME);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
