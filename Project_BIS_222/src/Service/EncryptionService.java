package Service;

import org.bouncycastle.util.encoders.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

public class EncryptionService {

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    public EncryptionService()
    {
        try {
            myEncryptionKey = "The-Longest.Secret+Key*123456";
            myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
            arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
            ks = new DESedeKeySpec(arrayBytes);
            skf = SecretKeyFactory.getInstance(myEncryptionScheme);
            cipher = Cipher.getInstance(myEncryptionScheme);
            key = skf.generateSecret(ks);
        }catch (Exception e){System.err.println("error init secret key");}
    }
    public String EncryptPassword(String pass) {
        String encryptedString = null;
        try {

            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = pass.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encode(encryptedText));
        }catch(Exception e)
        {
            System.err.println("Can't encrypt pasword");
        }

        return encryptedString;

    }

    public String DecryptPassword(String pass) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] encryptedText = Base64.decode(pass);
        byte[] plainText = new byte[0];
        try {
            plainText = cipher.doFinal(encryptedText);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new String(plainText);
    }



}
