package Service;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.stream.Stream;

//import org.apache.commons.codec.binary.Base64;

public class LogInService
{
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec ks;
    private SecretKeyFactory skf;
    private Cipher cipher;
    byte[] arrayBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;

    String filename;
    String password;

    public LogInService(String filename) throws Exception {
        this.filename = filename;
        myEncryptionKey = "SecretKeySecretKeyBiggerKey!.123";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        ks = new DESedeKeySpec(arrayBytes);
        skf = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = skf.generateSecret(ks);
        load();
    }


    private void load()
    {
        Path path = Paths.get(filename);
        Stream<String> lines;
        try
        {
            lines = Files.lines(path); //Files – helper class
            lines.forEach(s -> { password=s; });

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean existsPass()
    {
        if(password!=null)
            return true;
        return false;
    }

    public void createpass(String pass) {
        String encryptedString = null;
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = pass.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = new String(Base64.encode(encryptedText));
            writer.write(encryptedString);
            password=encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean logIn(String pass)
    {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decode(password);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
            if(pass.equals(decryptedText))
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
