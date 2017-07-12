package bupt.androidsipchat.sip.util;


import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * Created by Maou Lim on 2017/7/4.
 */
public class Configuration {

    private static final String DEFAULT_JSON =
            "{\n" +
                    "    \"driver\": \"com.mysql.jdbc.Driver\",\n" +
                    "    \"URL\": \"\",\n" +
                    "    \"ACCOUNT\": \"\",\n" +
                    "    \"PASSWORD\": \"\"\n" +
                    "}";

    private JSONObject object = null;

    public Configuration() {
        object = new JSONObject();
    }

    public Configuration(String url) {
        String json = null;

        try {
            json = readFromResource(url);
        } catch (IOException ex) {
            /* if there is an exception during the file io */
            json = DEFAULT_JSON;
        }

        this.object = JSONObject.fromObject(json);

    }

    public boolean containKey(Object key) {
        return object.containsKey(key);
    }

    /* get the value of the specific property */
    public Object get(String property) {
        return object.get(property);
    }

    /*
     * set the value of the property. when the property
     * doesn't exist, it will be created.
     * @return the former value of the property
     */
    public Object put(String property, Object value) {
        return object.put(property, value);
    }

    private String readFromResource(String url) throws IOException {
        /* load the resource file */
        BufferedReader reader = new BufferedReader(new FileReader(url));

        /* create a string builder to store the content of the file */
        StringBuilder builder = new StringBuilder();

        String line = null;

        while (null != (line = reader.readLine())) {
            builder.append(line);
        }

        reader.close();

        return builder.toString();
    }
}

