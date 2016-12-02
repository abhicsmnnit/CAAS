package controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.helper.AffiliateRequest;
import model.Affiliate;
import util.ObjectToJsonString;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        final ServletOutputStream out = resp.getOutputStream();
        final Optional<AffiliateRequest> request = AffiliateRequest.of(req.getPathInfo());

        if (request.isPresent())
        {
            if (SHOW_REPORT.equals(request.get().getAction()))
            {
                out.print(ObjectToJsonString.of(Affiliate.get(request.get().getId()).getReport()));
            }
            else if (NO_ACTION.equals(request.get().getAction()))
            {
                out.print(ObjectToJsonString.of(getAffiliates()));
            }
            return;
        }

        out.print("Invalid URL");
    }

    private List<Affiliate> getAffiliates()
    {
        List<Affiliate> affiliates = new ArrayList<>();
        affiliates.add(new Affiliate("1", "sundar.pichai@google.com", "google.com"));
        affiliates.add(new Affiliate("2", "mark.zuck@facebook.com", "facebook.com"));
        affiliates.add(new Affiliate("3", "elon.musk@spacex.com", "spacex.com"));
        return affiliates;
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

            out.print(objectMapper.writeValueAsString(new Affiliate("totally-random-id", email, website)));
        }
    }
}
