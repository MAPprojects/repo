package Service;

import Domain.User;
import Repository.IRepository;
import ValidatorsAndExceptions.ServiceException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;


import org.bouncycastle.util.encoders.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.stream.Stream;


public class AccountService {

    private IRepository<User,String> repoUser;
    private ArrayList<String> profesori;
    private String fileName;

//    private static final String UNICODE_FORMAT = "UTF8";
//    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
//    private KeySpec ks;
//    private SecretKeyFactory skf;
//    private Cipher cipher;
//    byte[] arrayBytes;
//    private String myEncryptionKey;
//    private String myEncryptionScheme;
//    SecretKey key;
    EncryptionService encryptionService;

    public AccountService(IRepository<User, String> repoUser,String fileName)
    {
        this.repoUser = repoUser;
        this.fileName=fileName;
        encryptionService=new EncryptionService();
//        try {
//            myEncryptionKey = "The-Longest.Secret+Key*123456";
//            myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
//            arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
//            ks = new DESedeKeySpec(arrayBytes);
//            skf = SecretKeyFactory.getInstance(myEncryptionScheme);
//            cipher = Cipher.getInstance(myEncryptionScheme);
//            key = skf.generateSecret(ks);
//        }catch (Exception e){System.err.println("error init secret key");}

        initProfesori();
    }

    private void initProfesori() {
            profesori=new ArrayList<>();
            Path path= Paths.get(fileName);
            Stream<String> lines;
            try
            {
                lines= Files.lines(path);
                lines.forEach(instance->profesori.add(instance));
            }catch (IOException e)
            {
                System.err.println("Can't load data from file "+fileName+"\n");
            }
        }



    public User Register(String email,String password,String confirmPassword)
    {
        boolean autorizat=false;
        for (String profesor:profesori)
        {
            if(profesor.equals(email))
            {
                autorizat=true;
                break;
            }
        }
        if(password.equals(confirmPassword))
        {
            User user;
            String passwordEncrypted=encryptionService.EncryptPassword(password);
            if(autorizat==true)
            {
                user=new User(email,passwordEncrypted,"profesor");
            }
            else
            {
                user=new User(email,passwordEncrypted,"vizitator");
            }
            repoUser.add(user);
            return user;
        }
        return  null;
    }

//    private String EncryptPassword(String pass) {
//           String encryptedString = null;
//           try {
//
//               cipher.init(Cipher.ENCRYPT_MODE, key);
//               byte[] plainText = pass.getBytes(UNICODE_FORMAT);
//               byte[] encryptedText = cipher.doFinal(plainText);
//               encryptedString = new String(Base64.encode(encryptedText));
//           }catch(Exception e)
//           {
//               System.err.println("Can't encrypt pasword");
//           }
//
//            return encryptedString;
//
//    }

    public User Login(String email, String password)
    {
        String role="unknown";
        User userActive=null;
        String decryptedText=null;
        for (User user:repoUser.getAll())
        {
            try {
//                cipher.init(Cipher.DECRYPT_MODE, key);
//                byte[] encryptedText = Base64.decode(user.getPassword());
//                byte[] plainText = cipher.doFinal(encryptedText);
                decryptedText= encryptionService.DecryptPassword(user.getPassword());
                if(user.getEmail().equals(email) && password.equals(decryptedText))
                {
                    userActive=user;
                    role=userActive.getRol();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (role.equals("unknown"))
        {
            throw new ServiceException("There is no user with these credentials.");
        }
         return userActive;
    }

    public void RemindPassword(String email)
    {
        for (User user:repoUser.getAll()) {
            if (user.getEmail().equals(email)) {
                MailSender.sendPassword(user);
                return;
            }
        }
        throw new ServiceException("There is no user with this email");
    }
}
