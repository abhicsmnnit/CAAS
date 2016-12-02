package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class Affiliate
{
    private String id;
    private String email;
    private String website;

    public Affiliate(String id, String email, String website)
    {
        this.id = id;
        this.email = email;
        this.website = website;
    }

    public static Affiliate get(int affiliateId)
    {
        return new Affiliate(String.valueOf(1), "", "");
    }

    @JsonIgnore
    public List<ReportEntry> getReport()
    {
        List<ReportEntry> report = new ArrayList<>();
        report.add(new ReportEntry("14-11-2016", "Dev Cloud", "$ 8000"));
        report.add(new ReportEntry("14-11-2016", "G Suite", "$ 80000"));
        report.add(new ReportEntry("13-11-2016", "Dev Cloud", "$ 6000"));
        report.add(new ReportEntry("13-11-2016", "G Suite", "$ 70000"));
        report.add(new ReportEntry("12-11-2016", "Dev Cloud", "$ 2150"));
        report.add(new ReportEntry("12-11-2016", "G Suite", "$ 90000"));
        return report;
    }
}
