package com.revature.models;

import java.sql.Date;

/**
 * This concrete Reimbursement class can include additional fields that can be used for
 * extended functionality of the ERS application.
 *
 * Example fields:
 * <ul>
 *     <li>Description</li>
 *     <li>Creation Date</li>
 *     <li>Resolution Date</li>
 *     <li>Receipt Image</li>
 * </ul>
 *
 */
public class Reimbursement extends AbstractReimbursement {
    private String description;
    private Date creationDate;
    private Date resolutionDate;
    // private String receiptImageURL;
    // private Image ReceiptImage;

    public Reimbursement() {
        super();
    }

    /**
     * This includes the minimum parameters needed for the {@link com.revature.models.AbstractReimbursement} class.
     * If other fields are needed, please create additional constructors.
     */
    public Reimbursement(int id, Status status, User author, User resolver, double amount) {
        super(id, status, author, resolver, amount);
    }

    /**
     * This includes all the parameters needed for the full Reimbursement
     */
    public Reimbursement(int id, Status status, User author, User resolver, double amount,
                         String description, Date creationDate, Date resolutionDate, String receiptImageURL) {
        super(id, status, author, resolver, amount);
        this.description = description;
        this.creationDate = creationDate;
        this.resolutionDate = resolutionDate;
        //this.receiptImageURL = receiptImageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    /*
    public String getReceiptImageURL() {
        return receiptImageURL;
    }

    public void setReceiptImageURL(String receiptImageURL) {
        this.receiptImageURL = receiptImageURL;
    }
    */
}
