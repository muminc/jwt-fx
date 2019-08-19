package com.choudhury.jwt.fx.config;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AppSettings {
    private List<WindowSettings> windowSettings;


    public AppSettings() {
        windowSettings = new ArrayList<>();
    }
}
