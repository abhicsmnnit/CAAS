package controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
        ServletOutputStream out = resp.getOutputStream();

        log.info(req.getPathInfo());

        final String email = req.getParameter("email");
        final String website = req.getParameter("website");

        if (email == null || website == null)
        {
            out.print("Invalid input");
        }
        else
        {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

//            out.print(objectMapper.writeValueAsString(new Affiliate(9876, email, website)));
        }
    }
}
