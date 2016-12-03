package controller;

import model.Affiliate;
import model.Provider;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class BestMatch extends HttpServlet
{
    public static final Map<String, String> URL_MAP = new HashMap<>();

    static
    {
        URL_MAP.put("hostinglinuxus", "cart/hosting/linux/us/300171/12/");
        URL_MAP.put("dedicatedserverslinuxus", "cart/dedicatedservers/linux/us/78/12/");
        URL_MAP.put("cloudsiteslinuxus", "cart/cloudsites/linux/us/300141/1/");
        URL_MAP.put("resellerhostinglinuxin", "cart/resellerhosting/linux/in/38/1/");
    }

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
        } else
        {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String getProductFromMLModule(HttpServletRequest req,
                                          int affiliateId,
                                          Optional<Provider> provider) throws IOException
    {
        String productKey = new BufferedReader(new InputStreamReader(getResponse(req,
                                                                                 affiliateId,
                                                                                 provider).getEntity().getContent())).readLine();
        if (!URL_MAP.containsKey(productKey))
        {
            productKey = URL_MAP.keySet()
                                .stream()
                                .findFirst().get();
        }
        return productKey;
    }

    private CloseableHttpResponse getResponse(HttpServletRequest req,
                                              int affiliateId,
                                              Optional<Provider> provider) throws IOException
    {
        return HttpClientBuilder.create()
                                .build()
                                .execute(getRequest(req, affiliateId, provider));
    }

    private HttpPost getRequest(HttpServletRequest req, int affiliateId, Optional<Provider> provider)
            throws UnsupportedEncodingException
    {
        HttpPost httpPost = new HttpPost(getMLRequestUrl());

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("content", getData(req.getParameter("keywords"))));
        urlParameters.add(new BasicNameValuePair("entityid", String.valueOf(provider.get().getId())));
        urlParameters.add(new BasicNameValuePair("affiliateid", String.valueOf(affiliateId)));
        urlParameters.add(new BasicNameValuePair("country", getCountryFromIP(req.getRemoteAddr())));

        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

        return httpPost;
    }

    private String getMLRequestUrl()
    {
        return "http://172.16.181.96:8090/index";
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
        return "<a href=\"http://" + providerWebsite + "/" + getURL(productKey,
                                                                    affiliateId) + "\"><img src=\"http://172.16.180.152:8080/images/" + productKey + ".png\" height=\"100px\"></a>";
    }

    private String getURL(String productKey, int affiliateId)
    {
        return URL_MAP.get(productKey) + "?a_aid=" + affiliateId;
    }
}