//decoder la cle symetrique:


import java.util.Base64;
import javax.crypto.Cipher; //Cipher
import java.security.spec.PKCS8EncodedKeySpec; //PKCS8EncodedKeySpec
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.nio.file.*;

public class schema {

    public static void main(String[] args) {
      String AES = "AES";
      String RSA = "RSA";

      Cipher cipher;
      Cipher symetricCipher;
      PrivateKey privateKey;
      SecretKeySpec secretKeySpec;

      try {
        cipher = Cipher.getInstance(RSA);
        symetricCipher = Cipher.getInstance(AES);

        Path fileLocation = Paths.get("./data/privatekey.der");
        byte[] privatekeyBytes =  Files.readAllBytes(fileLocation);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privatekeyBytes);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        privateKey = kf.generatePrivate(spec);

        //decrypt
        cipher.init(Cipher.DECRYPT_MODE, privateKey); //Cipher.DECRYPT_MODE <- int set in decrypt mode
        Path myKeyFileLocation = Paths.get("./data/symetric_key_encrypted.txt");
        byte[] myKey =  Files.readAllBytes(myKeyFileLocation);
        byte[] key = cipher.doFinal(Base64.getDecoder().decode(myKey));
        secretKeySpec = new SecretKeySpec(key, AES);

        symetricCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        Path signatureFileLocation = Paths.get("./data/encryptedSignature.txt");
        byte[] signature =  Files.readAllBytes(signatureFileLocation);
        byte[] decryptedSignature = symetricCipher.doFinal(signature);

        Files.createFile(Paths.get("/uncryptedSignature.crt"));
        Files.write(Paths.get("/uncryptedSignature.crt"), signature);

      } catch (Exception e) {
          throw new IllegalStateException("Errors occured uncrypting", e);
      }
    }

}
