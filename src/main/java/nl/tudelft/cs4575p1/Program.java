package nl.tudelft.cs4575p1;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Random;

public sealed interface Program permits
    SingleThreadedProgram,
    MultiThreadedProgram,
    MultiThreadedCachedProgram {

    void run() throws Exception;

    static byte[] task(int value) {
        try {
            KeySpec spec = new PBEKeySpec(Integer.toHexString(value).toCharArray(), Task.SALT, 100_000, 256);
            SecretKey key = Task.KEY_FACTORY.generateSecret(spec);
            return key.getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Task {
    static final SecretKeyFactory KEY_FACTORY;

    static final byte[] SALT = new byte[16];

    static {
        try {
            KEY_FACTORY = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        new Random(0L).nextBytes(SALT);
    }
}
