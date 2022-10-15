package com.choudhury.jwt.fx;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyReader {

    public static PublicKey readCertificateOrPublicKey(String key) throws GeneralSecurityException {
        if (key.contains("BEGIN CERTIFICATE")) {
            return loadCertificate(key);
        }
        else {
            return readPublicKey(key);
        }
    }


    public static PublicKey readPublicKey(String input) throws GeneralSecurityException {

        String publicKeyPEM = input
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getMimeDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return  keyFactory.generatePublic(keySpec);
    }

    private static PublicKey loadCertificate(String asn1) throws  GeneralSecurityException {
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        InputStream targetStream = new ByteArrayInputStream(asn1.getBytes());
        X509Certificate cer = (X509Certificate) fact.generateCertificate(targetStream);
        return cer.getPublicKey();
    }
}
