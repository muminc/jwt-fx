package com.choudhury.jwt.fx.jwt.api;

public interface JWTService {

    String obtainToken(String url, String redirectURI, String clientId, String scope, boolean kerberos, boolean clientCertificate, boolean nativeKeyStore, boolean allowCircularRedirect);
}
