package log.io.test;

public class IoBuffer {
    IoBuffer(long limit) {
        this.limit = limit;
        this.buf = new byte[(int)limit];
    }

    public long limit;
    public byte[] buf;
    public Slice slice;
    public int length;

    public void prepare() {
        this.position = 0;
        if (slice.skipFirstRecord) {
            getNextLine();
        }
    }

    public boolean hasNextItem() {
        if (slice.addPadding) {
            if (position <= slice.length) {
                return true;
            }
            return false;
        } else {
            if (position < slice.length) {
                return true;
            }
            return false;
        }
    }

    public String getNextLine() {
        String s = "";
        try{
            int pos1 = position;
            byte b = buf[position++];
            while (b != '\n') {
                b = buf[position++];
                if(position>=limit){
                    if(this.slice.addPadding){
                        return "";
                    }else {
                        this.length = slice.length-pos1;
                        s = new String(buf, pos1, slice.length);
                        return s;
                    }
                }
            }
            this.length = position-pos1-1;
            s = new String(buf, pos1, length);
            if (s.length() == 0) {
                return "";
            }
            return s;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName());
        }
       return "";
    }

    public int getNextLength() {
        int pos1 = position;

        byte b = buf[position++];
        while (b != '\n') {
            b = buf[position++];
        }
        this.length = position-pos1-1;
        return length;
    }

    public long getNextId() {
        byte b = buf[position++];
        long v = 0;
        while (b != '\n') {
            v = v * 10 + (b - '0');
            b = buf[position++];
        }
        return v;
    }

    public int position;
}
