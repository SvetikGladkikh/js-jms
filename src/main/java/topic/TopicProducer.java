package topic;

import util.Constants;
import util.JmsConnectionHolder;

import javax.jms.*;

public class TopicProducer {

    public void sendMessage(String text){
        try (Connection connection = JmsConnectionHolder.get()){
            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic(Constants.TOPIC_NAME);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            producer.send(message);

            // Clean up
            session.close();
        } catch (JMSException e) {
            System.out.println("ОШИБКА: Невозможно отправить сообщение. " + e);
            throw new RuntimeException(e);
        }
    }
}
