package controller;

import model.Token;
import util.JsonString;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        if (username == null || password == null)
        {
            out.print("Invalid input");
        }
        else
        {
            out.print(JsonString.of(new Token(username + "~" + password)));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doGet(req, resp);
    }
}