package netty;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class CmdClient {
    private static CmdClient client;
    private Map<String,ChannelHandlerContext> map = new ConcurrentHashMap(100,
    0.75f, 100);

    public static CmdClient getClient() {
        if(client==null){
            synchronized (CmdClient.class){
                if(client==null){
                    client = new CmdClient();
                }
            }
        }
        return client;
    }
    public void put(String key,ChannelHandlerContext ctx){
        map.put(key,ctx);
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        for(;;){
            System.out.println("cmd send msg:"+s.nextLine());
        }
    }
    public void receivMsgByCmd(){
        Scanner s = new Scanner(System.in);
        for(;;){
           final String msg = s.nextLine();
           map.forEach((k,v)->{
               v.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
           });
        }
    }
}
