package util;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Created by abhinav.v on 03/12/16.
 */
public class Database
{
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static Connection c = null;
    private static Connection reportingDb = null;

    public static Connection getConnection()
    {
        if (c == null)
        {
            synchronized (Database.class)
            {
                if (c == null)
                {
                    try
                    {
                        Class.forName("com.mysql.jdbc.Driver");
                        c = DriverManager.getConnection("jdbc:mysql://172.16.189.46:3306/CAAS", "CAAS", "CAAS");
                    }
                    catch (Exception e)
                    {
                        LOGGER.info("Unable to connect to Database!");
                        e.printStackTrace();
                    }
                }
            }
        }
        return c;
    }

    public static Connection getReportingDbConnection()
    {
        if (reportingDb == null)
        {
            synchronized (Database.class)
            {
                if (reportingDb == null)
                {
                    try
                    {
                        Class.forName("com.mysql.jdbc.Driver");
                        reportingDb = DriverManager.getConnection("jdbc:mysql://172.16.189.46:3306/ss2logs", "root", "");
                    }
                    catch (Exception e)
                    {
                        LOGGER.info("Unable to connect to Reporting Database!");
                        e.printStackTrace();
                    }
                }
            }
        }
        return reportingDb;
    }

    public static void executeNonQuery(String sql) throws SQLException
    {
        try (Statement stmt = c.createStatement())
        {
            stmt.executeUpdate(sql);
        }
    }

    private static void closeConnection()
    {
        try
        {
            c.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
//            LOGGER.error(e.getMessage());
//            SendEmail.sendErrorMail(e.getMessage());
        }
    }
}
