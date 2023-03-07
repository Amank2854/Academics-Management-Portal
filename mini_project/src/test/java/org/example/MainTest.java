package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    static Main ob;
    static DBinit db;
    static Connection con;

    @BeforeAll
    public static void init() throws Exception {
        final String DB_URL_2 = "jdbc:postgresql://localhost/mini_project";
        final String USER = "postgres";
        final String PASSWORD = "aman123456789000";
        try {
            con = DriverManager.getConnection(DB_URL_2, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db = new DBinit();
        db.connect();
        ob = new Main();
    }
    @Test
    void main() {
        String input = "1 \n2020csb1153@iitrpr.ac.in\n2020csb1153\n7\n1 \nfac@iitrpr.ac.in\nfac123\n1 \n2020csb1153@iitrpr.ac.in\n2020csb\n2 \nfac@iitrpr.ac.in\nfac123\n6\n2 \nacad@iitrpr.ac.in\nacad123\n2 \nfac@iitrpr.ac.in\nfac1234\n3 \nacad@iitrpr.ac.in\nacad123\n5\n3 \nfac@iitrpr.ac.in\nfac123\n3 \nacad@iitrpr.ac.in\nacad12\n4 \n\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = Main.main();
        assertEquals(ret,"1");
    }
}