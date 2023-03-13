package com.goblin.qrhunter.uiUnitTests;

import org.junit.Test;

import static org.junit.Assert.*;

import com.goblin.qrhunter.QRCode;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class QRCodeUnitTest {
    @Test
    public void testCalculateScore() throws NoSuchAlgorithmException {
        QRCode qr = new QRCode("hello world!");
        Map<Integer, String> testcases = new HashMap<>() {{
            put(20, "00");
            put(400, "000");
            put(8000, "0000");

            put(1, "111");

            put(4, "222");

            put(10, "aa");
            put(100, "aaa");

            put(15, "ff");

            put(30, "00baa3f");


        }};
        testcases.forEach((score, hash) -> {
            qr.setHash(hash);
            assertEquals((Integer) score, (Integer) qr.getScore());
        });

    }
}