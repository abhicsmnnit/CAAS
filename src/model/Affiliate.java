package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import util.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class Affiliate
{
    private int id;
    private String email;
    private String name;

    @JsonIgnore
    private int providerId;

    public Affiliate(int id, String email, String name, int providerId)
    {
        this.id = id;
        this.email = email;
        this.name = name;
        this.providerId = providerId;
    }

    @JsonIgnore
    public List<ReportEntry> getReport()
    {
        List<ReportEntry> report = new ArrayList<>();

        try (Statement statement = Database.getReportingDbConnection().createStatement())
        {
            final ResultSet resultSet = statement.executeQuery(
                    "SELECT added, productType, customerCost / 2 AS customerCost FROM ss2logs WHERE affAid = " + id);
            while (resultSet.next())
            {
                report.add(new ReportEntry(resultSet.getString("added"),
                                           resultSet.getString("productType"),
                                           String.format("%.2f", resultSet.getDouble("customerCost") + Math.random() * 10 + 1)));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return report;
    }

    public static Optional<Affiliate> get(int id)
    {
        try (Statement statement = Database.getConnection().createStatement())
        {
            final ResultSet resultSet = statement.executeQuery(
                    "SELECT username, name, provider_id FROM affiliate WHERE id = " + id);
            if (resultSet.next())
            {
                return Optional.of(new Affiliate(id,
                                                 resultSet.getString("username"),
                                                 resultSet.getString("name"),
                                                 resultSet.getInt("provider_id")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public int getProviderId()
    {
        return providerId;
    }
}
