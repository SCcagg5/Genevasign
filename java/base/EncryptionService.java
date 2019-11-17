import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;

public class EncryptionService {

    	public static final String AES = "AES";
    	public static final String RSA = "RSA";

	private PrivateKey privateKey;

    	private Cipher cipher;

    	private Cipher symetricCipher;

    	public byte[] decryptText(String msg)
            	throws InvalidKeyException, UnsupportedEncodingException,
            	IllegalBlockSizeException, BadPaddingException {
        	this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        	return cipher.doFinal(Base64.decodeBase64(msg));
    	}

    	public static void main(String[] args) {
		this.cipher = Cipher.getInstance(RSA);
            	this.symetricCipher = Cipher.getInstance(AES);
            	Resource resource = resourceLoader.getResource("..\\private.pem");
        	byte[] bytes = IOUtils.toByteArray(resource.getInputStream());
        	this.privateKey = this.loadPrivate(bytes);
        	decryptText("aKs9+tOB9OjiUlHluNrXsWi6ZN8iOgZPTULNvWNi1UH6U8MTgPVd3c4ynbNNr6/nhoh0tctvtDin4FKwMvxmHCDtbvgOQJzC4cATcjRPwvQ4/UmiIe/y+dpCeSSnYLBu8q/EP58vggjJe7VsSy5LqnAKm8eFleZUEjBi0pYh283EPZfoO0RWW5hjkaiBI98DguWxnv1Jxq9UYVbJ18+GB1FRrxl7mTYBzdWGUy0niLP6YWVRmw7EnEe6/V+IPVacWx+hnFUzjDz5kEXv9PgcFOaLR/vZrUDOIYhxIuizMmIIu6R5fqp4PS5WGfik62DUEKmHtds0gdTdUF9qW8XZRfzjMJ4lcoa3ZdeguVdqtKDZ2+/Z4yyswCUHatTjgf8sNeB0GXscF2E+RRk2zWCfO6Lt5c/vxQCojNDALMfGH5EGPSak4iJxvGP+JAbZhDkAf1JI1/F/4r8KoCFnikbqlRiUe5T8BAWQOsAj69+W/czKNf6vUxtj5AdJ9GlLEQSxeK/fHWjuwpz2b4C558uXXE0jGTrDyz/IiZRdZfR7t93ySmE3XdMZ9qUnIvxue/0HWkxqTMYbr7AeL7OMX+CxGlPPStGiWpTWpZdJ0j/GInZba50n1+u+d7vZYbba/8FjXB/YQKufzRxOvuX9KL4jdXX1e5m2lRqEATmLDoecWnA=");
        	System.out.println("Hello, World");
    	}
}
