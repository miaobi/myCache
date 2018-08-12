package Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestLog4j {
    private static Logger logger = LogManager.getLogger(TestLog4j.class);
    public static void main(String[] args) throws InterruptedException {
        System.out.println("111111");
        logger.info("test_log1");
        logger.info("test_log2");
        logger.info("test_log3");
        System.out.println("111111");

    }
}
