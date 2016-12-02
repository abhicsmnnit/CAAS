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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        doOptions(req, resp);

        final String meta = req.getParameter("meta");
        final ServletOutputStream out = resp.getOutputStream();
        out.print(getBestMatchTag());
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.addHeader("Access-Control-Request-Method", "*");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Headers",
                       "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, X-Aff-Id");
    }

    private String getBestMatchTag()
    {
        return "<a href=\"https://www.google.com\"><img src=\"http://172.16.180.152:8080/images/yes-no.jpg\" height=\"100px\"></a>";
    }
}
