package com.cherlshall.butterfly.common;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by htf on 2019/8/5.
 */
public class TokenUtil {

    public static String create(int id) {
        return id + "_" + System.currentTimeMillis() + "_" + ThreadLocalRandom.current().nextInt(10000);
    }

    public static int getId(String token) {
        return Integer.parseInt(token.substring(0, token.indexOf("_")));
    }
}
