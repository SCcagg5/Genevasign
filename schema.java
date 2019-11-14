//decoder la cle symetrique:


import org.apache.commons.io.IOUtils; //IOUtils
//from spring //ResourceLoader
import javax.crypto.Cipher; //Cipher
import java.security.spec.PKCS8EncodedKeySpec; //PKCS8EncodedKeySpec
import java.security.KeyFactory; //KeyFactory


String AES = "AES";
String RSA = "RSA";

PrivateKey privateKey;
ResourceLoader resourceLoader;

cipher = Cipher.getInstance(RSA);
symetricCipher = Cipher.getInstance(AES);

//file to private key
Resource resource = resourceLoader.getResource("classpath:privatekey.der"); //l1
byte[] bytes = IOUtils.toByteArray(resource.getInputStream()); //l2  -> from file to array of byte
PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes); //byte to ASN.1 encoding
KeyFactory kf = KeyFactory.getInstance("RSA");
privateKey = kf.generatePrivate(spec);

//decrypt
cipher.init(Cipher.DECRYPT_MODE, privateKey); //Cipher.DECRYPT_MODE <- int set in decrypt mode
cipher.doFinal(Base64.decodeBase64(msg));
