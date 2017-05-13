package cryogen;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * @author Elnette Moller
 * E-Mail: elnette.moller@gmail.com
 * Java class handler for the Cryptogen application main GUI
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */
public class Cryptogen implements Initializable
{
    //Instance Variables
    private List<File> files; //List of files to be encrypted or decrypted
    private boolean exiting;
    //GUI Instance Variables
    @FXML private TitledPane pneAlgorithmsPane;
    @FXML private StackPane stackPane;
    @FXML private TitledPane pneFilePane;
    @FXML private RadioButton radVigenere;
    @FXML private RadioButton radVernam;
    @FXML private RadioButton radColumnarTrans;
    @FXML private RadioButton radElephant;
    @FXML private TextArea txtMessage;
    @FXML private TextArea txtKey;
    private final ToggleGroup algorithms;
    private Stage currentStage;

    /**
     *
     */
    public Cryptogen()
    {
        files = null;
        exiting = false;
        algorithms = new ToggleGroup();
    }

    /**
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    /**
     *
     * @param currentStage
     */
    public void initialize(Stage currentStage)
    {
        this.currentStage = currentStage;
        getCurrentStage().setOnCloseRequest(confirmCloseEventHandler);//Set default close event
        radVigenere.setToggleGroup(algorithms);
        radVigenere.setSelected(true);
        radVernam.setToggleGroup(algorithms);
        radColumnarTrans.setToggleGroup(algorithms);
        radElephant.setToggleGroup(algorithms);
        pneAlgorithmsPane.requestFocus();
    }

