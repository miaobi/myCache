package myDisruptor;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class Event implements Comparable<Event>{
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
    public int compareTo(@NotNull Event o) {
        return this.getLdt().compareTo(o.getLdt());
    }


}
