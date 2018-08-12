package log.io.test;




import java.sql.Timestamp;
import java.text.ParseException;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class TestDate {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'['dd/MMM/yyyy:HH:mm:ss Z']'",Locale.CANADA);
    private static DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("'['dd/MMM/yyyy:HH:mm:ss",Locale.CANADA);
    private static DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("HH:mm:ss",Locale.CANADA);

    private static SimpleDateFormat sdf = new SimpleDateFormat("'['dd/MMM/yyyy:HH:mm:ss",Locale.CANADA);

    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss");
//        String dateStr = "22/Oct/2017:00:00:41";
//
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d LLL yyyy HH:mm:ss Z");
//        dateStr = "4 Jul 2001 12:08:56 -0700";
//
//        LocalDateTime ldt = LocalDateTime.parse(dateStr,dtf);
//        System.out.println(ldt);
        String goodFriday = "[22/Oct/2017:00:00:41 +0800]";
        String date2 = "[22/Oct/2017:00:00:42";
        String date3 = "22/Oct/2017:00:00:42";
        String line = "11.130.147.62 - - - [22/Oct/2017:00:11:09 +0800] \"POST /ye.1688.com/industrybelt/wpconfig/fetch_config.json?from=ye&bizId=huadu.1688.com&memberId=b2b-26763126254476d HTTP/1.1\" 200 1778 15421 \"http://kylin.1688.com\" \"Jakarta Commons-HttpClient/3.1\" - - \"a=-; b=-; c=-\" - 43540 industry-web011183236062.et2\n";

        String test = "[]";
        System.out.println(test.getBytes()[0]+"|"+test.getBytes()[1]);

        try {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 4000000; i++) {
                LocalDateTime holiday = LocalDateTime.parse(goodFriday, formatter);
            }
            System.out.println("formatter:"+(System.currentTimeMillis()-start));
            start = System.currentTimeMillis();
            for (int i = 0; i < 4000000; i++) {
                LocalDateTime holiday2 = LocalDateTime.parse(date2, formatter2);
            }
            System.out.println("formatter2:"+(System.currentTimeMillis()-start));
            start = System.currentTimeMillis();
            for (int i = 0; i < 4000000; i++) {
                Date date = sdf.parse(date2);
            }
            System.out.println("sdf:"+(System.currentTimeMillis()-start));
            start = System.currentTimeMillis();
            for (int i = 0; i < 4000000; i++) {
                LocalDateTime holiday2 = LocalDateTime.parse(date3, formatter3);
//                Timestamp ts = Timestamp.valueOf(holiday2);
//                long tm = ts.getTime();
            }
            System.out.println("formatter3:"+(System.currentTimeMillis()-start));
            start = System.currentTimeMillis();
            for (int i = 0; i < 4000000; i++) {
                String date = line.split(" ")[4];
            }
            System.out.println("split:"+(System.currentTimeMillis()-start));




        } catch (DateTimeParseException ex) {

            System.out.printf("%s is not parsable!%n", goodFriday);

            ex.printStackTrace();

        }

    }
    public static LocalDateTime getDate(String dataStr){
        return LocalDateTime.parse(dataStr, formatter);
    }
    public static LocalDateTime getDate2(String dataStr){
        return LocalDateTime.parse(dataStr, formatter2);
    }
    public static LocalTime getTime3(String dataStr){
        return LocalTime.parse(dataStr, formatter3);
    }
}
