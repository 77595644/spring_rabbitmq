package com.zixue.www.web;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/2/7 0007.
 */
public class Foo implements ChannelAwareMessageListener {



    public static void main(final String... args) throws Exception {

        AbstractApplicationContext ctx =
                new ClassPathXmlApplicationContext("spring-config.xml");
        final RabbitTemplate template = ctx.getBean(RabbitTemplate.class);
        final AtomicInteger count = new AtomicInteger(0);
        while(true){
            Thread t = new Thread(new Runnable() {
                public void run() {
                    template.convertAndSend("myExchange", "test", "Hello, world! " + count.decrementAndGet());
                    System.out.println(count.get());
                }
            });

            t.start();

        }

    }

    public void onMessage(Message message , Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
