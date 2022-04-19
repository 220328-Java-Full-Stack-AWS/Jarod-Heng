package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;

import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ReimbursementDAO {
    /** Column Names in Database "reimbursements" - static implementation
     * reimbursement_id
     * status
     * author_username
     * resolver_username
     * amount
     * description
     * creation_date
     * resolution_date
     */
    private String RT_NAME = "reimbursements_db";
    private static String RT_ID = "reimbursement_id";
    private static String RT_STATUS = "status";
    private static String RT_AUTHOR = "author_username";
    private static String RT_RESOLVER = "resolver_username";
    private static String RT_AMOUNT = "amount";
    private static String RT_DESCRIPTION = "description";
    private static String RT_CREATIONDATE = "creation_date";
    private static String RT_RESOLUTIONDATE = "resolution_date";
    private static String RT_IMAGE = "";

    protected UserService userService = new UserService();

    /*******************************************************************
     * Constructors
     ******************************************************************/
    public ReimbursementDAO(String reimbursementsTableName) {
        this();
        // OVERRIDE THE PROPERTIES FILE FOR THE TABLE NAME
        this.createReimbursementTable(reimbursementsTableName);
    }

    public ReimbursementDAO() {
        populateDBProperties();
    }

    /*******************************************************************
     * Methods: CRUD
     * Create/Read/Update/Delete
     ******************************************************************/
    // CREATE Method
    public Reimbursement createReimbursement(Reimbursement rb) {
        String sql = "INSERT INTO " + RT_NAME
                                    + " (" + RT_STATUS + ","  + RT_AUTHOR + ","
                                    + "," + RT_RESOLVER + "," + RT_AMOUNT + "," + RT_DESCRIPTION + ","
                                    + RT_CREATIONDATE + "," + RT_RESOLUTIONDATE + ","
                                    +") VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, rb.getId());
            pstmt.setString(2, rb.getStatus().toString());
            pstmt.setString(3, rb.getAuthor().getUsername());
            pstmt.setString(4, rb.getResolver().getUsername());
            pstmt.setDouble(5, rb.getAmount());
            pstmt.setString(6, rb.getDescription());
            pstmt.setString(7, rb.getCreationDate().toString());
            pstmt.setString(8, rb.getResolutionDate().toString());
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            if(keys.next()) {
                int key = keys.getInt(1);
                rb.setId(key);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rb;
    }

    // READ Methods

    /**
     * Should retrieve a Reimbursement from the DB with the corresponding id or an empty optional if there is no match.
     */
    public Optional<Reimbursement> getById(int id) {
        Reimbursement reimbursement = new Reimbursement();
        try {
            String SQL = "SELECT * FROM " + RT_NAME + " WHERE " + RT_ID + " = ?";
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reimbursement.setId(rs.getInt(RT_ID));
                // TODO: change to handle enum instead of string
                reimbursement.setStatus(Status.valueOf(rs.getString(RT_STATUS)));

                // TODO: add handling of error related to getbyusername
                reimbursement.setAuthor(userService.getByUsername(rs.getString(RT_AUTHOR)).get());
                reimbursement.setResolver(userService.getByUsername(rs.getString(RT_RESOLVER)).get());

                reimbursement.setDescription(rs.getString(RT_DESCRIPTION));
                reimbursement.setCreationDate(Date.valueOf(rs.getString(RT_CREATIONDATE)));
                reimbursement.setResolutionDate(Date.valueOf(rs.getString(RT_RESOLUTIONDATE)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(reimbursement);
    }

    /**
     * Should retrieve a List of Reimbursements from the DB with the corresponding Status or an empty List if there are no matches.
     */
    public List<Reimbursement> getByStatus(Status status) {
        List<Reimbursement> reimbursementList = new ArrayList<>();
        try {
            // TODO: change test_table to name of table in DB
            String SQL = "SELECT * FROM " + RT_NAME + " WHERE " + RT_STATUS + " = ?";
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                Reimbursement rb = new Reimbursement();
                rb.setId(rs.getInt(RT_ID));
                // enum
                Status rbstatus = Status.valueOf(rs.getString(RT_STATUS).toUpperCase());
                rb.setStatus(status);

                // using the UserService class, we can get the user by the stored username.
                rb.setAuthor(userService.getByUsername(rs.getString(RT_AUTHOR)).get());
                rb.setResolver(userService.getByUsername(rs.getString(RT_RESOLVER)).get());

                rb.setAmount(rs.getDouble(RT_AMOUNT));
                rb.setDescription(rs.getString(RT_DESCRIPTION));
                rb.setCreationDate(rs.getDate(RT_CREATIONDATE));
                rb.setResolutionDate(rs.getDate(RT_RESOLUTIONDATE));
                // rb.setReceiptImageURL(rs.getString("receiptImageURL"));

                reimbursementList.add(rb);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reimbursementList;
    }

    /**
     * Should retrieve a List of ALL Reimbursements from the DB or an empty List if there are none.
     */
    public List<Reimbursement> getAll() {
        List<Reimbursement> list = new ArrayList<>();
        try {
            // TODO: change test_table to name of table in DB
            String SQL = "SELECT * FROM " + RT_NAME;
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            ResultSet rs = pstmt.executeQuery();

            /**
             * int id, Status status, User author, User resolver, double amount,
             * String description, Date creationDate, Date resolutionDate, String receiptImageURL)
             */
            while(rs.next()) {
                Reimbursement rb = new Reimbursement();
                rb.setId(rs.getInt(RT_ID));
                // enum
                Status rstatus = Status.valueOf(rs.getString(RT_STATUS).toUpperCase());
                rb.setStatus(rstatus);

                // using the UserService class, we can get the user by the stored username.
                rb.setAuthor(userService.getByUsername(rs.getString(RT_AUTHOR)).get());
                rb.setResolver(userService.getByUsername(rs.getString(RT_RESOLVER)).get());

                rb.setAmount(rs.getDouble(RT_AMOUNT));
                rb.setDescription(rs.getString(RT_DESCRIPTION));
                rb.setCreationDate(rs.getDate(RT_CREATIONDATE));
                rb.setResolutionDate(rs.getDate(RT_RESOLUTIONDATE));
                // rb.setReceiptImageURL(rs.getString("receiptImageURL"));

                list.add(rb);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // UPDATE Methods

    /**
     * <ul>
     *     <li>Should Update an existing Reimbursement record in the DB with the provided information.</li>
     *     <li>Should throw an exception if the update is unsuccessful.</li>
     *     <li>Should return a Reimbursement object with updated information.</li>
     * </ul>
     */
    public Reimbursement update(Reimbursement unprocessedReimbursement) {
        Reimbursement processedReimbursement;
        String sql =
                "UPDATE " + RT_NAME + " SET " +
                RT_ID + " = ?, " + //1
                RT_STATUS + " = ?, " + //2
                RT_AUTHOR + " = ?, " + //3
                RT_RESOLVER + " = ?, " + //4
                RT_AMOUNT + " = ?, " + //5
                RT_DESCRIPTION + " = ?, " + //6
                RT_CREATIONDATE + " = ? " + //7
                RT_RESOLUTIONDATE + " = ? " + //8
                " WHERE " + RT_ID + " = ?"; // 9
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            // ID
            pstmt.setInt(1, unprocessedReimbursement.getId());
            // Status
            pstmt.setString(2, unprocessedReimbursement.getStatus().toString());
            // Author
            pstmt.setString(3, unprocessedReimbursement.getAuthor().getUsername());
            // Resolver, if there is one
            pstmt.setString(4, unprocessedReimbursement.getResolver().getUsername());
            // Amount
            pstmt.setDouble(5, unprocessedReimbursement.getAmount());
            // Description
            pstmt.setString(6, unprocessedReimbursement.getDescription());
            // Creation Date
            pstmt.setDate(7, unprocessedReimbursement.getCreationDate());
            // Resolution Date
            pstmt.setDate(8, unprocessedReimbursement.getResolutionDate());
            // image
//            pstmt.setString(9, unprocessedReimbursement.getResolver().getImage());


            pstmt.setInt(9, unprocessedReimbursement.getId());

            pstmt.executeUpdate();
            processedReimbursement = getById(unprocessedReimbursement.getId()).get();

            return processedReimbursement;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // DELETE Methods
    /*
     * returns true if successful, false if failed to delete
     *
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM " + RT_NAME + " WHERE " + RT_ID + " = ?";
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

    public boolean delete(Reimbursement reimbursement) {
        return delete(reimbursement.getId());
    }


    /********************
     * Helper functions
     *
     ********************/
    // Method to read schema from reimbursements_table.properties
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
        RT_NAME = props.getProperty("RT_NAME");
        RT_ID = props.getProperty("RT_ID");
        RT_STATUS = props.getProperty("RT_STATUS");
        RT_AUTHOR = props.getProperty("RT_AUTHOR");
        RT_RESOLVER = props.getProperty("RT_RESOLVER");
        RT_AMOUNT = props.getProperty("RT_AMOUNT");
        RT_DESCRIPTION = props.getProperty("RT_DESCRIPTION");
        RT_CREATIONDATE = props.getProperty("RT_CREATIONDATE");
        RT_RESOLUTIONDATE = props.getProperty("RT_RESOLUTIONDATE");
        RT_RESOLUTIONDATE = props.getProperty("RT_IMAGE");
        return true;
    }

    // Returns true if the table was created or already exists
    private boolean createReimbursementTable(String tableName) {
        this.RT_NAME = tableName;
        String sql = "CREATE TABLE IF NOT EXISTS reimbursements_db(" +
                        RT_ID + " serial PRIMARY KEY," +
                        RT_STATUS + " varchar(20) NOT NULL," +
                        RT_AUTHOR + " varchar(50) NOT NULL," +
	                    RT_RESOLVER + " varchar(50)," +
	                    RT_AMOUNT + " numeric NOT NULL," +
	                    RT_DESCRIPTION + " varchar(500)," +
	                    RT_CREATIONDATE + " varchar(50) NOT NULL," +
	                    RT_RESOLUTIONDATE + " varchar(50) );";
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().getConnection().prepareStatement(sql);
            pstmt.setString(1, RT_NAME);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
