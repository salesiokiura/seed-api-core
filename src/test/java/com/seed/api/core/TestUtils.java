package com.seed.api.core;

import java.net.ServerSocket;
import java.util.Random;
import java.security.InvalidParameterException;

public class TestUtils {

    public static String errToStr(Exception e){
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getName() + ": " + e.getMessage());
        StackTraceElement[] stack = e.getStackTrace();
        if (stack != null){
            for (StackTraceElement s : stack) {
                sb.append("\n " + s.toString());
            }
        }

        return sb.toString();
    }

    public static String errToStr(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getClass().getName() + ": " + e.getMessage());
        StackTraceElement[] stack = e.getStackTrace();
        if (stack != null){
            for (StackTraceElement s : stack) {
                sb.append("\n " + s.toString());
            }
        }

        return sb.toString();
    }

    /**
     * Gets a free port for use in testing.
     * @return The port.
     */
    public static int getFreePort() {
        int port = -1;
        int retres = 0;
        do {
            try {
                ServerSocket soc = new ServerSocket(getRandomPortInt());
                port = soc.getLocalPort();
                soc.close();
            } catch (Exception e) {
                retres++;
            }

            if (retres == 400) {
                throw new InvalidParameterException("All ports used up.");
            }
        } while (port == -1);

        return port;
    }

    /**
     * Generates a randon number between a port range.
     * @return The random number generated.
     */
    public static int getRandomPortInt() {
        Random r = new Random();
        int low = 8080;
        int high = 8442;
        return r.nextInt(high-low) + low;
    }
    
}
