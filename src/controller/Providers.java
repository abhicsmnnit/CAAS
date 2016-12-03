package controller;

import model.Token;
import util.Database;
import util.JsonString;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class Providers extends HttpServlet
{
    private static final Logger log = Logger.getLogger(Providers.class.getName());

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        ServletOutputStream out = resp.getOutputStream();

        log.info(req.getPathInfo());

        final String username = req.getParameter("username");
        final String password = req.getParameter("password");
        final String website = req.getParameter("website");

        if (username == null || password == null || website == null)
        {
            out.print("Invalid input");
        }
        else
        {
            try (PreparedStatement pst = Database.getConnection().prepareStatement(
                    "INSERT INTO provider (username, password, website, token) VALUES (?, ?, ?, UUID())",
                    Statement.RETURN_GENERATED_KEYS))
            {
                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, website);

                pst.executeUpdate();
                final ResultSet generatedKeys = pst.getGeneratedKeys();

                int providerId = -1;
                if (generatedKeys.next())
                {
                    providerId = generatedKeys.getInt(1);
                }

                final ResultSet resultSet = Database.getConnection().createStatement().executeQuery(
                        "SELECT token FROM provider WHERE id = " + providerId);

                if (resultSet.next())
                {
                    out.print(JsonString.of(new Token(resultSet.getString("token"))));
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}