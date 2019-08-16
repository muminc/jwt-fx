package com.choudhury.jwt.fx;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class KeyReader {

    public static PublicKey readPublicKey(String key) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        PemReader reader = new PemReader(new StringReader(key));
        PemObject pemObject = reader.readPemObject();
        byte[] content = pemObject.getContent();
        return getPublicKey(content, "RSA");
    }

    private static PublicKey getPublicKey(byte[] keyBytes, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey = null;
        KeyFactory kf = KeyFactory.getInstance(algorithm);
        EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return kf.generatePublic(keySpec);
    }
}
