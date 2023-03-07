package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;
import java.sql.*;
public class Main
{
    public static String main()
    {
        final String DB_URL_2 = "jdbc:postgresql://localhost/mini_project";
        final String USER = "postgres";
        final String PASSWORD = "password";
        DBinit db = new DBinit();
        db.connect();
        Scanner sc = new Scanner(System.in);
        int Choice;
        while(true) {
            System.out.println("Enter 1 for Student, 2 for Faculty and 3 for Academic Office!");
            System.out.println("Enter 4 to exit");
            Choice = sc.nextInt();
            sc.nextLine();
            if (Choice < 1 || Choice > 4)
                System.out.println("Wrong Input");
            else if(Choice == 4)
                return "1";
            else {
                String Email_id, password;
                if (Choice == 1) {
                    try {
                        Connection connection_2 = DriverManager.getConnection(DB_URL_2, USER, PASSWORD);
                        System.out.print("Enter Email: ");
                        Email_id = sc.nextLine();
                        System.out.println();
                        System.out.print("Enter Password: ");
                        password = sc.nextLine();
                        System.out.println();
                        String sql = "select * from users WHERE email = ?;";
                        PreparedStatement ps = connection_2.prepareStatement(sql);
                        ps.setString(1, Email_id);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            if (!rs.getString("type").equals("student")) {
                                System.out.println("Invalid User Selection");
                            } else {
                                if (!rs.getString("email").equals(Email_id) || !rs.getString("password").equals(password)) {
                                    System.out.println("Wrong Email or Password!");
                                } else {
                                    Student ob = new Student();
                                    String sql1 = "select * from student where email = ?;";
                                    PreparedStatement ps1 = connection_2.prepareStatement(sql1);
                                    ps1.setString(1, Email_id);
                                    ResultSet rs1 = ps1.executeQuery();
                                    rs1.next();
                                    String entry_no = rs1.getString("entry_no");
                                    ob.Home(connection_2, entry_no);
                                    connection_2.close();

                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }

                } else if (Choice == 2) {
                    try {
                        Connection connection_2 = DriverManager.getConnection(DB_URL_2, USER, PASSWORD);
                        System.out.print("Enter Email: ");
                        Email_id = sc.nextLine();
                        System.out.println();
                        System.out.print("Enter Password: ");
                        password = sc.nextLine();
                        System.out.println();
                        String sql = "select * from users WHERE email = ?;";
                        PreparedStatement ps = connection_2.prepareStatement(sql);
                        ps.setString(1, Email_id);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            if (!rs.getString("type").equals("faculty")) {
                                System.out.println("Invalid User Selection");
                            } else {
                                if (!rs.getString("email").equals(Email_id) || !rs.getString("password").equals(password)) {
                                    System.out.println("Wrong Email or Password!");
                                } else {
                                    String sql1 = "select * from faculty where email = ?;";
                                    PreparedStatement ps1 = connection_2.prepareStatement(sql1);
                                    ps1.setString(1, Email_id);
                                    ResultSet rs1 = ps1.executeQuery();
                                    rs1.next();
                                    String faculty_id = rs1.getString("fac_id");
                                    Faculty ob = new Faculty();
                                    ob.Home(connection_2, faculty_id);

                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                } else {
                    try {
                        Connection connection_2 = DriverManager.getConnection(DB_URL_2, USER, PASSWORD);
                        System.out.print("Enter Email: ");
                        Email_id = sc.nextLine();
                        System.out.println();
                        System.out.print("Enter Password: ");
                        password = sc.nextLine();
                        System.out.println();
                        String sql = "select * from users WHERE email = ?;";
                        PreparedStatement ps = connection_2.prepareStatement(sql);
                        ps.setString(1, Email_id);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            if (!rs.getString("type").equals("acad")) {
                                System.out.println("Invalid User Selection");
                            } else {
                                if (!rs.getString("email").equals(Email_id) || !rs.getString("password").equals(password)) {
                                    System.out.println("Wrong Email or Password!");
                                } else {
                                    Acad ob = new Acad();
                                    ob.Home(connection_2);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
    }
    public static void main(String args[])
    {
        main();
    }
}