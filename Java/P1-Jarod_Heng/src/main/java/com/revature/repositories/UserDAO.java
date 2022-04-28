package com.revature.repositories;

import com.revature.exceptions.RegistrationUnsuccessfulException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionFactory;

import javax.swing.text.html.Option;
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
        UT_TABLENAME = userTableName;

        // OVERRIDE THE PROPERTIES FILE FOR THE TABLE NAME
        createUserTable(userTableName);
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
                + " (" + UT_USERNAME + ", "  + UT_PASSWORD + ", "
                + UT_ROLE + ", " + UT_FIRSTNAME + ", " + UT_LASTNAME + ", "
                + UT_EMAIL + ", " + UT_PHONE + ""
                +") VALUES (?,?,?,?,?,?,?)";
//        System.out.println("create method SQL string " + sql);

        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            pstmt.setInt(1, userToBeRegistered.getId());
            pstmt.setString(1, userToBeRegistered.getUsername());
            pstmt.setString(2, userToBeRegistered.getPassword());
            pstmt.setString(3, userToBeRegistered.getRole().toString());
            pstmt.setString(4, userToBeRegistered.getFirstName());
            pstmt.setString(5, userToBeRegistered.getLastName());
            pstmt.setString(6, userToBeRegistered.getEmail());
            pstmt.setString(7, userToBeRegistered.getPhoneNumber());
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            if(keys.next()) {
                int key = keys.getInt(1);
                userToBeRegistered.setId(key);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RegistrationUnsuccessfulException(e.getMessage());
        }

        return userToBeRegistered;
    }
    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
    public Optional<User> getByUsername(String username) {

        try {
            User user;
            String SQL = "SELECT * FROM " + UT_TABLENAME + " WHERE " + UT_USERNAME + " = ?";
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt(UT_ID));
                user.setUsername(rs.getString(UT_USERNAME));
                user.setPassword(rs.getString(UT_PASSWORD));
                user.setRole(Role.valueOf(rs.getString(UT_ROLE).toUpperCase()));
                user.setFirstName(rs.getString(UT_FIRSTNAME));
                user.setLastName(rs.getString(UT_LASTNAME));
                user.setEmail(rs.getString(UT_EMAIL));
                user.setPhoneNumber(rs.getString(UT_PHONE));
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> getByID(int id) {
        User user;
        try {
            String SQL = "SELECT * FROM " + UT_TABLENAME + " WHERE " + UT_ID + " = ?";
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            user = new User();

            while (rs.next()) {
                user.setId(rs.getInt(UT_ID));
                user.setUsername(rs.getString(UT_USERNAME));
                user.setPassword(rs.getString(UT_PASSWORD));
                user.setRole(Role.valueOf(rs.getString(UT_ROLE).toUpperCase()));
                user.setFirstName(rs.getString(UT_FIRSTNAME));
                user.setLastName(rs.getString(UT_LASTNAME));
                user.setEmail(rs.getString(UT_EMAIL));
                user.setPhoneNumber(rs.getString(UT_PHONE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> getByEmail(String emailAddress) {
        User user = null;
        try {
            String SQL = "SELECT * FROM " + UT_TABLENAME + " WHERE " + UT_EMAIL + " = ?";
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, emailAddress);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt(UT_ID));
                user.setUsername(rs.getString(UT_USERNAME));
                user.setPassword(rs.getString(UT_PASSWORD));
                user.setRole(Role.valueOf(rs.getString(UT_ROLE).toUpperCase()));
                user.setFirstName(rs.getString(UT_FIRSTNAME));
                user.setLastName(rs.getString(UT_LASTNAME));
                user.setEmail(rs.getString(UT_EMAIL));
                user.setPhoneNumber(rs.getString(UT_PHONE));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.ofNullable(user);
    }

    // UPDATE
    public Optional<User> updateUser(User user) {
        User processedUser;
        String sql =
                "UPDATE " + UT_TABLENAME + " SET " +
//                        UT_ID + " = ?, " + //1
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
//            pstmt.setInt(1, user.getId());
            // Username
            pstmt.setString(1, user.getUsername());
            // Password
            pstmt.setString(2, user.getPassword());
            // Role
            pstmt.setString(3, user.getRole().toString());
            // First Name
            pstmt.setString(4, user.getFirstName());
            // Last Name
            pstmt.setString(5, user.getLastName());
            // Email
            pstmt.setString(6, user.getEmail());
            // Phone Number
            pstmt.setString(7, user.getPhoneNumber());
            // image
//            pstmt.setString(9, unprocessedReimbursement.getResolver().getImage());

            pstmt.setInt(8, user.getId());

            pstmt.executeUpdate();
            // this may increase computational complexity and time needed to access
            // remove in future?
            processedUser = getByID(user.getId()).get();

            return Optional.of(processedUser);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // TODO CHANGE RETURN TYPES TO VOID IN DAO, let userservice handle returns with getuser functions
    public Optional<User> updatePassword(String password, User user) {
        Optional<User> processedUser;
        String sql =
                "UPDATE " + UT_TABLENAME + " SET " +
                        UT_PASSWORD + " = ?" + //3
                        " WHERE " + UT_ID + " = ?"; // 9
        System.out.println(sql);
        System.out.println("'" + password + "'");
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            // Password
            pstmt.setString(1, password);
            pstmt.setInt(2, user.getId());

            pstmt.executeUpdate();
            // this may increase computational complexity and time needed to access
            // remove in future?
            processedUser = getByID(user.getId());

            return processedUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> updateRole(Role newRole, User user) {
        User processedUser;
        String sql =
                "UPDATE " + UT_TABLENAME + " SET " +
                        UT_ROLE + " = ?, " + //3
                        " WHERE " + UT_ID + " = ?"; // 9
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            // Password
            pstmt.setString(1, newRole.toString());
            pstmt.setInt(2, user.getId());

            pstmt.executeUpdate();
            // this may increase computational complexity and time needed to access
            // remove in future?
            processedUser = getByID(user.getId()).get();

            return Optional.of(processedUser);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
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

    public boolean deleteByEmail(String email) {
        if (email == null)
            return false;

        String sql = "DELETE FROM " + UT_TABLENAME + " WHERE " + UT_EMAIL + " = ?";
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            pstmt.setString(1, email);
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
        createUserTable(this.UT_TABLENAME);
        Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("users_table.properties");
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
    public static boolean createUserTable(String tableName) {
//        UT_TABLENAME = tableName;
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" +
                        UT_ID + " serial PRIMARY KEY, " +
                        UT_USERNAME + " varchar(50) NOT NULL unique, " +
                        UT_PASSWORD + " varchar(50) NOT NULL, " +
	                    UT_ROLE + " varchar(30) NOT NULL, " +
	                    UT_FIRSTNAME + " varchar(50), " +
	                    UT_LASTNAME + " varchar(50), " +
	                    UT_EMAIL + " varchar(100) unique, " +
	                    UT_PHONE + " varchar(16) );";
        // System.out.println("Create User Table sql string: " + sql);
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
//            pstmt.setString(1, UT_TABLENAME);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUserTable(String tableName) {
        String sql = "drop table " + tableName + ";";
        System.out.println("Delete User Table sql string: " + sql);
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUT_TABLENAME() {
        return UT_TABLENAME;
    }

}
