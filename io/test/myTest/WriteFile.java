package log.io.test.myTest;

import log.io.test.FileWriteCache2;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class WriteFile implements Runnable {
    private int index;
    private RandomAccessFile writerRaf;
    private FileWriteCache2 writeCache;

    public WriteFile(int index) {
        this.index = index;
        this.writerRaf = Common.targetRafMap.get(index + 1);
        try {
            this.writeCache = new FileWriteCache2(writerRaf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        List arrayList = Common.fileIndexMap.get(index + 1);
        long startTime = System.currentTimeMillis();
        Collections.sort(arrayList);
        FileIndex fileIndex = null;
        String fileName = "";
        long offset = 0;
        long length = 0;
        String line = "";
        BufferedReader in = null;
        BufferedWriter out = null;
        BufferedInputStream bis = null;
        RandomAccessFile readRaf = null;
        try {
            for (Object object : arrayList) {
                fileIndex = (FileIndex) object;
                fileName = fileIndex.getFileName();
                offset = fileIndex.getOffset();
                length = fileIndex.getLength();
                readRaf = Common.sourceRafMap.get(fileName);
                byte[] b = new byte[(int) length + 2];
                readRaf.seek(offset);
                readRaf.read(b, 0, (int) length + 2);
//                raf = new RandomAccessFile(Common.FROM_PATH + fileName, "r");
//                raf.seek(offset);
//                line = raf.readLine();
////                in = Common.buffReadMap.get(fileName);
//                in = new BufferedReader(new FileReader(new File(Common.FROM_PATH + fileName)));
//                in.skip(offset);
//                line = in.readLine();

//                bis = new BufferedInputStream(new FileInputStream(Common.FROM_PATH + fileName));
//                bis.skip(offset);
//                int c = 0;
//                for(long i = 0; (i < (length + 2)) && (c = bis.read()) != -1; i++){
//                    line += (char)c;
//                }
                writeCache.write(b);
//                writeCache.write("\n".getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeCache.flush();
            writeCache.close();
        }
        Common.writeCounter.countDown();
        System.out.println("write " + (index + 1) + ".log cost " + (System.currentTimeMillis() - startTime) + " ms");
    }


}
