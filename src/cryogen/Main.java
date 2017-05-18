package cryogen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * Main class to be executed first.
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */
public class Main extends Application
{
    @Override
    public void start(Stage cgWindow) throws Exception
    {
        cgWindow.initStyle(StageStyle.DECORATED);
        cgWindow.getIcons().add(new Image(getClass().getResourceAsStream("/icons/cryogen/icon.png")));
        cgWindow.setTitle("Cryptogen V1.0");
        FXMLLoader loader;
        if(System.getProperty("os.name").startsWith("Windows"))
            loader = new FXMLLoader(getClass().getResource("CryptogenWindows.fxml"));
        else
            loader = new FXMLLoader(getClass().getResource("Cryptogen.fxml"));
        cgWindow.setHeight(605);
        cgWindow.setWidth(720);
        cgWindow.setResizable(false);
        cgWindow.setScene(createScene(loader.load()));
        cgWindow.getScene().getStylesheets().add("BreathDark.css");
        Cryptogen cg = loader.getController();
        cg.initialize(cgWindow);
        cgWindow.show();
    }

    private Scene createScene(Pane layout)
    {
        return new Scene(layout, Color.TRANSPARENT);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
