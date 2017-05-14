package cryogen;

import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * This class is used for the progress bar utility
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */
public class Progress
{
    private final Stage dialogStage;
    private final ProgressBar pb = new ProgressBar();

    /**
     *
     */
    public Progress()
    {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("File Encryption Progress");

        pb.setProgress(-1F);

        final VBox hb = new VBox();
        hb.setPrefWidth(332);
        hb.setPrefHeight(90);
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(pb);
        pb.setPrefSize(260, 20);
        pb.setLayoutX(1);

        hb.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        pb.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        hb.getStyleClass().add("anchorPaneDefault");
        pb.getStyleClass().add("progress-barDefault");

        Scene scene = new Scene(hb);
        dialogStage.setScene(scene);
    }

    /**
     *
     * @param task
     */
    public void activateProgressBar(final Task<?> task)
    {
        pb.progressProperty().bind(task.progressProperty());
        dialogStage.show();
    }

    /**
     *
     * @return
     */
    public Stage getDialogStage()
    {
        return dialogStage;
    }
}