package myyoucloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSA {

    public static final String ALGORITHM = "RSA";
    public static final String PRIVATE_KEY_FILE = "C:/keys/private.key";
    public static final String PUBLIC_KEY_FILE = "C:/keys/public.key";
    File privateKey = new File(PRIVATE_KEY_FILE);
    File publicKey = new File(PUBLIC_KEY_FILE);

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Prvate.key and Public.key files.
     */
    public static void generateKey() {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            // Create files to store public and private key
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();

            // Saving the Public key in a file
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(key.getPublic());
            publicKeyOS.close();

            // Saving the Private key in a file
            ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                    new FileOutputStream(privateKeyFile));
            privateKeyOS.writeObject(key.getPrivate());
            privateKeyOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean areKeysPresent() {
        if (privateKey.exists() && publicKey.exists()) {
            return true;
        }
        return false;
    }

    public static byte[] encrypt(byte[] text, String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
        byte[] cipherText = null;
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
        PublicKey key = (PublicKey) inputStream.readObject();;

        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(filename + " : " + cipherText);
        return cipherText;
    }

    public static String decrypt(byte[] text, String filename) throws IOException, ClassNotFoundException {
        byte[] dectyptedText = null;
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
        final PrivateKey key = (PrivateKey) inputStream.readObject();

        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(filename + " : " + dectyptedText);
        return new String(dectyptedText);
    }

    void checkKeys() {


        // Check if the pair of keys are present else generate those.
        if (!areKeysPresent()) {
            // Method generates a pair of keys using the RSA algorithm and stores it
            // in their respective files
            generateKey();
        }

        /*          final String originalText = "Nistane Ankush";
         ObjectInputStream inputStream = null;

         // Encrypt the string using the public key
         inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
         final PublicKey publicKey = (PublicKey) inputStream.readObject();
         final byte[] cipherText = encrypt(originalText, publicKey);

         // Decrypt the cipher text using the private key.
         inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
         final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
         final String plainText = decrypt(cipherText, privateKey);

         // Printing the Original, Encrypted and Decrypted Text
         System.out.println("Original: " + originalText);
         System.out.println("Encrypted: " + cipherText.toString());
         //      System.out.println("Decrypted: " + plainText);
         */
    }
}