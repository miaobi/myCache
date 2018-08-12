package com.shenghua.app.config;


import com.sheng.hua.cache.api.CacheClient;
import com.sheng.hua.cache.data.ServerInetAddress;
import com.sheng.hua.cache.impl.CacheClientImpl;
import io.netty.channel.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AppConfig {
    @Bean
    public CacheClient getCacheClient() throws Exception {
        try{
            int port = 8090;
            CacheClientImpl cacheClient = new CacheClientImpl();
            ServerInetAddress serverInetAddress = new ServerInetAddress();
            serverInetAddress.setHost("127.0.0.1");
            for (int i = 0; i < 3; i++,port++) {
                serverInetAddress.setPort(port);
                Channel channel = cacheClient.connect(serverInetAddress);
            }
            return cacheClient;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
