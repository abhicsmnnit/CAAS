package controller.helper;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class AffiliateRequest
{
    public static final int DUMMY_AFFILIATE_ID = 0;
    private int id;
    private AffiliateActions action;

    public AffiliateRequest(int id, AffiliateActions action)
    {
        this.id = id;
        this.action = action;
    }

    public static Optional<AffiliateRequest> of(String requestPath)
    {
        if (requestPath == null)
        {
            return Optional.of(new AffiliateRequest(DUMMY_AFFILIATE_ID, AffiliateActions.NO_ACTION));
        }

        Pattern reportPattern = Pattern.compile("/([0-9]*)/(report$)");
        Matcher matcher;
        matcher = reportPattern.matcher(requestPath);
        if (matcher.find())
        {
            int id = Integer.parseInt(matcher.group(1));
            return Optional.of(new AffiliateRequest(id, AffiliateActions.SHOW_REPORT));
        }

        return Optional.empty();
    }

    public AffiliateActions getAction()
    {
        return action;
    }

    public int getId()
    {
        return id;
    }
}

