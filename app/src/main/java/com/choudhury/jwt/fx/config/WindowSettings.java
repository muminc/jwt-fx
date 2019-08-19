package com.choudhury.jwt.fx.config;

import lombok.Data;

@Data
public class WindowSettings {

    private String session;
    private String scope;
    private String clientId;
    private String oauthURI;
    private String redirectURI;

    private boolean kerberos;
    private boolean clientCertificate;
    private boolean unrestrictedRedirect;




}
