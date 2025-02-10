package de.htwberlin.dbtech.bsp.test;

public class SimpleService {
    public String hw() {
        return "hw";
    }

    public String exe(int code) {
        if (code == 0) {
            return "exe";
        } else {
            throw new RuntimeException("falscher code");
        }
    }
}
