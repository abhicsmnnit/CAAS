package util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by abhinav.v on 02/12/16.
 */
public class ObjectToJsonString
{
    private static ObjectMapper objectMapper;

    static
    {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static String of(Object o) throws JsonProcessingException
    {
        return objectMapper.writeValueAsString(o);
    }
}
