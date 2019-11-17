//decoder la cle symetrique:

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;


import org.apache.commons.io.IOUtils; //IOUtils
import org.apache.commons.codec.binary.Base64;
//from spring //ResourceLoader
import javax.crypto.Cipher; //Cipher
import java.security.spec.PKCS8EncodedKeySpec; //PKCS8EncodedKeySpec
import java.security.*; //KeyFactory / PrivateKey


public class schema {

    public static void main(String[] args) {
      String AES = "AES";
      String RSA = "RSA";
      String msg = "mW7UABCvl6BGeW5w1s9gXrx2S+CJDC3CydWE8ExbLC2NAPrzEYlzRyZ7TRH/wmPjZ3/Q6912/jZ9hXnaRcHQAxtZ3VsA4mZHA6qOSrGCHUO/MurGJFlQMS6CyjZ2Bzs159yg2Xe8/jy/F89hSgJL9qqiT9phsjWU8+0e6UewjUu+dZdfCB3+NfHQ69wwtK+ZXB6e9S2iBuQlvMkyN4AbsyW2S4NVyjVmFwUMSSeIsWxtwMjy47bNcV6AMZzfm5eqbSDLOemJz8upoDWR2nXH7jeJDNl8114nVxh1gLkM0IXW33sjA8bq+6yOM1nSq0KJpLLvEuCr9S8W48ph6P46s2mAuHZ+4QORcClAQfMg+tHiGM/vi1JrvhxRUiZE6j3HCX6ef53H+GA8zoXgAT5/3GC9t5VdSWjfQv2jEipyYUi+PcVpVuGJ9F9a2qZAbz88Qc/NXI6dSKia+/XMRxerhB1NLti1+9MmAOz+n/dYymu17DZQWIFI4pb60WVRKbitn5oCyF8XB6QpSJhJnXHMoBtn4UFYKwMFI1BXy7RGvtJN5lzGOPMwlO9/+islEWrmBylPGi52cQEBs4Qk08PAtXaBwIWt1JYTgod9bxV3pcsfyHFviRzcF6ZYdUreggxqRKMYciBXGJUf6yxWTjM2Gw/QBdCka49xhE8/XxAhN4w=";

      Cipher cipher;
      Cipher symetricCipher;

      PrivateKey privateKey;
      ResourceLoader resourceLoader = new DefaultResourceLoader();

      try {
        cipher = Cipher.getInstance(RSA);
        symetricCipher = Cipher.getInstance(AES);

        //file to private key
        Resource resource = resourceLoader.getResource("classpath:private.pem"); //l1
        byte[] bytes = IOUtils.toByteArray(resource.getInputStream()); //l2  -> from file to array of byte
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes); //byte to ASN.1 encoding
        KeyFactory kf = KeyFactory.getInstance("RSA");
        privateKey = kf.generatePrivate(spec);

        //decrypt
        cipher.init(Cipher.DECRYPT_MODE, privateKey); //Cipher.DECRYPT_MODE <- int set in decrypt mode
        cipher.doFinal(Base64.decodeBase64(msg));
      } catch (Exception e) {
          throw new IllegalStateException("Errors occured during crypto keys loading", e);
      }
    }

}
