package controller;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class BestMatch extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        final String meta = req.getParameter("meta");
        final ServletOutputStream out = resp.getOutputStream();
        out.print(getBestMatchTag());
    }

    private String getBestMatchTag()
    {
        return "<a href=\"https://www.google.com\"><img src=\"images/yes-no.jpg\" height=\"100px\"></a>";
    }
}
