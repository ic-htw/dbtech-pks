package de.htwberlin.dbtech.bsp.intro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logging {
    private static final Logger L = LoggerFactory.getLogger(Logging.class);

    public static void main(String[] args) {
        L.info("main - start");
        m1(10);
        L.info("main - ende");
    }

    public static void m1(int i) {
        L.info("m1 - start");
        L.debug("i: " + i);
        int v = i;
        System.out.println(v);
        L.info("m1 - ende");
    }

}
