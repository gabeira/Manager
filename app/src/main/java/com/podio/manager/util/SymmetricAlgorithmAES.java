package com.podio.manager.util;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricAlgorithmAES {

    private static final String TAG = SymmetricAlgorithmAES.class.getSimpleName();
    private SecretKeySpec sks;
    private static SymmetricAlgorithmAES instance = null;

    protected SymmetricAlgorithmAES() {
    }
    public static SymmetricAlgorithmAES getInstance() {
        if(instance == null) {
            instance = new SymmetricAlgorithmAES();
        }
        // Set up secret key spec for 128-bit AES encryption and decryption
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG","Crypto");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            instance.sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }
        return instance;
    }

    public static String encode(String theTestText) {
        if (null == theTestText || theTestText.length()<1) return "";
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, instance.sks);
            encodedBytes = c.doFinal(theTestText.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error");
        }
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    public static String decode(String encoded) {
        if (null == encoded || encoded.isEmpty()) return "";
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, instance.sks);
            decodedBytes = c.doFinal(Base64.decode(encoded, Base64.DEFAULT) );
        } catch (Exception e) {
            Log.e(TAG, "AES decryption error");
            e.printStackTrace();
        }
        return new String(decodedBytes);
    }
}