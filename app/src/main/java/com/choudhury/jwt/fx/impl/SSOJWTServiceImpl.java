package com.choudhury.jwt.fx.impl;

import com.choudhury.jwt.fx.jwt.api.JWTService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.RequestLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.WinHttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SSOJWTServiceImpl implements JWTService {

    private Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String EXTENDED_USAGE_IDENTIFIER = "1.3.6.1.5.5.7.3.2";
    private static final String accessTokenTokenIdentifier="#access_token";
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";


    private Random random = new Random();


    private String generateString() {
        int length = 40;
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));
        }
        return builder.toString();
    }

    static class AccessTokenResult {
        String accessToken;
    }

    @Override
    public String obtainToken(String url, String redirectURI, String clientId, String scope, boolean kerberos, boolean clientCertificates, boolean nativeKeyStore, boolean allowCircularRedirect) {
        AccessTokenResult result = new AccessTokenResult();
        try {
            KeyStore keyStore = loadKeyStore(nativeKeyStore);
            SSLContext sslContext;
            if (clientCertificates) {
                sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, null, new PrivateKeyStrategy() {
                    @Override
                    public String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket) {
                        for (String alias : aliases.keySet()) {
                            PrivateKeyDetails privateKeyDetails = aliases.get(alias);
                            for (X509Certificate certificate : privateKeyDetails.getCertChain()) {
                                try {
                                    certificate.checkValidity();
                                    List<String> extKeyUsage = certificate.getExtendedKeyUsage();
                                    if (extKeyUsage != null && extKeyUsage.contains(EXTENDED_USAGE_IDENTIFIER)) {
                                        logger.info("Found a certificate with the required capabilities - alias [" + alias + "]");
                                        return alias;
                                    }
                                } catch (CertificateExpiredException | CertificateNotYetValidException | CertificateParsingException e) {
                                    logger.warn("Error checking certificate with alias [" + alias + "]", e);
                                    continue;
                                }
                            }
                        }
                        logger.info("Unable to find any candidate certificates with ext usage  [" + EXTENDED_USAGE_IDENTIFIER + "]");
                        return null;
                    }
                }).build();

            }
            else {
                sslContext = SSLContext.getDefault();
            }

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    new String[]{"TLSv1.2", "TLSv1.1"},
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());


            RequestConfig config = RequestConfig.custom().setCircularRedirectsAllowed(allowCircularRedirect).build();


            final HttpClientBuilder builder = kerberos ? WinHttpClients.custom() : HttpClients.custom();

            CloseableHttpClient client = builder
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setDefaultRequestConfig(config)
                    .setRedirectStrategy(new DefaultRedirectStrategy() {
                        @Override
                        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
                            final RequestLine requestLine = request.getRequestLine();
                            String uri = requestLine.getUri();
                            final boolean containsAccessToken = requestLine.getUri().contains(accessTokenTokenIdentifier);
                            if (containsAccessToken){
                                List<NameValuePair> parse = URLEncodedUtils.parse(uri, Charset.defaultCharset());
                                for (NameValuePair nameValuePair : parse) {
                                    if (nameValuePair.getName().contains(accessTokenTokenIdentifier)) {
                                        result.accessToken = nameValuePair.getValue();
                                    }
                                }
                            }
                            return !containsAccessToken;
                        }
                    })
                    .build();

            final String clientIdEncoded = encodeValue(clientId);
            final String state = generateString();
            final String redirectURIEncoded = encodeValue(redirectURI);
            final String scopeEncoded = encodeValue(scope);

            HttpGet request = new HttpGet(url+"?response_type=token&client_id="+clientIdEncoded+"&state="+state+"&redirect_uri="+redirectURIEncoded+"&scope="+scopeEncoded);

            CloseableHttpResponse execute = client.execute(request);
            final HttpEntity entity = execute.getEntity();
            if (entity!=null){
                final String response = EntityUtils.toString(entity);
                logger.debug("Response : "+response);
            }
            if (result.accessToken == null){
                throw new RuntimeException("Unable to obtain access token");
            }
            return result.accessToken;
        }
        catch (Exception e){
            logger.error("Exception occurred",e);
            throw new RuntimeException(e.getMessage(),e);
        }

    }

    private KeyStore loadKeyStore(boolean windowsKeyStore) throws GeneralSecurityException, IOException {
        KeyStore keyStore;
        if (windowsKeyStore) {
            keyStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");  // Windows KeyStore
            keyStore.load(null, new char[]{});  // TO

        } else {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());  // Default Key store
            keyStore.load(null, new char[]{});  // TO
        }
        return keyStore;
    }

    private static String encodeValue(String input){
        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }
}
