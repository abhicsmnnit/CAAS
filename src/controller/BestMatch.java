package controller;

import model.Affiliate;
import model.Provider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class BestMatch extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        setRequiredHeaders(resp);

        final String affiliateIdString = req.getHeader("X-Aff-Id");
        int affiliateId;
        try
        {
            affiliateId = Integer.parseInt(affiliateIdString);
        }
        catch (NumberFormatException e)
        {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final Optional<Provider> provider = getProvider(affiliateId);

        if (provider.isPresent())
        {
            resp.getOutputStream().print(getBestMatchTag(getProductFromMLModule(req, affiliateId, provider),
                                                         provider.get().getWebsite(),
                                                         affiliateId));
        }
        else
        {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String getProductFromMLModule(HttpServletRequest req,
                                          int affiliateId,
                                          Optional<Provider> provider) throws IOException
    {
        return new BufferedReader(new InputStreamReader(getResponse(req,
                                                                    affiliateId,
                                                                    provider).getEntity().getContent())).readLine();
    }

    private CloseableHttpResponse getResponse(HttpServletRequest req,
                                              int affiliateId,
                                              Optional<Provider> provider) throws IOException
    {
        return HttpClientBuilder.create()
                                .build()
                                .execute(getRequest(req, affiliateId, provider));
    }

    private HttpGet getRequest(HttpServletRequest req, int affiliateId, Optional<Provider> provider)
    {
        return new HttpGet(getMLRequestUrl(req, affiliateId, provider));
    }

    private String getMLRequestUrl(HttpServletRequest req, int affiliateId, Optional<Provider> provider)
    {
        return "http://172.16.181.96:8090/index?content=" + getData(req.getParameter("keywords"))
               + "&entityid=" + provider.get().getId() + "&affiliateid=" + affiliateId
               + "&country=" + getCountryFromIP(req.getRemoteAddr());
    }

    private Optional<Provider> getProvider(int affiliateId)
    {
        final Optional<Affiliate> affiliate = Affiliate.get(affiliateId);
        return affiliate.isPresent()
               ? Provider.get(affiliate.get().getProviderId())
               : Optional.empty();
    }

    private String getCountryFromIP(String ipAddress)
    {
        return "US";
    }

    private String getData(String keywords)
    {
        return keywords;
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        setRequiredHeaders(resp);
    }

    private void setRequiredHeaders(HttpServletResponse resp)
    {
        resp.addHeader("Access-Control-Request-Method", "*");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Headers",
                       "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, X-Aff-Id");
    }

    private String getBestMatchTag(final String productKey, final String providerWebsite, int affiliateId)
    {
        return "<a href=\"" + providerWebsite + "\"><img src=\"http://172.16.180.152:8080/images/" + productKey + ".jpg\" height=\"100px\"></a>";
    }
}
