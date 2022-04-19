package com.revature;
import com.revature.services.AuthService;
import com.revature.util.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Driver {

    public static void main(String[] args) {
        System.out.println("Driver has run main.");
        try {
            Connection conn = ConnectionFactory.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /************
     * TESTS
     ************/

}
