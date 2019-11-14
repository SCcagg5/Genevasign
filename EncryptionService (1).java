package ch.ge.qualified_signature.gateway.service;

import ch.ge.qualified_signature.gateway.dao.OrganisationDao;
import ch.ge.qualified_signature.gateway.service.ethereum.EthereumService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jean on 05.12.17.
 */

@Component
public class EncryptionService {

    public static final String AES = "AES";
    public static final String RSA = "RSA";

    final static Logger log = LoggerFactory.getLogger(EncryptionService.class);

    private Map<String, PublicKey> publicKeys = new HashMap();

    private Cipher cipher;

    private Cipher symetricCipher;

    private OrganisationDao organisationDao;

    @Autowired
    public EncryptionService(OrganisationDao organisationDao) {
        this.organisationDao = organisationDao;
    }


    public void init()  {
        try {
            this.cipher = Cipher.getInstance(RSA);
            this.symetricCipher = Cipher.getInstance(AES);
            loadClientPublicKeys();
        } catch (Exception e) {
            throw new IllegalStateException("Errors occured during crypto keys loading", e);
        }
    }

    public byte[] generateRandomKey(int size) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[size];
        SecureRandom.getInstanceStrong().nextBytes(bytes);
        log.debug("random key generated  -  size :" + bytes.length);
        return bytes;
    }

    private SecretKeySpec generateKey(byte[] key)  {
        return new SecretKeySpec(key, AES);
    }

    public byte[] encryptWithSymetricKey(byte[] dataToEncrypt, byte[] key)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = generateKey(key);
        log.debug("encryptWithSymetricKey  -   key size :" + secretKeySpec.getEncoded().length);
        this.symetricCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return this.symetricCipher.doFinal(dataToEncrypt);
    }

    /**
     * Load the client's public keys
     *
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    private void loadClientPublicKeys() throws  NoSuchAlgorithmException, IOException, InvalidKeySpecException {

        Map<String, String> publicKeyByOrganisation = this.organisationDao.getAll().stream().collect(Collectors.toMap(o -> o.organisationId, o -> o.puk));

        for (Map.Entry<String, String> entry : publicKeyByOrganisation.entrySet()) {
            this.publicKeys.put(entry.getKey(), getPublic(entry.getValue()));
            log.debug("publicKey correctly loaded  : " + entry.getKey() + " - "+ entry.getValue());
        }
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
            throws  IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        log.debug("encryption with public key ");
        this.cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(cipher.doFinal(msg));
    }

    public String encryptText(byte[] msg, String organisationId) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        return encryptText(msg, this.publicKeys.get(organisationId));
    }


    /**
     * Build a Public key Object from the public key string
     *
     * @param base64KeyString
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey getPublic(String base64KeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new SSHEncodedToRSAPublicConverter(base64KeyString.getBytes()).convertToRSAPublicKey();
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }



}
