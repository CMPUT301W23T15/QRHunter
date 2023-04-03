package com.goblin.qrhunter.uiUnitTests;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.goblin.qrhunter.QRCode;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


/**
 * Going to refactor this so it will look similar to other tests using a mock object
 * also going to test the other methods used by this class
 */
public class QRCodeUnitTest {

    private QRCode qrCode;

    public QRCode MockQRCode(){
        qrCode = new QRCode("hello world");
        return qrCode;
    }

    /**
     * also tests getScore(has calculateScore), setHash, getHash
     */
    @Test
    public void testCalculateScore() throws NoSuchAlgorithmException {
        qrCode = MockQRCode();
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
            qrCode.setHash(hash);
            assertNotNull(qrCode.getHash());
            assertEquals(score, (Integer) qrCode.getScore());
        });

    }

    @Test
    public void NameGeneratorTest() throws NumberFormatException{
        qrCode = MockQRCode();
        String hash = qrCode.getHash();
        assertNotNull(hash);
        String name = qrCode.NameGenerator();
        assertNotNull(name);
//        manually went to see what name was
        assertEquals(name, "S-Likeable-Gligar");
    }

    @Test
    public void equalsTest(){
        qrCode = MockQRCode();
        QRCode qrCode1 = MockQRCode();
        assertTrue(qrCode.equals(qrCode1));
    }

}