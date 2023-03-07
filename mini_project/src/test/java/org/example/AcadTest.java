package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AcadTest {

    static Acad ob;
    static DBinit db;
    static Connection con;

    @BeforeAll
    public static void init() throws Exception {
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
        ob = new Acad();
    }
    @Test
    void edit_Catalog() throws SQLException {
        String input = "CS518\n4\n1\n1\n3 \nCS\n1 \nCS201\n\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Edit_Catalog(con);
        assertEquals(ret,"1");
        String sql = "delete from catalog where course_id = ?;";
        PreparedStatement ps1 = con.prepareStatement(sql);
        ps1.setString(1,"CS518");
        ps1.executeUpdate();

        sql = "delete from prerequisite where course_id = ?;";
        ps1 = con.prepareStatement(sql);
        ps1.setString(1,"CS518");
        ps1.executeUpdate();

    }

    @Test
    void view_Grades() throws SQLException {
        String input = "CS101\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.View_Grades(con);
        assertEquals(ret,"1");
    }

    @Test
    void generate_Transcript() throws SQLException, IOException {
        String input = "2020csb1153\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Generate_Transcript(con);
        assertEquals(ret,"1");

        input = "2020csb1317\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out = new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Generate_Transcript(con);
        assertEquals(ret,"1");
    }

    @Test
    void update_Sem() throws RuntimeException, SQLException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        ArrayList<Integer> cred = new ArrayList<Integer>();
        ArrayList<Integer> cred_prev = new ArrayList<Integer>();
        ArrayList<Integer> cred_prev2 = new ArrayList<Integer>();
        ArrayList<String> entry = new ArrayList<String>();

        Scanner sc = new Scanner(System.in);
        try {


            String sql = "select * from student;";
            PreparedStatement ps1 = con.prepareStatement(sql);
            ResultSet rs = ps1.executeQuery();

            while(rs.next())
            {
                int x = rs.getInt("credits");
                int y = rs.getInt("cred_prev");
                int z = rs.getInt("cred_prev_2");
                cred.add(x);
                cred_prev.add(y);
                cred_prev2.add(z);
                entry.add(rs.getString("entry_no"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String ret = ob.Update_Sem(con);
        assertEquals(ret,"1");

        String sql = "select * from event;";
        PreparedStatement ps1 = con.prepareStatement(sql);
        ResultSet rs = ps1.executeQuery();
        rs.next();

        int sem = rs.getInt("sem");
        int year = rs.getInt("year");

        sem--;
        if(sem<=0)
        {
            sem = 2;
            year--;
        }

        sql = "UPDATE event set sem = ?,year = ?;";
        ps1 = con.prepareStatement(sql);
        ps1.setInt(1,sem);
        ps1.setInt(2,year);
        ps1.executeUpdate();

        sql = "select * from student;";
        ps1 = con.prepareStatement(sql);
        rs = ps1.executeQuery();
        int i = 0;
        while(rs.next())
        {
            sql = "UPDATE student set credits = ?, cred_prev = ? , cred_prev_2 = ? where entry_no = ?;";
            ps1 = con.prepareStatement(sql);
            ps1.setInt(1,cred.get(i));
            ps1.setInt(2,cred_prev.get(i));
            ps1.setInt(3,cred_prev2.get((i)));
            ps1.setString(4,entry.get(i));
            ps1.executeUpdate();
            i++;
        }

    }

    @Test
    void grad_Status() throws SQLException {
        String input = "2020csb1153\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Grad_Status(con);
        assertEquals(ret,"Required Credits Not Reached");

        input = "2020csb1317\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out =  new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Grad_Status(con);
        assertEquals(ret,"Required Credits Not Reached");

        input = "2020csb1198\n";
        in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        out =  new ByteArrayOutputStream();
        ps = new PrintStream(out);
        System.setOut(ps);

        ret = ob.Grad_Status(con);
        assertEquals(ret,"Credits Required for Graduation Completed!");
    }

    @Test
    void home() throws SQLException, IOException {
        String input = "7\n5 \n\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        String ret = ob.Home(con);
        assertEquals(ret,"1");
    }
}