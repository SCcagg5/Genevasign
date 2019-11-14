package ch.ge.qualified_signature.business.mockup.service;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;

/**
 * @author jean on 05.12.17.
 */

@Service
public class EncryptionService {

    public static final String AES = "AES";
    public static final String RSA = "RSA";

    private PrivateKey privateKey;

    private Cipher cipher;

    private Cipher symetricCipher;

    private ResourceLoader resourceLoader;

    @Autowired
    public EncryptionService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            this.cipher = Cipher.getInstance(RSA);
            this.symetricCipher = Cipher.getInstance(AES);
            loadPrivateKeys();
        } catch (Exception e) {
            throw new IllegalStateException("Errors occured during crypto keys loading", e);
        }
    }

    private void loadPrivateKeys() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        Resource resource = resourceLoader.getResource("classpath:privatekey.der");
        byte[] bytes = IOUtils.toByteArray(resource.getInputStream());
        this.privateKey = this.loadPrivate(bytes);
    }

    public byte[] generateRandomKey(int size) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[size];
        SecureRandom.getInstanceStrong().nextBytes(bytes);
        return bytes;
    }

    private SecretKeySpec generateKey(byte[] key) throws UnsupportedEncodingException {
        return new SecretKeySpec(key, AES);
    }

    public byte[] encryptWithSymetricKey(byte[] dataToEncrypt, byte[] key)
            throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = generateKey(key);

        this.symetricCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return this.symetricCipher.doFinal(dataToEncrypt);
    }

    public byte[] decryptWithSymetricKey(byte[] data, byte[] key)
            throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = generateKey(key);

        this.symetricCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return this.symetricCipher.doFinal(data);
    }

    /**
     * Encrypt data with authority public key
     *
     * @param msg data to encrypt
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    private String encryptText(byte[] msg, PublicKey publicKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(cipher.doFinal(msg));
    }

    public String getSignature(String privateKeyPath, String swissId) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        Signature dsa = Signature.getInstance("SHA256withRSA");
        dsa.initSign(getPrivate(privateKeyPath));
        dsa.update(swissId.getBytes());
        return new String(dsa.sign());
    }

    /**
     * Decrypt data with the private key
     *
     * @param msg data to decrypt
     * @return
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] decryptText(String msg)
            throws InvalidKeyException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException {
        this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        return cipher.doFinal(Base64.decodeBase64(msg));
    }

    /**
     * Build a Private key Object from the private key file
     *
     * @param filename private key file
     * @return PrivateKey bean
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PrivateKey getPrivate(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        return loadPrivate(keyBytes);
    }

    /**
     * Load the private key
     *
     * @param privateKey byte array of the private key
     * @return Private key
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PrivateKey loadPrivate(byte[] privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

}
