module app {
    requires static lombok;
    requires java.logging;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.bouncycastle.provider;
    requires com.fasterxml.jackson.core;

    requires com.jfoenix;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;
    requires com.auth0.jwt;

    requires org.slf4j;

    requires com.fasterxml.jackson.databind;
    requires com.goxr3plus.fxborderlessscene;

    requires org.apache.httpcomponents.core5.httpcore5;
    requires org.apache.httpcomponents.client5.httpclient5;

    requires org.apache.httpcomponents.client5.httpclient5.win;



    requires jjwt.api;
    requires reactfx;
    requires org.apache.commons.lang3;




    opens com.choudhury.jwt.fx.editor to javafx.fxml;
    opens com.choudhury.jwt.fx.config to javafx.fxml;

    opens com.choudhury.jwt.fx.impl to javafx.fxml, com.fasterxml.jackson.databind;

    opens com.choudhury.jwt.fx to javafx.fxml;
    opens com.choudhury.jwt.fx.jwt to javafx.fxml;
    opens com.choudhury.jwt.fx.jwt.api to javafx.fxml;
    opens com.choudhury.jwt.fx.jwt.model to javafx.fxml;
    opens com.choudhury.jwt.fx.window to javafx.fxml;





    exports com.choudhury.jwt.fx.config;
    exports com.choudhury.jwt.fx.impl;
    exports com.choudhury.jwt.fx.editor;
    exports com.choudhury.jwt.fx;
    exports com.choudhury.jwt.fx.jwt;
    exports com.choudhury.jwt.fx.jwt.api;
    exports com.choudhury.jwt.fx.jwt.model;

    provides com.choudhury.jwt.fx.jwt.api.JWTService with com.choudhury.jwt.fx.impl.SSOJWTServiceImpl;

    uses com.choudhury.jwt.fx.jwt.api.JWTService;




}