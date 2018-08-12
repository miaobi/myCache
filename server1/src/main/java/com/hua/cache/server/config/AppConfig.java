package com.hua.cache.server.config;

import com.hua.cache.server.service.impl.FileIndexBuild;
import com.hua.cache.server.transport.CacheServer;
import com.hua.cache.server.transport.FileServer;
import com.hua.cache.server.transport.NewCacheServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AppConfig {
//    @Bean
//    public CacheServer getCacheServer1() throws Exception {
//        CacheServer server = null;
//        int port = 8090;
//        for (int i = 0; i < 3; i++,port++) {
//            server = new CacheServer();
//            server.start(port);
//            System.out.println("listening port："+port);
//        }
//        return server;
//    }

//    @Bean
//    public FileServer getFileServer() throws Exception {
////        FileIndexBuild fileIndexBuild = new FileIndexBuild();
////        fileIndexBuild.build();
//        FileServer server = null;
//        int port = 8090;
//        for (int i = 0; i < 3; i++,port++) {
//            server = new FileServer();
//            server.start(port);
//            System.out.println("listening port："+port);
//        }
//        return server;
//    }

    @Bean
    public FileServer getFileServer() throws Exception {
//        FileIndexBuild fileIndexBuild = new FileIndexBuild();
//        fileIndexBuild.build();
//        NewCacheServer server = null;
        FileServer server = null;
        int port = 8090;
        for (int i = 0; i < 3; i++,port++) {
//            server = new NewCacheServer();
            server = new FileServer();
            server.start(port);
            System.out.println("listening port："+port);
        }
        return server;
    }
}
