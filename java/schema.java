//decoder la cle symetrique:


import java.util.Base64;
import javax.crypto.Cipher; //Cipher
import java.security.spec.PKCS8EncodedKeySpec; //PKCS8EncodedKeySpec
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.KeyFactory;

public class schema {

    public static void main(String[] args) {
      String AES = "AES";
      String RSA = "RSA";

      Cipher cipher;
      Cipher symetricCipher;
      PrivateKey privateKey;
      SecretKeySpec secretKeySpec;
      String sym = "ZJx4Pvpk32WgyAw0gJztwUP5un0lqMT6n8XlwmLwWp7nfN+9VX6BfcuRQz1+bEsbb9fAYlF8pvyofu8v328C8Cd5ICs1MLYoj/hpsvocdE6z58XQeay8xxpW+tnGnQk7ROfsXqXRpUX9lhr5zwrVd9k8ohw4M4zOtGVoEq3/OcXaysMem8A2Td/fGHEVJaHi/BgekLjPBAh7xqrCoCSb9AbLnMvrZ6Az6G7d0urEvgJhwCWPMA1TPmWwPIfBGFbu0dpximQFLMsD3+2spW+bNzeS2YIWLfI3FClSyqx3IxopE5QEZ/rVB+mYj7/DFy2FC3Dg6yDhPrbd8q2iws/5pPT0cuKYvsBCqszlZF53UpZWnVJlxFWOjLoa48maRtswvOebRDiZovQB1yTxs8u8iBcGZuLzBLD8j7tOT/qSBcauyf4wXSvsYED74xWeb1Uef6E+cNJj5EYdCsCCkOS/5PGK/IVxkO/p+srkxvxsDu0SnmZr5aa02Em6u5+7O8PWOTbysEHm4ZUZmSBFDQCpEKc8EXbXrJ2Q8t1GIN9kRUvWJ/rJtKcBRh655ubtbMrZQZ0frxF5eKPZTVJc/crzAFqkkYXKzcRqvA4eYSv0ulNQ5EEW13aPfyLuQjYyAgLGgDqn0LVI4Br18OddceAkDutEsTn8pD16MkixtehL9gA=";
      String data = "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";
      try {
        cipher = Cipher.getInstance(RSA);
        symetricCipher = Cipher.getInstance(AES);

        File f = new File("./data/private_key.der");
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] privatekeyBytes = new byte[(int) f.length()];
        dis.readFully(privatekeyBytes);
        dis.close();

        byte[] signature = data.getBytes(StandardCharsets.UTF_8);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privatekeyBytes);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        privateKey = kf.generatePrivate(spec);

        //decrypt
        cipher.init(Cipher.DECRYPT_MODE, privateKey); //Cipher.DECRYPT_MODE <- int set in decrypt mode
        byte[] key = cipher.doFinal(Base64.getDecoder().decode(sym));
        secretKeySpec = new SecretKeySpec(key, AES);
        symetricCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        symetricCipher.doFinal(signature);
      } catch (Exception e) {
          throw new IllegalStateException("Errors occured uncrypting", e);
      }
    }

}
