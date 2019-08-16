package com.choudhury.jwt.fx.jwt.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.fxmisc.richtext.model.EditableStyledDocument;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.SimpleEditableStyledDocument;
import org.reactfx.collection.LiveList;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

public class JWTEditorModelNewer {



  private StringProperty algorithmProperty = new SimpleStringProperty("");
  private StringProperty expiryProperty = new SimpleStringProperty("");
  private StringProperty errorProperty = new SimpleStringProperty("");

  private Base64.Decoder decoder = Base64.getUrlDecoder();
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss ").withZone(ZoneId.systemDefault());
  private StringProperty signature = new SimpleStringProperty();
  private BooleanProperty signatureOkProperty = new SimpleBooleanProperty(false);


  private ObjectMapper mapper = new ObjectMapper();
  private final EditableStyledDocument<Collection<String>, String, Collection<String>> editorDocument = new SimpleEditableStyledDocument<>(Collections.emptyList(), Collections.emptyList());
  private final EditableStyledDocument<Collection<String>, String, Collection<String>> claimsAreaDocument = new SimpleEditableStyledDocument<>(Collections.emptyList(), Collections.emptyList());


  public JWTEditorModelNewer() {
    LiveList<Paragraph<Collection<String>, String, Collection<String>>> paragraphs = editorDocument.getParagraphs();


//    ObservableValue<String> stringObservableValue =  editorDocument.textProperty();
//   stringObservableValue.addListener((observable, oldValue, newValue) -> processUpdatedToken(newValue));
  }

  //  public JWTEditorModelNewer(StringProperty algorithmProperty,
//                             CaretSelectionBind<Collection<String>, String, Collection<String>> bodySelection,
//                             StringProperty expiryProperty,
//                             StringProperty signatureProperty,
//                             BooleanProperty signatureOk,
//                             StringProperty errorProperty
//  ) {
//
//    algorithmProperty.bind(this.algorithmProperty);
//    this.bodySelection = bodySelection;
//    expiryProperty.bind(this.expiryProperty);
//    errorProperty.bind(this.errorProperty);
//    signatureOk.bind(this.signatureOkProperty);
//
//
//
//    ObservableValue<String> stringObservableValue =  editorDocument.textProperty();
//    stringObservableValue.addListener((observable, oldValue, newValue) -> processUpdatedToken(newValue));
//    this.signature.bind(signatureProperty);
//    this.signature.addListener((observable, oldValue, newValue) -> processSignature(algorithmProperty.get(), stringObservableValue.getValue()));
//
//
//  }

  public EditableStyledDocument<Collection<String>, String, Collection<String>> getEditorDocument() {
    return editorDocument;
  }

  public EditableStyledDocument<Collection<String>, String, Collection<String>> getClaimsAreaDocument() {
    return claimsAreaDocument;
  }



}
