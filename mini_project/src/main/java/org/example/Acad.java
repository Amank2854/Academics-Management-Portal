

package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Acad
{
    public String Edit_Catalog(Connection con) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Course ID of the course");
        String course_id = sc.nextLine();
        int L, T, P, C;
        String dept;
        System.out.println("Enter Lecture Hours (L)");
        L = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Tutorial Hours (T)");
        T = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Practical Hours (P)");
        P = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Credits Hours (C)");
        C = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Offering Department (CS,EE,ME,CE,CH,MC,MM)");
        dept = sc.nextLine();

        String sql = "insert into catalog values (?,?,?,?,?,?) on conflict(course_id) do update set L = ?, T = ?, P = ?, C = ?,dept = ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, course_id);
        ps.setInt(2, L);
        ps.setInt(3, T);
        ps.setInt(4, P);
        ps.setInt(5, C);
        ps.setString(6, dept);
        ps.setInt(7, L);
        ps.setInt(8, T);
        ps.setInt(9, P);
        ps.setInt(10, C);
        ps.setString(11, dept);

        ps.executeUpdate();

        System.out.println("Enter number of Pre-requisite");
        int count = sc.nextInt();
        sc.nextLine();

        String[] prereq = new String[count];
        for (int i = 0; i < count; i++) {
            System.out.println("Enter Course ID");
            prereq[i] = sc.nextLine();
        }

        for (int i = 0; i < count; i++) {
            sql = "insert into prerequisite values (?,?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, course_id);
            ps.setString(2, prereq[i]);
            ps.executeUpdate();
        }

        return "1";
    }

    public String View_Grades(Connection con) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the course ID to view grades");
        String course_id = sc.nextLine();

        ArrayList<String> student_id = new ArrayList<String>();
        ArrayList<String> grade = new ArrayList<String>();

        String sql = "select * from grades where course_id = ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, course_id);
        ResultSet rs = ps.executeQuery();

        while (rs.next())
        {
            student_id.add(rs.getString("student_id"));
            grade.add(rs.getString("grade"));
        }
        System.out.println("Student Grades are as Follows:");
        for(int i = 0 ; i<grade.size() ; i++)
        {
            System.out.println(student_id.get(i)+" - "+grade.get(i));
        }

        return "1";
    }

    public String Generate_Transcript(Connection con) throws IOException, SQLException {
        //Generate Trans
        Scanner sc = new Scanner(System.in);
        String filePath = new File("").getAbsolutePath() + "/data/transcript.txt";
        File file = new File(filePath);
        FileWriter fileWrite = new FileWriter(file);
        System.out.println("Enter the Entry No. of the Student");
        String entry = sc.nextLine();

        fileWrite.write(entry+"\n\n");

        String sql = "select course_id,grade from grades where student_id = ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, entry);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            fileWrite.write(rs.getString("course_id")+": "+rs.getString("grade")+"\n");
        }
        fileWrite.write("\n");

        sql = "select g.grade,cat.C from grades as g, catalog as cat where cat.course_id = g.course_id and g.student_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1, entry);
        rs = ps.executeQuery();
        double creds = 0, cumulative = 0;
        String gr;
        while (rs.next()) {
            int x = rs.getInt("C");
            creds += x;
            gr = rs.getString("grade");
            switch (gr) {
                case "A" -> cumulative += 10 * x;
                case "A-" -> cumulative += 9 * x;
                case "B" -> cumulative += 8 * x;
                case "B-" -> cumulative += 7 * x;
                case "C" -> cumulative += 6 * x;
                case "C-" -> cumulative += 5 * x;
                case "D" -> cumulative += 4 * x;
                case "E" -> cumulative += 3 * x;
            }
        }

        double cgpa = cumulative / creds;
        fileWrite.write("CGPA: "+Double.toString(cgpa)+"\n");

        System.out.println("Transcript Generated Successfully!");

        fileWrite.close();
        return "1";
    }

    public String Update_Sem(Connection con) throws SQLException {
        Scanner sc = new Scanner(System.in);

        String sql = "select * from event;";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();

        int sem = rs.getInt("sem");
        int year = rs.getInt("year");

        sem++;
        if(sem>2)
        {
            sem = 1;
            year++;
        }

        sql = "UPDATE event set sem = ?,year = ?;";
        ps = con.prepareStatement(sql);
        ps.setInt(1,sem);
        ps.setInt(2,year);
        ps.executeUpdate();

        sql = "select * from student;";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        while(rs.next())
        {
            int x = rs.getInt("credits");
            int y = rs.getInt("cred_prev");
            sql = "UPDATE student set credits = 0, cred_prev = ? , cred_prev_2 = ? where entry_no = ?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1,x);
            ps.setInt(2,y);
            ps.setString(3,rs.getString("entry_no"));
            ps.executeUpdate();
        }

        return "1";
    }

    public String Grad_Status(Connection con) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Entry no.");
        String entry = sc.nextLine();
        String op ;
        int core_reqd = 60;
        int elec_reqd = 30;
        String sql = "select * from curriculum where student_id = ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,entry);
        ResultSet rs = ps.executeQuery();
        rs.next();
        boolean flag = true;

        if(rs.getInt("core") < core_reqd || rs.getInt("elec") < elec_reqd)
        {
            flag = false;
        }

        sql = "select * from grades where student_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1,entry);
        rs = ps.executeQuery();
        while (rs.next())
        {
            if(rs.getString("grade").equals("F"))
            {
                flag = false;
            }
        }

        sql = "select  * from grades where student_id = ? AND course_id = 'CP301';";
        ps = con.prepareStatement(sql);
        ps.setString(1,entry);
        rs = ps.executeQuery();
        if(!rs.next())
            flag = false;
        else if(rs.getString("grade").equals("F"))
            flag = false;

        sql = "select  * from grades where student_id = ? AND course_id = 'CP302';";
        ps = con.prepareStatement(sql);
        ps.setString(1,entry);
        rs = ps.executeQuery();
        if(!rs.next())
            flag = false;
        else if(rs.getString("grade").equals("F"))
            flag = false;

        sql = "select  * from grades where student_id = ? AND course_id = 'CP303';";
        ps = con.prepareStatement(sql);
        ps.setString(1,entry);
        rs = ps.executeQuery();
        if(!rs.next())
            flag = false;
        else if(rs.getString("grade").equals("F"))
            flag = false;

        if(!flag)
        {
            System.out.println("Required Credits Not Reached");
            op = "Required Credits Not Reached";
        }
        else
        {
            System.out.println("Credits Required for Graduation Completed!");
            op = "Credits Required for Graduation Completed!";
        }

        return op;
    }
    public String Home(Connection con) throws SQLException, IOException {
        int ch;
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.println("1. Edit Course Catalog");
            System.out.println("2. View Grades of all Students");
            System.out.println("3. Generate Transcript");
            System.out.println("4. Update Semester");
            System.out.println("5. Logout");
            System.out.println("6. Check Graduation Status");
            System.out.print("Enter Your Choice:");
            ch = sc.nextInt();
            sc.nextLine();
            if (ch < 1 || ch > 6) {
                while (ch < 1 || ch > 6) {
                    System.out.println("Enter correct Choice");
                    ch = sc.nextInt();
                    sc.nextLine();
                }
            }
            if (ch == 1)
            {
                Edit_Catalog(con);
            }
            else if(ch == 2)
            {
                View_Grades(con);
            }
            else if(ch == 3)
            {
                Generate_Transcript(con);
            }
            else if(ch == 4)
            {
                Update_Sem(con);
            }
            else if(ch == 5)
                return "1";
            else
            {
                Grad_Status(con);
            }
        }
    }
}
