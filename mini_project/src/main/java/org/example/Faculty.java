/* TODO
*   Offered for Batch and Branch Specific
*   Success Message
*   Register */


package org.example;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.util.*;
import java.sql.*;
import java.io.*;
public class Faculty
{
    public String Register(Connection con, String fac_id) throws SQLException {
        Scanner sc = new Scanner(System.in);
        String sql = "select * from catalog;";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next())
        {
            System.out.println(rs.getString("course_id"));
        }
        System.out.println("Enter the Course ID you want to register");
        String course_id = sc.nextLine();


        System.out.println("Enter CGPA criteria if any else enter 0");
        float cg_criteria = sc.nextFloat();
        sc.nextLine();

        if (cg_criteria != 0) {
            sql = "insert into criteria values (?,?);";
            ps = con.prepareStatement(sql);
            ps.setString(1, course_id);
            ps.setFloat(2, cg_criteria);
            ps.executeUpdate();
        }

        sql = "insert into offerings values (?,?);";
        ps = con.prepareStatement(sql);
        ps.setString(1,course_id);
        ps.setString(2,fac_id);
        ps.executeUpdate();

        System.out.println("Course Registered Successfully!");

        return fac_id;
    }

    public String Deregister(Connection con, String fac_id) throws SQLException {
        Scanner sc = new Scanner(System.in);
        //Deregister Course!
        System.out.println("Enter the course you want to deregister");
        String course_id = sc.nextLine();

        ArrayList<String> student_id = new ArrayList<String>();

        String sql = "select * from registrations where course_id = ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,course_id);
        ResultSet rs = ps.executeQuery();
        while (rs.next())
        {
            student_id.add(rs.getString("student_id"));
        }

        for (String s : student_id) {
            sql = "delete from registrations where course_id = ? AND student_id = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, course_id);
            ps.setString(2, s);
            ps.executeUpdate();

            sql = "select credits from student where entry_no = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, s);
            rs = ps.executeQuery();
            rs.next();
            int cred = rs.getInt("credits");

            sql = "select C from catalog where course_id = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1, course_id);
            rs = ps.executeQuery();
            rs.next();
            int c = rs.getInt("C");
            cred = cred - c;

            sql = "UPDATE student SET credits = ? where entry_no = ?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1, cred);
            ps.setString(2, s);
            ps.executeUpdate();

            sql = "select * from courses where course_id = ? AND branch = 'CS';";
            ps = con.prepareStatement(sql);
            ps.setString(1,course_id);
            rs = ps.executeQuery();

            boolean is_core = true;

            if(!rs.next())
            {
                is_core = false;
            }

            sql = "select * from curriculum where student_id = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1,s);
            rs = ps.executeQuery();
            rs.next();
            int core = rs.getInt("core");
            int elec = rs.getInt("elec");
            if(is_core)
            {
                core -=c;
            }
            else
            {
                elec-=c;
            }
            sql = "UPDATE curriculum SET core = ? , elec = ? where student_id = ?;";
            ps = con.prepareStatement(sql);
            ps.setInt(1,core);
            ps.setInt(2,elec);
            ps.setString(3,s);
            ps.executeUpdate();
        }

        sql = "delete from offerings where course_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1,course_id);
        ps.executeUpdate();

        sql = "delete from criteria where course_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1,course_id);
        ps.executeUpdate();

        System.out.println("Course Removed Successfully!");

        return fac_id;
    }

    public String View_Grades(Connection con, String fac_id) throws SQLException {
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

        return fac_id;
    }

    public String Upload_Grades(Connection con, String fac_id) throws SQLException, IOException {
        //Update Grades
        String filePath = new File("").getAbsolutePath() + "/data/grades.csv";
        File file = new File(filePath);
        String line = "";
        String splitBy = ",";
        ArrayList<String[]> grades = new ArrayList<String[]>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null)   //returns a Boolean value
        {
            String[] details = line.split(splitBy);
            grades.add(details);
        }
        int n = grades.size();
        for(int i = 0 ; i<n ; i++)
        {
            String sql = "insert into grades values (?,?,?) on conflict(course_id,student_id) do update set grade = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,grades.get(i)[1]);
            ps.setString(2,grades.get(i)[0]);
            ps.setString(3,grades.get(i)[2]);
            ps.setString(4,grades.get(i)[2]);
            ps.executeUpdate();

        }
        System.out.println("Grades Uploaded Successfully!");
        return fac_id;
    }

    public String Update_Profile(Connection con, String fac_id) throws SQLException {
        Scanner sc = new Scanner(System.in);
        String name, join_date, contact, department;
        System.out.println("Enter Updated Name");
        name = sc.nextLine();
        System.out.println("Enter Updated Department");
        department = sc.nextLine();
        System.out.println("Enter Updated Contact");
        contact = sc.nextLine();
        System.out.println("Enter Updated Join Date in YYYY-MM-DD");
        join_date = sc.nextLine();

        String sql = "update faculty SET name = ?, join_date = ?, contact = ?,department = ? where fac_id =?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setDate(2, Date.valueOf(join_date));
        ps.setString(3,contact);
        ps.setString(4,department);
        ps.setString(5,fac_id);
        ps.executeUpdate();

        System.out.println("Do you want to change Password?");
        System.out.println("Press 1 for Yes , any other for No");
        int flag = sc.nextInt();
        sc.nextLine();
        if(flag == 1)
        {
            String pass;
            System.out.println("Enter New Password");
            pass = sc.nextLine();
            sql = "select * from faculty where fac_id = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1,fac_id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String email = rs.getString("email");
            sql = "update users SET password = ? where email = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1,pass);
            ps.setString(2,email);
            ps.executeUpdate();

        }

        System.out.println("Profile Updated!");

        return fac_id;
    }

    public String Home(Connection con, String fac_id) throws SQLException, IOException {
        int ch ;
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.println("1. Register Courses");
            System.out.println("2. Deregister Courses");
            System.out.println("3. View Grades of all Students");
            System.out.println("4. Upload Course Grades");
            System.out.println("5. Update Profile");
            System.out.println("6. Logout");
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
                Register(con, fac_id);
            }
            else if(ch == 2)
            {
                Deregister(con, fac_id);
            }
            else if(ch == 3)
            {
                View_Grades(con, fac_id);
            }
            else if(ch == 4)
            {
                Upload_Grades(con, fac_id);
            }
            else if(ch == 5)
            {
                Update_Profile(con, fac_id);
            }
            else
            {
                return fac_id;
            }
        }

    }
}
