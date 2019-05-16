package com.gtl.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedis {

    @Test
    public void testJedis() throws Exception{
        //创建一个jedis对象，需要指定ip和端口号
        Jedis jedis=new Jedis("192.168.169.130",6379);
        //直接操作数据库
        jedis.set("jedis-key","123");
        String result=jedis.get("jedis-key");
        System.out.println( result);
        //关闭数据库
        jedis.close();
    }
    @Test
    public void testJedisPool() throws Exception{
        //创建一个数据库连接池对象（单例），需要指定ip和端口号
        JedisPool jedisPool=new JedisPool("192.168.169.130",6379);
        //从连接池中获得连接
        Jedis jedis=jedisPool.getResource();
        //使用Jedis操作数据库
        String result=jedis.get("jedis-key");
        System.out.println( result);
        //关闭数据库
        jedis.close();
        //关闭连接池
        jedisPool.close();
    }


}
