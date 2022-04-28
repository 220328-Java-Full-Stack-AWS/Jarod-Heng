package com.revature;
import com.revature.exceptions.RegistrationUnsuccessfulException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.ReimbursementDAO;
import com.revature.repositories.UserDAO;
import com.revature.services.AuthService;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import com.revature.util.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class Driver {

    public static void main(String[] args) {
        UserDAOtest1(false);
//        UserDAOtest1(true);

//        UserServiceTest();

//        basicCreateReimbursement();
    }

    /************
     * TESTS
     ************/

    static void UserDAOtest1(boolean testDelete) {
        //ReimbursementDAO rdtest1 = new ReimbursementDAO("reimbursements_db_test1");

        UserDAO udtest1 = new UserDAO("user_db_test1");
        User testUserEM = new User(0, "udtest1", "password", Role.EMPLOYEE);
        User testUserFM = new User(0, "udtest2", "password", Role.FINANCE_MANAGER);
        try {
            testUserEM = udtest1.create(testUserEM);
            System.out.println(testUserEM);
        } catch (RegistrationUnsuccessfulException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
            testUserEM = udtest1.getByUsername(testUserEM.getUsername()).get();
            System.out.println(testUserEM);
        }

        try {
            testUserFM = udtest1.create(testUserFM);
            System.out.println(testUserFM);
        } catch (RegistrationUnsuccessfulException e) {
            System.out.println(e.getMessage());
            testUserFM = udtest1.getByUsername(testUserFM.getUsername()).get();
            System.out.println(testUserFM);
        }

        if(testDelete) {
            if (udtest1.delete(testUserEM.getId())) {
                System.out.println("Successful deletion.");
            } else {
                System.out.println("Delete failed.");
                udtest1.delete(testUserEM.getUsername());
            }

            if (udtest1.delete(testUserFM.getUsername())) {
                System.out.println("Successful deletion.");
            } else {
                System.out.println("Delete failed.");
            }

            if(UserDAO.deleteUserTable(udtest1.getUT_TABLENAME())) {
                System.out.println("TABLE SUCCESSFULLY DELETED");
            }
        }
    }

    static void UserServiceTest() {
        UserService us1 = new UserService();
        UserDAO ud = new UserDAO("testy_mctesterton");
        UserService us2 = new UserService(ud);

        User defaultEUser = new User(0, "username1", "password", Role.EMPLOYEE, "first", "last", "email1@email.com", "5555555555");
        User defaultFMUser = new User(0, "usernameF", "password", Role.FINANCE_MANAGER, "first", "last", "email2@email.com", "5555555555");

        User dEU1, dEU2, dFM1, dFM2;

        try {
            dEU1 = us1.createUser(defaultEUser);
            dFM1 = us1.createUser(defaultFMUser);
            dEU2 = us2.createUser(defaultEUser);
            dFM2 = us2.createUser(defaultFMUser);
        } catch (RuntimeException e) {
            System.out.println("users already created. probably.");
            dEU1 = us1.getByUsername(defaultEUser.getUsername()).get();
            dEU2 = us2.getByUsername(defaultEUser.getUsername()).get();
            dFM1 = us1.getByUsername(defaultFMUser.getUsername()).get();
            dFM2 = us2.getByUsername(defaultFMUser.getUsername()).get();
        }

        Optional<User> u1 = us1.getByID(1);
        if (u1.isPresent()) {
            System.out.println("User returned: " + u1);
        } else {
            System.out.println("User service 1 getByID failed");
        }

        Optional<User> u2 = us2.getByID(1);
        if (u2.isPresent()) {
            System.out.println("User returned: " + u2);
        } else {
            System.out.println("User service 1 getByID failed");
        }

        // should fail
        try {
            us2.changePassword("pw123", dEU1);
        } catch (RuntimeException e) {
            System.out.println("User " + dEU1 + " change password fail.");
        }
        try {
            us2.changePassword("pw123", dFM1);
        } catch (RuntimeException e) {
            System.out.println("User " + dEU2 + " change password fail.");
        }

        //should succeed
        try {
            dEU2 = us2.changePassword("pw123", dEU2);
            dFM2 = us2.changePassword("pw123", dFM2);
            System.out.println(dEU2);
            System.out.println(dFM2);
        } catch (RuntimeException e) {
            System.out.println("Uh oh.");
        }

    }

    static void basicCreateReimbursement() {
        ReimbursementService rs1 = new ReimbursementService();
        Reimbursement testReimb = new Reimbursement();
        try { // should fail cuz several null values
            rs1.createReimbursement(testReimb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * User Stories Tests - no postman
     *
     *
     */

}
