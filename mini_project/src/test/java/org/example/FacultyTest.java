package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FacultyTest {

    static Faculty ob;
    static DBinit db;
    static Connection con;

    @BeforeAll
    public static void init()
    {
        final String DB_URL_2 = "jdbc:postgresql://localhost/mini_project";
        final String USER = "postgres";
        final String PASSWORD = "password";
        try {
            con = DriverManager.getConnection(DB_URL_2, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        db = new DBinit();
        db.connect();
        ob = new Faculty();
    }

    @Test
    void register() throws SQLException {
        String input = "CS503\n4.0 \n\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Register(con,"fac");
        assertEquals(ret,"fac");

        input = "CS503\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        Student st = new Student();
        st.Register(con,"2020csb1153");

        input = "CS503\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Deregister(con,"fac");
        assertEquals(ret,"fac");

        input = "CS406\n0.0 \n\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Register(con,"fac");
        assertEquals(ret,"fac");


        input = "CS406\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Deregister(con,"fac");
        assertEquals(ret,"fac");
    }

    @Test
    void deregister() {
    }

    @Test
    void view_Grades() throws SQLException {
        String input = "CS101\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.View_Grades(con,"fac");
        assertEquals(ret,"fac");
    }

    @Test
    void upload_Grades() throws SQLException, IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Upload_Grades(con,"fac");
        assertEquals(ret,"fac");
    }

    @Test
    void update_Profile() throws SQLException {
        String input = "fac\nCS\n9876543210\n2020-08-01\n1\nfac123\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Update_Profile(con,"fac");
        assertEquals(ret,"fac");
    }

    @Test
    void home() throws SQLException, IOException {
        String input = "7\n6\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Home(con,"fac");
        assertEquals(ret,"fac");
    }
}