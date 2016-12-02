package model;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class ReportEntry
{
    private String date;
    private String product;
    private String earning;

    public ReportEntry(String date, String product, String earning)
    {
        this.date = date;
        this.product = product;
        this.earning = earning;
    }
}