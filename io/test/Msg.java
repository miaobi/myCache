package log.io.test;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class Msg implements Comparable<Msg>{
    private String value;
    private LocalDateTime ldt;
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void clear(){
        this.value = null;
    }

    public LocalDateTime getLdt() {
        return ldt;
    }

    public void setLdt(LocalDateTime ldt) {
        this.ldt = ldt;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public int compareTo(@NotNull Msg o) {
        return this.getLdt().compareTo(o.getLdt());
    }


    public static void main(String[] args) {
        String ss = "11 [22/Oct/2017:00:00:01 +0800] 111 [ dd";
        System.out.println(ss.split(" ")[1]);
    }


}
