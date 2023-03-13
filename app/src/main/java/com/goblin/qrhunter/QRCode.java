/**
 * This class represents a QR code object, with a hash value and a score based on the code's content.
 */
package com.goblin.qrhunter;


import com.google.firebase.firestore.Exclude;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class represents a QR code object, which contains a hash and a score.
 *
 * The hash is calculated from the QR code string using SHA-256 algorithm, and the score is calculated
 *
 * from the hash using a custom scoring system.
 */
public class QRCode {

    /**
     * The hash value of the QR code.
     */
    private String hash;

    /**
     * required by firebase
     */
    public QRCode() {

    }

    /**
     * Constructs a QR code object with the given code string.
     *
     * @param code The code string used to construct the QR code.
     */
    public QRCode(String code) {
        try {
            hash = toHexString(MessageDigest.getInstance("SHA-256").digest(code.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            // pass
        }
    }

    /**
     * Returns the hash value of the QR code.
     *
     * @return The hash value of the QR code.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the hash value of the QR code.
     *
     * @param hash The hash value to set.
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Returns the score of the QR code.
     *
     * @return The score of the QR code.
     */
    public int getScore() {
        //TODO: calculate score
        return calculateScore(hash);
    }

    /**
     * Calculates the score of the QR code based on its hash value.
     *
     * @param hex The hash value of the QR code.
     * @return The calculated score of the QR code.
     */
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

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to convert.
     * @return The hexadecimal string representation of the byte array.
     */
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

