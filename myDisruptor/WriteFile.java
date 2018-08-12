package myDisruptor;

import java.io.*;

public class WriteFile implements Runnable{
    private static final String INTERIMPATH = "D:/workspace_intellij/MessageSystem/week_log/interim/";
    private String logName;
    private String content;

    public WriteFile(String logName, String content){
        this.logName = logName;
        this.content = content;
    }

    @Override
    public void run(){
        try{
            File logFile = new File(INTERIMPATH + logName);     //文件路径（路径+文件名）
            if (!logFile.exists()) {   //文件不存在则创建文件，先创建目录
                File dir = new File(logFile.getParent());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                logFile.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(logFile, true)));
            out.write(content);
            out.write("\r\n");
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后关闭文件
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
