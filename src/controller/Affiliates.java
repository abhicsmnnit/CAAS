package controller;

import controller.helper.AffiliateRequest;
import model.Affiliate;
import model.Provider;
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
import java.util.Optional;
import java.util.logging.Logger;

import static controller.helper.AffiliateActions.NO_ACTION;
import static controller.helper.AffiliateActions.SHOW_REPORT;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class Affiliates extends HttpServlet
{
    private static final Logger log = Logger.getLogger(Affiliates.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        final Optional<Provider> provider = Provider.get(req.getHeader("X-Auth-Token"));
        if (!provider.isPresent())
        {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final ServletOutputStream out = resp.getOutputStream();
        final Optional<AffiliateRequest> affiliateRequest = AffiliateRequest.of(req.getPathInfo());

        if (affiliateRequest.isPresent())
        {
            final AffiliateRequest request = affiliateRequest.get();
            if (SHOW_REPORT.equals(request.getAction()))
            {
                out.print(JsonString.of(Affiliate.get(request.getId()).get().getReport()));
            }
            else if (NO_ACTION.equals(request.getAction()))
            {
                out.print(JsonString.of(provider.get().getAffiliates()));
            }
            return;
        }

        out.print("Invalid URL");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        final Optional<Provider> provider = Provider.get(req.getHeader("X-Auth-Token"));
        if (!provider.isPresent())
        {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        ServletOutputStream out = resp.getOutputStream();

        final String username = req.getParameter("email");
        final String name = req.getParameter("name");

        if (username == null || name == null)
        {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else
        {
            try (PreparedStatement pst = Database.getConnection().prepareStatement(
                    "INSERT INTO affiliate (username, name, provider_id) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS))
            {
                pst.setString(1, username);
                pst.setString(2, name);
                pst.setInt(3, provider.get().getId());

                pst.executeUpdate();

                final ResultSet generatedKeys = pst.getGeneratedKeys();

                if (generatedKeys.next())
                {
                    out.print(JsonString.of(Affiliate.get(generatedKeys.getInt(1)).get()));
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}
