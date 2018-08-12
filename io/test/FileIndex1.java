package log.io.test;

import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;


public class FileIndex1 implements Comparable<FileIndex1>{
    private String fileName;
    private long offset;
    private long length;
    private LocalTime date;
    private String line;
    private String dayOfMonth;

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

    public LocalTime getDate() {
        return date;
    }

    public void setDate(LocalTime date) {
        this.date = date;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    public int compareTo(@NotNull FileIndex1 o) {
        return this.date.compareTo(o.date);
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
