package com.revature.services;

import com.revature.exceptions.ReimbursementNotFoundException;
import com.revature.exceptions.ReimbursementUpdateFailedException;
import com.revature.exceptions.RoleAccessDeniedException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.repositories.ReimbursementDAO;

import java.util.List;
import java.util.Optional;

/**
 * The ReimbursementService should handle the submission, processing,
 * and retrieval of Reimbursements for the ERS application.
 *
 * {@code process} and {@code getReimbursementsByStatus} are the minimum methods required;
 * however, additional methods can be added.
 *
 * Examples:
 * <ul>
 *     <li>Create Reimbursement</li>
 *     <li>Update Reimbursement</li>
 *     <li>Get Reimbursements by ID</li>
 *     <li>Get Reimbursements by Author</li>
 *     <li>Get Reimbursements by Resolver</li>
 *     <li>Get All Reimbursements</li>
 * </ul>
 */
public class ReimbursementService {

    ReimbursementDAO rdao;

    public ReimbursementService() {
        this.rdao = new ReimbursementDAO();
    }

    /**
     * <ul>
     *     <li>Should ensure that the user is logged in as a Finance Manager</li>
     *     <li>Must throw exception if user is not logged in as a Finance Manager</li>
     *     <li>Should ensure that the reimbursement request exists</li>
     *     <li>Must throw exception if the reimbursement request is not found</li>
     *     <li>Should persist the updated reimbursement status with resolver information</li>
     *     <li>Must throw exception if persistence is unsuccessful</li>
     * </ul>
     *
     * Note: unprocessedReimbursement will have a status of PENDING, a non-zero ID and amount, and a non-null Author.
     * The Resolver should be null. Additional fields may be null.
     * After processing, the reimbursement will have its status changed to either APPROVED or DENIED.
     */
    public Reimbursement process(Reimbursement unprocessedReimbursement, Status finalStatus, User resolver) {
        if(resolver.getRole() != Role.FINANCE_MANAGER) {
            throw new RoleAccessDeniedException();
        }

        Reimbursement processedReimbursement = unprocessedReimbursement;
        processedReimbursement.setStatus(finalStatus);
        Optional<Reimbursement> queriedReimbursement = rdao.getById(unprocessedReimbursement.getId());
        if (!queriedReimbursement.isPresent()) {
            throw new ReimbursementNotFoundException();
        }

        processedReimbursement = rdao.update(processedReimbursement);
        if (processedReimbursement == null) { // persistence failed
            throw new ReimbursementUpdateFailedException();
        }
        return processedReimbursement;
    }

    public Reimbursement createReimbursement(Reimbursement newReimbursement) {
        return rdao.createReimbursement(newReimbursement);
    }

    public Reimbursement updateReimbursement(Reimbursement updatedReimbursement) {
        return rdao.update(updatedReimbursement);
    }

    // Should retrieve reimbursement with the ID given.
    public Optional<Reimbursement> getReimbursementByID(int id) {
        return rdao.getById(id);
    }

    /**
     * Should retrieve all reimbursements with the correct status.
     */
    public List<Reimbursement> getReimbursementsByStatus(Status status) {
        return rdao.getByStatus(status);
    }

    public List<Reimbursement> getReimbursementsByAuthor(User author) {
        return rdao.getByAuthor(author);
    }

    public List<Reimbursement> getReimbursementsByResolver(User resolver) {

        return rdao.getByAuthor(resolver);
    }
}
