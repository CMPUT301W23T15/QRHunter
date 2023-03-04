package com.goblin.qrhunter;


import com.google.firebase.firestore.Exclude;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QRCode {

    private String hash;
    private int score;

    public QRCode(String code) throws NoSuchAlgorithmException {
        hash = toHexString(MessageDigest.getInstance("SHA-256").digest(code.getBytes(StandardCharsets.UTF_8)));
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getScore() {
        //TODO: calculate score
        return calculateScore(hash);
    }


    public static int calculateScore(String hex) {
        // TODO: determine if should be in post or qrcode
        int score = 0;
        // find repeat characters
        Matcher matcher = Pattern.compile("(.)\\1+").matcher(hex);

        // Iter over each group of repeats
        while (matcher.find()) {
            int repeats = matcher.group().length();
            char c = matcher.group().charAt(0);
            // special case 0, else use the hex equivalent value
            int base = (c == '0') ? 20 : Character.digit(c, 16);
            // if base is less than 0, invalid hex just ignore.
            if( base > -1) {
                score += Math.pow(base, repeats - 1);
            }
        }
        return score;
    }

    @Exclude
    public static String toHexString(byte[] bytes) {
        // https://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l/2197650#2197650
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v/16];
            hexChars[j*2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }


}
