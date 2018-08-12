package log.io.test.myTest;

import log.io.test.TestDate;
import org.jetbrains.annotations.NotNull;


public class FileIndex implements Comparable<FileIndex>{
    private String fileName;
    private long offset;
    private long length;
    private String date;
    private String line;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public int compareTo(@NotNull FileIndex o) {
        return TestDate.getDate2(this.date).compareTo(TestDate.getDate2(o.date));
    }

    @Override
    public String toString() {
        return "FileIndex{" +
                "fileName='" + fileName + '\'' +
                ", offset=" + offset +
                ", length=" + length +
                ", date='" + date + '\'' +
                ", line='" + line + '\'' +
                '}';
    }
}