    public Stage getCurrentStage()
    {
        return this.currentStage;
    }


    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void btnEncryptMessage_Clicked(ActionEvent event) throws IOException
    {
        try
        {
            /*plainPassword = pswPassword.getText().toCharArray();
            if (new String(plainPassword).equals(""))
            {
                pswPassword.requestFocus();
                throw new Exception("Please Enter a Password");
            }
            key = pswKey.getText().toCharArray();
            if (new String(key).equals(""))
            {
                pswKey.requestFocus();
                throw new Exception("Please Enter a Key");
            }

            int limit;
            if(cbxCompact.isSelected())
                limit = 12;
            else
                limit = 32;

            cipherPassword = encrypt(plainPassword, key, limit);
            if(cipherPassword == null)
                throw new Exception("Error Occurred During Encryption");

            Stage passWindow = new Stage(StageStyle.TRANSPARENT);
            passWindow.getIcons().add(new Image(getClass().getResourceAsStream("/cryogen/icon.png")));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Output.fxml"));
            passWindow.setHeight(186);
            passWindow.setWidth(495);
            passWindow.setResizable(false);
            passWindow.setScene(new Scene((Pane)loader.load(), Color.TRANSPARENT));
            Output pass = loader.<Output>getController();
            pass.initialize(cipherPassword);
            passWindow.showAndWait();*/
        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void btnEncryptFiles_Clicked(ActionEvent event) throws IOException//TODO: Add dialog with progress bar
    {
        try
        {
            String message = "";
            String header = "";
            String method = "";
            if(files == null)
                throw new NoFilesAttachedException("Please drag some files onto the highlighted area\n for encryption.");//TODO: Highlight Drag and Drop area
            if(files.size() > 1)
                message = "Files are encrypted using the ";
            else if(files.size() == 1)
                message = "File is encrypted using the ";
            if(files.size() > 1)
                header = "Files Encrypted";
            else if(files.size() == 1)
                header = "File Encrypted";
            String message2 = " \nRemember your key!";

            if(txtKey.getText().equals(""))
                throw new EmptyKeyException("Please Enter a Key");//TODO: Highlight text area

            char[] key = txtKey.getText().toCharArray();
            byte[] cipherFileData = null;
            for (int ii = 0; ii < files.size(); ii++)//Encrypt Each File
            {
                //File plainFile = new File(String.valueOf(new FileInputStream(files.get(ii).getAbsolutePath())));
                Path path = Paths.get(files.get(ii).getAbsolutePath());
                byte[] plainFileData = Files.readAllBytes(path);
                if (radVigenere.isSelected())
                {
                    cipherFileData = Cryptography.VigenereCipher.encrypt(plainFileData, key);
                    method = "Vigenère cipher.";
                }
                else if (radVernam.isSelected())
                {
                    cipherFileData = Cryptography.VernamCipher.encrypt(plainFileData, key);
                    method = "Vernam cipher.";
                }
                else if (radColumnarTrans.isSelected())
                {
                    cipherFileData = Cryptography.ColumnarTranspositionCipher.encrypt(plainFileData, key);
                    method = "columnar transposition.";
                }
                else if (radElephant.isSelected())
                {
                    cipherFileData = Cryptography.ElephantCipher.encrypt(plainFileData, key);
                    method = "Elephant Encryption.";
                }

                FileOutputStream fos = new FileOutputStream(files.get(ii).getAbsoluteFile() + ".cg");
                fos.write(cipherFileData);
                fos.close();
                System.out.println(ii + ": " + files.get(ii).getAbsolutePath());
            }

            Alert encryptionInformation = new Alert(Alert.AlertType.INFORMATION, message + method + message2);
            Button okButton = (Button) encryptionInformation.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("OK");
            encryptionInformation.setHeaderText(header);
            encryptionInformation.initModality(Modality.APPLICATION_MODAL);
            encryptionInformation.initOwner(getCurrentStage());
            Optional<ButtonType> closeResponse = encryptionInformation.showAndWait();
            if (!ButtonType.OK.equals(closeResponse.get()))
            {
                event.consume();
            }
        }
        catch (NoFilesAttachedException ex)
        {
            handleException(ex, "Error", "Drag and Drop Files", ex.getMessage());
        }
        catch(EmptyKeyException ex)
        {
            handleException(ex, "Error", "Empty Key Value", ex.getMessage());
        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void btnDecryptMessage_Clicked(ActionEvent event) throws IOException
    {
        try
        {

        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void btnDecryptFiles_Clicked(ActionEvent event) throws IOException//TODO: Add dialog with progress bar
    {
        try
        {
            String message = "";
            String header = "";
            String method = "";
            if(files == null)
                throw new NoFilesAttachedException("Please drag some files onto the highlighted area\n for encryption.");//TODO: Highlight Drag and Drop area
            if(files.size() > 1)
                message = "Files are decrypted using the ";
            else if (files.size() == 1)
                message = "File is decrypted using the ";
            if (files.size() > 1)
                header = "Files Decrypted";
            else if (files.size() == 1)
                header = "File Decrypted";

            if(txtKey.getText().equals(""))
                throw new EmptyKeyException("Please Enter a Key");//TODO: Highlight text area

            char[] key = txtKey.getText().toCharArray();
            byte[] plainFileData = null;
            for (int v = 0; v < files.size(); v++)//Decrypt Each File
            {
                Path path = Paths.get(files.get(v).getAbsolutePath());
                byte[] cipherFileData = Files.readAllBytes(path);
                if (radVigenere.isSelected())
                {
                    plainFileData = Cryptography.VigenereCipher.decrypt(cipherFileData, key);
                    method = "Vigenère cipher.";
                }
                else if (radVernam.isSelected())
                {
                    plainFileData = Cryptography.VernamCipher.decrypt(cipherFileData, key);
                    method = "Vernam cipher.";
                }
                else if (radColumnarTrans.isSelected())
                {
                    plainFileData = Cryptography.ColumnarTranspositionCipher.decrypt(cipherFileData, key);
                    method = "columnar transposition.";
                }
                else if (radElephant.isSelected())
                {
                    plainFileData = Cryptography.ElephantCipher.decrypt(cipherFileData, key);
                    method = "Elephant Encryption.";
                }
                else
                    throw new InputMismatchException("Please Choose an Algorithm for Encryption/Decryption");

                FileOutputStream fos = new FileOutputStream(files.get(v).getAbsolutePath().substring(0, files.get(v).getAbsolutePath().length() - 3));
                fos.write(plainFileData);
                fos.close();
            }

            Alert decryptionInformation = new Alert(Alert.AlertType.INFORMATION, message + method);
            decryptionInformation.initModality(Modality.APPLICATION_MODAL);
            decryptionInformation.initOwner(getCurrentStage());
            Button okButton = (Button) decryptionInformation.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("OK");
            decryptionInformation.setHeaderText(header);
            decryptionInformation.initModality(Modality.APPLICATION_MODAL);
            decryptionInformation.initOwner(getCurrentStage());
            Optional<ButtonType> closeResponse = decryptionInformation.showAndWait();
            if (!ButtonType.OK.equals(closeResponse.get()))
            {
                event.consume();
            }
        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    /**
     * Event necessary to execute before DragDropped may be executed, allows DragDropped to receive files by Copy or Move
     * @param event
     */
    @FXML
    protected void onDragOver(DragEvent event)
    {
        //data is dragged over the target
        //accept it only if it is not dragged from the same node
        //and if it has a string data
        if(event.getGestureSource() != stackPane && event.getDragboard().hasString())
        {
            //allow for both copying and moving, whatever user chooses
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    /**
     * Do something when an Object is dragged over the StackPane
     * @param event
     * @throws IOException
     */
    @FXML
    protected void onDragEntered(DragEvent event)
    {
        pneFilePane.getStyleClass().remove("pneDefault");
        pneFilePane.getStyleClass().add("pneFilePaneDrag");
    }

    /**
     * Do something when an object exits the StackPane
     * @param event
     */
    @FXML
    protected void onDragExited(DragEvent event)
    {
        pneFilePane.getStyleClass().remove("pneFilePaneDrag");
        pneFilePane.getStyleClass().add("pneDefault");
    }

    /**
     * Do something when an object is dropped onto the StackPane
     * @param event
     * @throws IOException
     */
    @FXML
    protected void onDragDropped(final DragEvent event) throws IOException
    {
        final Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles())
        {
            success = true;
            // Only get the first file from the list
            files = db.getFiles();
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        for(int i = 0; i < files.size(); i++)
                            System.out.println(files.get(i).getAbsolutePath());

                        if(!stackPane.getChildren().isEmpty())
                        {
                            stackPane.getChildren().remove(0);
                        }
                        pneFilePane.getStyleClass().remove("pneFilePaneDrag");
                        pneFilePane.getStyleClass().remove("pneDefault");
                        pneFilePane.getStyleClass().add("pneFilePaneDropped");
                        System.out.println("Drop Successful!");
                    }
                    catch (Exception ex)
                    {
                        //Logger.getLogger(Cryptogen.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println(ex.toString());
                    }
                }
            });
        }
        event.setDropCompleted(success);
        event.consume();
    }

    /**
     * Event handler for File -> Clear Files
     * Used to clear loaded files
     * @param event
     */
    @FXML protected void mnuFile_ClearFiles_Clicked(ActionEvent event)
    {
        files = null;
        pneFilePane.getStyleClass().remove("pneFilePaneDrag");
        pneFilePane.getStyleClass().remove("pneFilePaneDropped");
        pneFilePane.getStyleClass().add("pneFilePane");
    }

    /**
     * Event handler method for File -> Exit
     * @param event
     */
    @FXML protected void mnuFile_Exit_Clicked(ActionEvent event)
    {
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Confirm Exit");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(getCurrentStage());
        exiting = true;
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.OK.equals(closeResponse.get()))
            System.exit(0);
        else
            exiting = false;
    }

    /**
     * Method to prompt before exit
     * Exits application with 0 error code if user prompt is confirmed else application continues
     */
    private EventHandler<WindowEvent> confirmCloseEventHandler = event ->
    {
        Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Confirm Exit");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(getCurrentStage());
        exiting = true;
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get()))
        {
            exiting = false;
            event.consume();
        }
    };

    protected void handleException(Exception ex)
    {
        handleException(ex, "Error");
    }

    protected void handleException(Exception ex, String title)
    {
        handleException(ex, title, ex.getMessage());
    }

    protected void handleException(Exception ex, String title, String header)
    {
        handleException(ex, title, header, ex.toString());
    }

    protected void handleException(Exception ex, String title, String header, String content)//TODO:Theme the Dialog
    {
        ex.printStackTrace();
        Alert error = new Alert(Alert.AlertType.ERROR, content);
        error.initModality(Modality.APPLICATION_MODAL);
        error.initOwner(getCurrentStage());
        error.setTitle(title);
        error.setHeaderText(header);
        error.showAndWait();
    }
}
