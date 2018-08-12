package Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class IOtest
{
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        //建立对服务器的Socket连接
        Socket clientSocket = serverSocket.accept();
        //clientSocket.getInputStream()是从Socket取得输入串流
        //InputStreamReader是低层和高层串流间的桥梁
        InputStreamReader stream = new InputStreamReader(clientSocket.getInputStream());
        BufferedReader in = new BufferedReader(stream);
        //建立连接到Socket的PrintWriter
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        String request, response;
        //从Socket上读取数据
        while((request = in.readLine()) != null){
            if("Done".equals(request)){
                break;
            }
            response = request;
            //写数据到Socket上
            out.println(response);
        }
    }

}
