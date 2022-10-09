package com.choudhury.jwt.fx.jwt.api;

import com.choudhury.jwt.fx.jwt.model.GrantType;
import lombok.Value;

@Value
public class TokenRequestSettings {
    GrantType grantType;
    String authoriseURI;
    String tokenURI;
    String redirectURI;
    String clientId;
    String scope;
    boolean kerberos;
    boolean clientCertificate;
    boolean nativeKeyStore;
    boolean allowCircularRedirect;
    String codeChallenge;
    String codeVerifier;

    public boolean isPCKE(){
        return grantType == GrantType.PCKE;
    }

}
