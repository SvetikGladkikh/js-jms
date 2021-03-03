import queue.CommonQueueConsumer;
import queue.CommonQueueProducer;
import topic.TopicConsumer;
import topic.TopicProducer;

import java.util.Date;

public class Runner {
    private static CommonQueueProducer COMMON_PRODUCER = new CommonQueueProducer();
    private static CommonQueueConsumer channel1QueueConsumer;
    private static CommonQueueConsumer channel2QueueConsumer;


    private static TopicProducer TOPIC_PRODUCER = new TopicProducer();
    private static TopicConsumer consumer1ForTopic;
    private static TopicConsumer consumer2ForTopic;

    public static void main(String... args){
        Thread shutdownHook = new Thread(() -> {
            shutdown();
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        channel1QueueConsumer = new CommonQueueConsumer("channel1");
        channel1QueueConsumer.start();

        channel2QueueConsumer = new CommonQueueConsumer("channel2");
        channel2QueueConsumer.start();

        consumer1ForTopic = new TopicConsumer("Consumer1");
        consumer1ForTopic.start();

        consumer2ForTopic = new TopicConsumer("Consumer2");
        consumer2ForTopic.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<=100; i++) {
            // Отправление сообщений в очереди
            COMMON_PRODUCER.sendToChannel1("Hello, Channel1");
            COMMON_PRODUCER.sendToChannel2("Hello, Channel2");

            // Отправление сообщений в топик
            TOPIC_PRODUCER.sendMessage("Hello, Consumer! Time: " + new Date().getTime());
        }

        shutdown();
    }

    private static void shutdown(){
        if(channel1QueueConsumer != null && channel1QueueConsumer.isStarted()){
            channel1QueueConsumer.stop();
        }

        if(channel2QueueConsumer != null && channel2QueueConsumer.isStarted()){
            channel2QueueConsumer.stop();
        }

        if(consumer1ForTopic != null && consumer1ForTopic.isStarted()){
            consumer1ForTopic.stop();
        }

        if(consumer2ForTopic != null && consumer2ForTopic.isStarted()){
            consumer2ForTopic.stop();
        }
    }


}
