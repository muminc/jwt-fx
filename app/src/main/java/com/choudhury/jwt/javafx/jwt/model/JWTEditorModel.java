package com.choudhury.jwt.javafx.jwt.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.choudhury.jwt.javafx.KeyReader;
import com.choudhury.jwt.javafx.model.TaskModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.fxmisc.richtext.CaretSelectionBind;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.EditableStyledDocument;
import org.fxmisc.richtext.model.ReadOnlyStyledDocument;
import org.fxmisc.richtext.model.SimpleEditableStyledDocument;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class JWTEditorModel {



  private StringProperty algorithmProperty = new SimpleStringProperty("");
  private CaretSelectionBind<Collection<String>, String, Collection<String>> bodySelection;
  private StringProperty expiryProperty = new SimpleStringProperty("");
  private StringProperty errorProperty = new SimpleStringProperty("");

  private Base64.Decoder decoder = Base64.getUrlDecoder();
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ").withZone(ZoneId.systemDefault());
  private StringProperty signature = new SimpleStringProperty();
  private BooleanProperty signatureOkProperty = new SimpleBooleanProperty(false);


  private ObjectMapper mapper = new ObjectMapper();
  private final GenericStyledArea<Collection<String>, String, Collection<String>> area;

  public JWTEditorModel(

                        CaretSelectionBind<Collection<String>, String, Collection<String>> editorSelection,
                        StringProperty algorithmProperty,
                        CaretSelectionBind<Collection<String>, String, Collection<String>> bodySelection,
                        StringProperty expiryProperty,
                        StringProperty signatureProperty,
                        BooleanProperty signatureOk,
                        StringProperty errorProperty
  ) {

    algorithmProperty.bind(this.algorithmProperty);
    this.bodySelection = bodySelection;
    expiryProperty.bind(this.expiryProperty);
    errorProperty.bind(this.errorProperty);
    signatureOk.bind(this.signatureOkProperty);

    area = editorSelection.getUnderlyingSelection().getArea();

    ObservableValue<String> stringObservableValue =  area.textProperty();
    stringObservableValue.addListener((observable, oldValue, newValue) -> processUpdatedToken(newValue));
    this.signature.bind(signatureProperty);
    this.signature.addListener((observable, oldValue, newValue) -> processSignature(algorithmProperty.get(), stringObservableValue.getValue()));


  }


  public void setToken(String token){
    area.clear();
    area.insertText(0,token);
  }



  private void processUpdatedToken(String token) {
    try {
      DecodedJWT jwt = JWT.decode(token);
      String header = jwt.getHeader();
      algorithmProperty.setValue(decodeAndPrettyPrint(header));
      String payload = jwt.getPayload();
      GenericStyledArea<Collection<String>, String, Collection<String>> bodyArea = bodySelection.getUnderlyingSelection().getArea();


      bodyArea.clear();
      bodyArea.insertText(0, decodeAndPrettyPrint(payload));

      Date expiresAt = jwt.getExpiresAt();
      if (expiresAt != null) {
        Instant instant = expiresAt.toInstant();
        String formatValue = formatter.format(instant);
        expiryProperty.setValue(formatValue);
      } else {
        expiryProperty.setValue("");
      }



    } catch (JWTDecodeException | IOException | IllegalArgumentException exception) {
      //Invalid token
      algorithmProperty.setValue("ERROR");
      GenericStyledArea<Collection<String>, String, Collection<String>> area = bodySelection.getUnderlyingSelection().getArea();
      area.clear();
      area.insertText(0, "ERROR");
    }


    processSignature(algorithmProperty.get(),token);


  }

  private void processSignature(String algorithm, String token) {
    errorProperty.setValue("");
    try {
      boolean isUsingSecretkey = true;
      if (!algorithm.contains("HS")) {
        isUsingSecretkey = false;
      }

      JwtParser parser = Jwts.parser();

      if (isUsingSecretkey) {
        parser.setSigningKey(signature.getValue().getBytes(StandardCharsets.UTF_8));
      } else {
        parser.setSigningKey(KeyReader.readPublicKey(signature.get()));
      }
      Claims claims = parser
              .parseClaimsJws(token).getBody();
      signatureOkProperty.setValue(true);
    } catch (Exception e) {
      errorProperty.setValue(e.getMessage());
      signatureOkProperty.setValue(false);
    }
  }




  public String getSignature() {
    return signature.get();
  }

  public StringProperty signatureProperty() {
    return signature;
  }

  private String decodeAndPrettyPrint(String input) throws IOException {
    try {
      String decoded = new String(decoder.decode(input));
      Object object = mapper.readValue(decoded, Object.class);
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }catch (IllegalArgumentException e){
      return "ERROR";
    }

  }

}
