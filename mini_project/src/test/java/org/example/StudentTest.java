package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    static Student ob;
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
        ob = new Student();
    }
    @Test
    void register() throws SQLException {
        String input = "CS402\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Register(con,"2020csb1153");
        assertEquals(ret,"2020csb1153");

        input = "CS402\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Deregister(con,"2020csb1153");
        assertEquals(ret,"2020csb1153");

        input = "CS402\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Register(con,"2020csb1187");
        assertEquals(ret,"2020csb1187");



        input = "CS402\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Register(con,"2020csb1143");
        assertEquals(ret,"2020csb1143");

    }

    @Test
    void deregister() {

    }

    @Test
    void view_Grades() throws SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        int ret = ob.View_Grades(con,"2020csb1153");
        assertEquals(ret,1);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.View_Grades(con,"2020csb1317");
        assertEquals(ret,1);
    }

    @Test
    void show_CGPA() throws SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        double ret = ob.Show_CGPA(con,"2020csb1153");
        assertEquals(ret,7.0);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Show_CGPA(con,"2020csb1317");
        assertEquals(ret,2.6);
    }

    @Test
    void update_Profile() throws SQLException {
        String input = "Aman Kumar\n2020\n9546003567\n1\n2020csb1153\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Update_Profile(con,"2020csb1153");
        assertEquals(ret,"2020csb1153");
    }

    @Test
    void grad_Status() throws SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Grad_Status(con,"2020csb1153");
        assertEquals(ret,"Required Credits Not Reached");

        out =  new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Grad_Status(con,"2020csb1317");
        assertEquals(ret,"Required Credits Not Reached");

        out =  new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Grad_Status(con,"2020csb1198");
        assertEquals(ret,"Credits Required for Graduation Completed!");
    }

    @Test
    void home() throws SQLException {
        String input = "8\n7\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Home(con,"2020csb1153");
        assertEquals(ret,"2020csb1153");
    }
}