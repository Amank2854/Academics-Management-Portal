

package org.example;
import java.sql.Connection;
import java.util.*;
import java.sql.*;
public class Student
{
    public String Register(Connection con, String entry) throws SQLException {
        Scanner sc = new Scanner(System.in);
        String sql = "select DISTINCT o.course_id from offerings as o where o.course_id NOT IN (select r.course_id from registrations as r where r.student_id = ?) order by o.course_id;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, entry);
        ResultSet rs = ps.executeQuery();

        System.out.println("Courses List:");
        while (rs.next()) {
            System.out.println(rs.getString("course_id"));
        }

        //Check for Credits left

        System.out.println("Enter the Course ID you want to register");
        String id;
        id = sc.nextLine();

        sql = "select * from student where entry_no = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1, entry);
        rs = ps.executeQuery();
        rs.next();
        int present_credits = rs.getInt("credits");
        int prev_1 = rs.getInt("cred_prev");
        int prev_2 = rs.getInt("cred_prev_2");

        sql = "select C from catalog where course_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        rs = ps.executeQuery();
        rs.next();
        int course_creds = rs.getInt("C");

        if (course_creds + present_credits > 24 || present_credits + course_creds > (prev_1 + prev_2) / 2) {
            System.out.println("Credits Limit Exceeded");
            return entry;
        }

        sql = "select * from prerequisite where course_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1,id);
        rs = ps.executeQuery();

        ArrayList<String> pre_req = new ArrayList<String>();
        while (rs.next())
        {
            pre_req.add(rs.getString("reqd_id"));
        }

        boolean flag = false;

        for(String s : pre_req)
        {
            sql = "select * from grades where course_id = ? and student_id = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1,s);
            ps.setString(2,entry);
            rs = ps.executeQuery();
            if(!rs.next())
            {
                flag = true;
                break;
            }
            else
            {
                if(rs.getString("grade").equals("F"))
                {
                    flag = true;
                    break;
                }
            }
        }

        if(flag)
        {
            System.out.println("Pre-Requisite not cleared!");
            return entry;
        }


        sql = "insert into registrations values (?,?);";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        ps.setString(2, entry);
        ps.executeUpdate();


        sql = "select credits from student where entry_no = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, entry);
        rs = ps.executeQuery();
        rs.next();
        int cred = rs.getInt("credits");

        sql = "select C from catalog where course_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        rs = ps.executeQuery();
        rs.next();
        int c = rs.getInt("C");
        cred = cred + c;

        sql = "UPDATE student SET credits = ? where entry_no = ?;";
        ps = con.prepareStatement(sql);
        ps.setInt(1, cred);
        ps.setString(2, entry);
        ps.executeUpdate();

        sql = "select * from courses where course_id = ? AND branch = 'CS';";
        ps = con.prepareStatement(sql);
        ps.setString(1,id);
        rs = ps.executeQuery();

        boolean is_core = true;

        if(!rs.next())
        {
            is_core = false;
        }

        sql = "select * from curriculum where student_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1,entry);
        rs = ps.executeQuery();
        rs.next();
        int core = rs.getInt("core");
        int elec = rs.getInt("elec");
        if(is_core)
        {
            core +=c;
        }
        else
        {
            elec+=c;
        }
        sql = "UPDATE curriculum SET core = ? , elec = ? where student_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setInt(1,core);
        ps.setInt(2,elec);
        ps.setString(3,entry);
        ps.executeUpdate();

        System.out.println("Course Registered Successfully!");

        return entry;
    }

