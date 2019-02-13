package com.taotao.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

public class TestActiveMq {

    //queue
    //Producer
    //点到点
    @Test
    public void testQueueProducer() throws  Exception{
        //1.创建一个连接工厂对象ConnectionFactory对象，需要指定mq服务的ip及端口
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.169.132:61616");
        //2.使用ConnectionFactory创建一个连接Connection对象
        Connection connection=connectionFactory.createConnection();
        //3.开启连接，调用Connection对象的start方法
        connection.start();;
        //4.使用Connection对象创建一个Session对象
        //第一个参数是否开启事务,一般不使用事务。保证数据最终的一致
        //如果第一个参数为true,第二个参数自动忽略
        //如果不开启事务，第二个参数是消息的应答模式，一般自动应答即可
        Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象创建一个Destination对象，两种形式queue,topic.现在应该使用queue
        //参数是消息队列的名称
        Queue queue=session.createQueue("test-queue");
        //6.使用Session对象创建一个Producer对象
        MessageProducer messageProducer=session.createProducer(queue);
        //7.创建一个TestMessage对象  两种方式
//        TextMessage textMessage=new ActiveMQTextMessage();
//        textMessage.setText(" hello  lzh");
        TextMessage textMessage= session.createTextMessage("hello havey");
        //8.发送消息
        messageProducer.send(textMessage);
        //9.关闭资源
        messageProducer.close();
        session.close();
        connection.close();
    }

    @Test
    public void testQueueConsumer() throws Exception{
        //创建一个连接工厂对象
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.169.132:61616");
        //使用连接工厂对象创建一个连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用连接对象创建一个Session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用Session创建一个Destination，Destination应该和消息的发送端一致
        Queue queue = session.createQueue("test-queue");
        //使用Session创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(queue);
        //向Consumer对象中设置一个MessageListener对象，用来接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //取消息的内容
                if (message instanceof TextMessage){
                    TextMessage textmessage = (TextMessage) message;
                    try {
                        String text = textmessage.getText();
                        //打印消息内容
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //系统等待接收消息
        System.in.read();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

    //topic   默认不在服务端持久化，queue可以
    //Producer
    @Test
    public void testTopicProducer() throws  Exception{
        //创建一个连接工厂对象
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.169.132:61616");
        //使用连接工厂对象创建一个连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用连接对象创建一个Session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建Destination,应该使用topic
        Topic topic = session.createTopic("test-topic");
        //创建一个Producer对象
        MessageProducer producer=session.createProducer(topic);
        //创建一个TextMessage对象
        TextMessage textMessage=session.createTextMessage("hello activemq topic");
        //发送消息
        producer.send(textMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();

    }

    //topic
    //consumer
    @Test
    public void testTopicConsumer() throws  Exception{
        //创建一个连接工厂对象
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.169.132:61616");
        //使用连接工厂对象创建一个连接
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用连接对象创建一个Session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用Session创建一个Destination，Destination应该和消息的发送端一致
        Topic topic = session.createTopic("test-topic");
        //使用Session创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(topic);
        //向Consumer对象中设置一个MessageListener对象，用来接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                //取消息的内容
                if (message instanceof TextMessage){
                    TextMessage textmessage = (TextMessage) message;
                    try {
                        String text = textmessage.getText();
                        //打印消息内容
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //系统等待接收消息
        System.out.println("topic消费者3.。。。。。。。");
        System.in.read();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

}
