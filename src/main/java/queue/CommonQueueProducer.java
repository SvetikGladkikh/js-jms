package queue;

import util.Constants;
import util.JmsConnectionHolder;

import javax.jms.*;

public class CommonQueueProducer {
    private static final String CHANNEL1_NAME = "channel1";
    private static final String CHANNEL2_NAME = "channel2";

    public void sendToChannel1(String message){
        sendMessage(message, CHANNEL1_NAME);
    }

    public void sendToChannel2(String message){
        sendMessage(message, CHANNEL2_NAME);
    }

    private void sendMessage(String text, String channel){
        try (Connection connection = JmsConnectionHolder.get()){
            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(Constants.QUEUE_NAME);

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a messages
            TextMessage message = session.createTextMessage(text);
            message.setStringProperty("channel", channel);

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
