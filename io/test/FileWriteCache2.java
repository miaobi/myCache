package log.io.test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public final class FileWriteCache2 {
	// 用来获取编号
	private static AtomicInteger globalNo = new AtomicInteger(0);
	/** 5/27架构大改：每个Producer独享一个FileWriteCache */
	// 表示cache已经写到哪个位置了（其实就是长度）
	private int pos;

	private RandomAccessFile raf;
	private FileChannel outChannel;
//	private MappedByteBuffer mappedFile;

	private long bytesWrite = 0;

	private final static int SIZE = (128*1024*1024);

	private final static int FILE_SIZE = (172 * 1024 *1024);

	private ByteBuffer wholeData;
	private static final Lock diskLock = new ReentrantLock();

	public FileWriteCache2(RandomAccessFile raf) throws IOException {
		this.wholeData = ByteBuffer.allocateDirect(SIZE);
		pos = 0;
		this.raf = raf;
		this.outChannel = this.raf.getChannel();
	}
	

	public void write(final byte[] data) {
		writeData(data);
	}
	
	private void flush2File() {
		this.wholeData.flip();
		if(this.wholeData.hasRemaining()) {
			try {
				diskLock.lock();
				while(this.wholeData.hasRemaining()) {
					this.outChannel.write(wholeData);
				}		
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				diskLock.unlock();
			}
		}
		this.wholeData.clear();
	}
	
	private void writeData(final byte[] data) {
		if(data.length + 7 > this.wholeData.remaining()) {
			// 写满了，flush
			flush2File();
		}
		this.wholeData.put(data, 0, data.length);
	}

	public void flush() {		
		flush2File();
		this.wholeData = null;
	}
	
	public void close() {
		try {
			this.outChannel.close();			
			this.raf.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}

