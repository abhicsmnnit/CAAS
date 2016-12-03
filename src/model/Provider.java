package model;

import util.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Created by abhinav.v on 03/12/16.
 */
public class Provider
{
    private int id;
    private String website;

    public Provider(int id, String website)
    {
        this.id = id;
        this.website = website;
    }

    public String getWebsite()
    {
        return website;
    }

    public int getId()
    {
        return id;
    }

    public static Optional<Provider> get(String token)
    {
        try (Statement statement = Database.getConnection().createStatement())
        {
            final ResultSet resultSet = statement.executeQuery("SELECT id, website FROM provider WHERE token = " + token);
            if (resultSet.next())
            {
                return Optional.of(new Provider(resultSet.getInt("id"), resultSet.getString("website")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static Optional<Provider> get(int id)
    {
        try (Statement statement = Database.getConnection().createStatement())
        {
            final ResultSet resultSet = statement.executeQuery("SELECT id, website FROM provider WHERE id = " + id);
            if (resultSet.next())
            {
                return Optional.of(new Provider(resultSet.getInt("id"), resultSet.getString("website")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
