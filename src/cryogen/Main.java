package cryogen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application
{

    @Override
    public void start(Stage cgWindow) throws Exception
    {
        cgWindow.initStyle(StageStyle.DECORATED);
        cgWindow.getIcons().add(new Image(getClass().getResourceAsStream("/cryogen/icon.png")));
        cgWindow.setTitle("Cryptogen Beta");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Cryptogen.fxml"));
        cgWindow.setHeight(605);
        cgWindow.setWidth(720);
        cgWindow.setResizable(false);

        cgWindow.setScene(createScene(loader.load()));
        Cryptogen cg = loader.getController();
        cg.initialize(cgWindow);
        cgWindow.show();
    }

    private Scene createScene(Pane layout)
    {
        return new Scene(layout, Color.TRANSPARENT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
