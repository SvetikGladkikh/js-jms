package queue;

import util.Constants;
import util.JmsConnectionHolder;

import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommonQueueConsumer {
    private final String channel;
    private boolean isStarted;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private ExecutorService service;

    public CommonQueueConsumer(String channel) {
        this.channel = channel;
    }

    public void start() {
        try {
            connection = JmsConnectionHolder.get();
            connection.start();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(Constants.QUEUE_NAME);

            // Create a MessageConsumer from the Session to the Topic or Queue
            consumer = session.createConsumer(destination, "channel = '" + channel + "'");

            service = Executors.newFixedThreadPool(2);
            service.submit(() -> {
                while (true) {
                    // Wait for a message
                    Message message = null;
                    try {
                        message = consumer.receive(1000);
                        if(message != null) {
                            if (message instanceof TextMessage) {
                                TextMessage textMessage = (TextMessage) message;
                                String text = textMessage.getText();
                                System.out.println(channel + " Получено: " + text);
                            } else {
                                System.out.println(channel + " Получено: " + message);
                            }
                        }

                    } catch (JMSException e) {
                        System.out.println("Caught: " + e);
                        e.printStackTrace();
                    }
                }
            });

            isStarted = true;
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
            e.printStackTrace();
        }
    }

    public void stop(){
        if(isStarted) {
            if (session != null && connection != null && consumer != null) {
                try {
                    consumer.close();
                    session.close();
                    connection.close();
                } catch (JMSException e) {
                    System.out.println("Ошибка: " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isStarted(){
        return isStarted;
    }
}