    public String Deregister(Connection con, String entry) throws SQLException {
        Scanner sc = new Scanner(System.in);
        String sql = "select course_id from registrations where student_id = ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, entry);
        ResultSet rs = ps.executeQuery();

        System.out.println("Courses List:");
        while (rs.next()) {
            System.out.println(rs.getString("course_id"));
        }

        System.out.println("Enter the Course ID you want to deregister");
        String id;
        id = sc.nextLine();

        sql = "delete from registrations where course_id = ? AND student_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        ps.setString(2, entry);
        ps.executeUpdate();

        sql = "select credits from student where entry_no = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, entry);
        rs = ps.executeQuery();
        rs.next();
        int cred = rs.getInt("credits");

        sql = "select C from catalog where course_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1, id);
        rs = ps.executeQuery();
        rs.next();
        int c = rs.getInt("C");
        cred = cred - c;

        sql = "UPDATE student SET credits = ? where entry_no = ?;";
        ps = con.prepareStatement(sql);
        ps.setInt(1, cred);
        ps.setString(2, entry);
        ps.executeUpdate();

        sql = "select * from courses where course_id = ? AND branch = 'CS';";
        ps = con.prepareStatement(sql);
        ps.setString(1,id);
        rs = ps.executeQuery();

        boolean is_core = true;

        if(!rs.next())
        {
            is_core = false;
        }

        sql = "select * from curriculum where student_id = ?;";
        ps = con.prepareStatement(sql);
        ps.setString(1,entry);
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
        ps.setString(3,entry);
        ps.executeUpdate();

        System.out.println("Course Removed Successfully!");

        return entry;
    }

    public int View_Grades(Connection con , String entry) throws SQLException {
        Scanner sc = new Scanner(System.in);
        String sql = "select course_id,grade from grades where student_id = ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, entry);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("course_id") + "- " + rs.getString("grade"));
        }

        return 1;
    }

    public double Show_CGPA(Connection con, String entry) throws SQLException {
        double cgpa = 0;
        Scanner sc = new Scanner(System.in);
        String sql = "select g.grade,cat.C from grades as g, catalog as cat where cat.course_id = g.course_id and g.student_id = ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, entry);
        ResultSet rs = ps.executeQuery();
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

        cgpa = cumulative / creds;
        System.out.println("Current CGPA: " + cgpa);

        return cgpa;
    }

    public String Update_Profile(Connection con, String entry) throws SQLException {
        Scanner sc = new Scanner(System.in);
        String name, contact;
        int batch;
        System.out.println("Enter Updated Name");
        name = sc.nextLine();
        System.out.println("Enter Updated Batch");
        batch = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Updated Contact");
        contact = sc.nextLine();

        String sql = "update student SET name = ?, batch = ?, contact = ? where entry_no = ?;";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,name);
        ps.setInt(2,batch);
        ps.setString(3,contact);
        ps.setString(4,entry);
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
            sql = "select * from student where entry_no = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1,entry);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String email = rs.getString("email");
            sql = "update users SET password = ? where email = ?;";
            ps = con.prepareStatement(sql);
            ps.setString(1,pass);
            ps.setString(2,email);
            ps.executeUpdate();

        }

        System.out.println("Profile Updated Successfully!");

        return entry;
    }

    public String Grad_Status(Connection con, String entry) throws SQLException {
        Scanner sc = new Scanner(System.in);
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
    public String Home(Connection con, String entry) throws SQLException {

        int ch;
        while(true)
        {
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.println("1. Register Course");
            System.out.println("2. Deregister Course");
            System.out.println("3. View Grades");
            System.out.println("4. Show CGPA");
            System.out.println("5. Update Profile");
            System.out.println("6. Check Graduation Status");
            System.out.println("7. Logout");
            System.out.print("Enter Your Choice:");
            ch = sc.nextInt();
            sc.nextLine();
            if (ch < 1 || ch > 7)
            {
                while (ch < 1 || ch > 7)
                {
                    System.out.println("Enter correct Choice");
                    ch = sc.nextInt();
                    sc.nextLine();
                }
            }
            if (ch == 1)
            {
                Register(con,entry);
            }
            else if (ch == 2)
            {
                Deregister(con, entry);
            }
            else if (ch == 3)
            {
                View_Grades(con, entry);
            }
            else if(ch == 4)
            {
                Show_CGPA(con, entry);
            }
            else if(ch == 5)
            {
                Update_Profile(con, entry);
            }
            else if(ch == 6)
            {
                Grad_Status(con, entry);
            }
            else
            {
                return entry;
            }
        }
    }
}
