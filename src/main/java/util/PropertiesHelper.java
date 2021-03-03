package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHelper {

    private static final String PROPERTIES_FILENAME = "conf/config.properties";

    private static final String JMS_CONNECTION_PARAMETER_NAME = "jms.connectionString";
    private static final String JMS_USERNAME_PARAMETER_NAME = "jms.username";
    private static final String JMS_PASSWORD_PARAMETER_NAME = "jms.password";

    private static PropertiesHelper propertiesHelper;

    private Properties properties;

    public static PropertiesHelper get(){
        if(propertiesHelper == null){
            propertiesHelper = new PropertiesHelper();
        }
        return propertiesHelper;
    }

    public PropertiesHelper() {
        FileInputStream fis;
        properties = new Properties();

        try {
            fis = new FileInputStream(PROPERTIES_FILENAME);
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }

    public String getJmsConnectionString(){
        return properties.getProperty(JMS_CONNECTION_PARAMETER_NAME);
    }

    public String getJmsUsername(){
        return properties.getProperty(JMS_USERNAME_PARAMETER_NAME);
    }

    public String getJmsPassword(){
        return properties.getProperty(JMS_PASSWORD_PARAMETER_NAME);
    }
}
