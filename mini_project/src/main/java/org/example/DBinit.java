package org.example;

import java.io.*;
import java.sql.*;

public class DBinit
{

    static void executeSql(File file, Connection connection)
    {
        try
        {
            System.out.println(file.getName());
            Statement statement = connection.createStatement();
            String sql = "";
            try (FileReader fileReaderObj = new FileReader(file))
            {
                char cur;
                while ((cur = (char) fileReaderObj.read()) != (char) -1)
                {
                    sql = sql + cur;
                }
            } catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
            statement.executeUpdate(sql);
            statement.close();
        }
        catch (Exception e)
        {
            System.out.println(file.getName() + ": " + e.getMessage());
        }
    }

    public void connect()
    {

        final String DB_URL_1 = "jdbc:postgresql://localhost/postgres";
        final String DB_URL_2 = "jdbc:postgresql://localhost/mini_project";
        final String USER = "postgres";
        final String PASSWORD = "aman123456789000";

        try
        {
            Connection connection_2 = DriverManager.getConnection(DB_URL_2, USER, PASSWORD);
            System.out.println("Successfully connected to the database: " + DB_URL_2);
            String filePath = new File("").getAbsolutePath() + "/sql/queries.txt";
            File file = new File(filePath);
            executeSql(file, connection_2);
            System.out.println("Executed Successfully");
            connection_2.close();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}
