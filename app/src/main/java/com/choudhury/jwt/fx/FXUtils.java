package com.choudhury.jwt.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import lombok.SneakyThrows;

public class FXUtils {



    @SneakyThrows
    public static <T, R extends Node> R loadAndInitialize(Class clazz, String file, T controller){
        FXMLLoader loader = new FXMLLoader(clazz.getResource(file));
        loader.setControllerFactory(c -> controller);
        R root = loader.load();
        // to prevent GCing of controller
        // see https://github.com/sialcasa/mvvmFX/issues/429#issuecomment-238829854 for more details
        root.setUserData(controller);

        return root;
    }

}
