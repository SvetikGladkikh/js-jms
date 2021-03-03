package util;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.JMSException;

public class JmsConnectionHolder {

    public static Connection get(){
        String jmsConnectionString = PropertiesHelper.get().getJmsConnectionString();
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsConnectionString);
        try {
            return connectionFactory.createConnection(PropertiesHelper.get().getJmsUsername(), PropertiesHelper.get().getJmsPassword());
        } catch (JMSException e) {
            System.out.println("ОШИБКА: Невозможно открыть соединение. " + e);
            throw new RuntimeException(e);
        }
    }
}
