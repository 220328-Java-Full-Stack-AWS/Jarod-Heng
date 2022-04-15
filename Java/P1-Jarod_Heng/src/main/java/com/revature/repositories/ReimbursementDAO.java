package com.revature.repositories;

import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private String reimbursementsTableName = "reimbursements_db";
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

    public ReimbursementDAO(String reimbursementsTableName) {
        this.reimbursementsTableName = reimbursementsTableName;
    }

    public ReimbursementDAO() {
        populateDBProperties();
    }

    /**
     * Should retrieve a Reimbursement from the DB with the corresponding id or an empty optional if there is no match.
     */
    public Optional<Reimbursement> getById(int id) {
        Reimbursement reimbursement;
        try {
            String SQL = "SELECT * FROM " + reimbursementsTableName + " WHERE " + RT_ID + " = ?";
            reimbursement = new Reimbursement();

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
    //TODO: implement
    public List<Reimbursement> getByStatus(Status status) {
        List<Reimbursement> reimbursements = new ArrayList<>();



        if (reimbursements.isEmpty()) {
            return Collections.emptyList();
        }
        return reimbursements;
    }

    /**
     * Should retrieve a List of ALL Reimbursements from the DB or an empty List if there are none.
     */
    public List<Reimbursement> getAll() {
        List<Reimbursement> list = new LinkedList<>();
        try {
            // TODO: change test_table to name of table in DB
            String SQL = "SELECT * FROM " + reimbursementsTableName;
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            ResultSet rs = pstmt.executeQuery();

            /**
             * int id, Status status, User author, User resolver, double amount,
             * String description, Date creationDate, Date resolutionDate, String receiptImageURL)
             */
            while(rs.next()) {
                Reimbursement rb = new Reimbursement();
                rb.setId(rs.getInt("reimbursement_id"));
                // enum
                Status status = Status.valueOf(rs.getString("status").toUpperCase());
                rb.setStatus(status);

                // using the UserService class, we can get the user by the stored username.
                rb.setAuthor(userService.getByUsername(rs.getString("author")).get());
                rb.setResolver(userService.getByUsername(rs.getString("resolver")).get());

                rb.setAmount(rs.getDouble("amount"));
                rb.setDescription(rs.getString("description"));
                rb.setCreationDate(rs.getDate("creationDate"));
                rb.setResolutionDate(rs.getDate("resolutionDate"));
                // rb.setReceiptImageURL(rs.getString("receiptImageURL"));

                list.add(rb);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    /**
     * <ul>
     *     <li>Should Update an existing Reimbursement record in the DB with the provided information.</li>
     *     <li>Should throw an exception if the update is unsuccessful.</li>
     *     <li>Should return a Reimbursement object with updated information.</li>
     * </ul>
     */
    public Reimbursement update(Reimbursement unprocessedReimbursement) {
        Optional<Reimbursement> processedReimbursement;
        // TODO: verify reimbursement doesnt have null values?
        String sql =
                "UPDATE " + reimbursementsTableName + " SET " +
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
            processedReimbursement = getById(unprocessedReimbursement.getId());

            return processedReimbursement.get();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * returns true if successful, false if failed to delete
     *
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM " + reimbursementsTableName + " WHERE " + RT_ID + " = ?";
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

    // TODO: Implement
    public boolean delete(Reimbursement reimbursement) {
        return false;
    }



    public String getReimbursementsTableName() {
        return reimbursementsTableName;
    }

    public void setReimbursementsTableName(String reimbursementsTableName) {
        this.reimbursementsTableName = reimbursementsTableName;
    }

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
        RT_ID = props.getProperty("RT_ID");
        RT_STATUS = props.getProperty("RT_STATUS");
        RT_AUTHOR = props.getProperty("RT_AUTHOR");
        RT_RESOLVER = props.getProperty("RT_RESOLVER");
        RT_AMOUNT = props.getProperty("RT_AMOUNT");
        RT_DESCRIPTION = props.getProperty("RT_DESCRIPTION");
        RT_CREATIONDATE = props.getProperty("RT_CREATIONDATE");
        RT_RESOLUTIONDATE = props.getProperty("RT_RESOLUTIONDATE");
        RT_RESOLUTIONDATE = props.getProperty("RT_IMAGE");
    }
}
