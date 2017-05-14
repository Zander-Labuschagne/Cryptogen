package cryogen;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.List;

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
    private String message;
    private String header;
    private String method;
    //GUI Instance Variables
    @FXML private TitledPane pneAlgorithmsPane;
    @FXML private StackPane stackPane;
    @FXML private TitledPane pneFilePane;
    @FXML private TitledPane pneKey;
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
        message = "";
        header = "";
        method = "";
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
            char[] cipherMessage = null;

            char[] newMessage = txtMessage.getText().toCharArray();
            if (new String(newMessage).equals(""))
            {
                txtMessage.requestFocus();
                throw new Exception("Please Enter a Password");
            }
            char[] key = txtKey.getText().toCharArray();
            if (new String(key).equals(""))
            {
                txtKey.requestFocus();
                throw new Exception("Please Enter a Key");
            }

            /*int limit;
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

            if (radVigenere.isSelected())
            {
                cipherMessage = Cryptography.VigenereCipher.encrypt(newMessage, key);
                method = "Vigenère cipher.";
            }
            else if(radVernam.isSelected())
            {
                cipherMessage = Cryptography.VernamCipher.encrypt(newMessage, key);
                method = "Vernam cipher.";
            }
            else if(radElephant.isSelected())
            {
                cipherMessage = Cryptography.ElephantCipher.encrypt(newMessage, key);
                method = "Elephant cipher.";
            }
        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    /**
     *
     * @param files
     */
    private void encryptFiles(List<File> files)
    {
        try
        {
            char[] key = txtKey.getText().toCharArray();
            for (int ii = 0; ii < files.size(); ii++)//Encrypt Each File
            {
                if (files.get(ii).isDirectory())
                    encryptFiles(Arrays.asList(files.get(ii).listFiles()));
                else if (files.get(ii).isFile())
                {
                    if (radVigenere.isSelected())
                    {
                        Cryptography.VigenereCipher.encrypt(files.get(ii), key);
                        method = "Vigenère cipher.";
                    }
                    else if (radVernam.isSelected())
                    {
                        Cryptography.VernamCipher.encrypt(files.get(ii), key);
                        method = "Vernam cipher.";
                    }
                    else if (radColumnarTrans.isSelected())
                    {
                        Cryptography.ColumnarTranspositionCipher.encrypt(files.get(ii), key);
                        method = "columnar transposition.";
                    }
                    else if (radElephant.isSelected())
                    {
                        Cryptography.ElephantCipher.encrypt(files.get(ii), key);
                        method = "Elephant Encryption.";
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            handleException(ex);
        }
    }

    /**
     *
     * @param files
     */
    private void decryptFiles(List<File> files)
    {
        try
        {
            char[] key = txtKey.getText().toCharArray();
            for (int v = 0; v < files.size(); v++)//Decrypt Each File
            {
                if (files.get(v).isDirectory())
                    decryptFiles(Arrays.asList(files.get(v).listFiles()));
                else if (files.get(v).isFile() && files.get(v).getAbsoluteFile().getPath().substring(files.get(v).getAbsoluteFile().getPath().length() - 3, files.get(v).getAbsoluteFile().getPath().length()).equals(".cg"))
                {
                    if (radVigenere.isSelected())
                    {
                        Cryptography.VigenereCipher.decrypt(files.get(v), key);
                        method = "Vigenère cipher.";
                    }
                    else if (radVernam.isSelected())
                    {
                        Cryptography.VernamCipher.decrypt(files.get(v), key);
                        method = "Vernam cipher.";
                    }
                    else if (radColumnarTrans.isSelected())
                    {
                        Cryptography.ColumnarTranspositionCipher.decrypt(files.get(v), key);
                        method = "columnar transposition.";
                    }
                    else if (radElephant.isSelected())
                    {
                        Cryptography.ElephantCipher.decrypt(files.get(v), key);
                        method = "Elephant Encryption.";
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            handleException(ex);
        }
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    protected void btnEncryptFiles_Clicked(ActionEvent event)
    {
        try
        {
            if(files == null)
                throw new NoFilesAttachedException("Please drag some files onto the highlighted area\n or copy some files followed by Edit -> Paste Files.");
            if(files.size() > 1)
                message = "Files are being encrypted using the ";
            else if(files.size() == 1)
                message = "File is being encrypted using the ";
            if(files.size() > 1)
                header = "Files Encrypting";
            else if(files.size() == 1)
                header = "File Encrypting";
            String message2 = " \nRemember your key!";

            if(txtKey.getText().equals(""))
                throw new EmptyKeyException("Please Enter a Key");

            encryptFiles(files);

            Alert encryptionInformation = new Alert(Alert.AlertType.INFORMATION, message + method + message2);
            encryptionInformation.setGraphic(new ImageView(this.getClass().getResource("/icons/success32.png").toString()));
            Button okButton = (Button) encryptionInformation.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("OK");
            encryptionInformation.setHeaderText(header);
            encryptionInformation.initModality(Modality.APPLICATION_MODAL);
            encryptionInformation.initOwner(getCurrentStage());
            DialogPane dialogPane = encryptionInformation.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
            dialogPane.getStyleClass().add("dlgDefault");
            Optional<ButtonType> closeResponse = encryptionInformation.showAndWait();
            if (!ButtonType.OK.equals(closeResponse.get()))
            {
                event.consume();
            }
        }
        catch (NoFilesAttachedException ex)
        {
            pneFilePane.getStyleClass().remove("pneDefault");
            pneFilePane.getStyleClass().add("pneFilePaneError");
            handleException(ex, "Error", "Drag and Drop Files", ex.getMessage());
        }
        catch(EmptyKeyException ex)
        {
            pneKey.getStyleClass().remove("pneDefault");
            pneKey.getStyleClass().add("pneDefaultError");
            handleException(ex, "Error", "Empty Key Value", ex.getMessage());
        }
        catch(OutOfMemoryError e)
        {
            e.printStackTrace();
            Cryptography.handleException(null, "Out of Memory", "Out of Memory", "File too large, or restart the system.");
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
            char[] plainMesage = null;

            char[] newMessage = txtMessage.getText().toCharArray();
            if (new String(newMessage).equals(""))
            {
                txtMessage.requestFocus();
                throw new Exception("Please Enter a Password");
            }
            char[] key = txtKey.getText().toCharArray();
            if (new String(key).equals(""))
            {
                txtKey.requestFocus();
                throw new Exception("Please Enter a Key");
            }

            /*int limit;
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

            if (radVigenere.isSelected())
            {
                plainMesage = Cryptography.VigenereCipher.decrypt(newMessage, key);
                method = "Vigenère cipher.";
            }
            else if(radVernam.isSelected())
            {
                plainMesage = Cryptography.VernamCipher.decrypt(newMessage, key);
                method = "Vernam cipher.";
            }
            else if(radElephant.isSelected())
            {
                plainMesage = Cryptography.ElephantCipher.decrypt(newMessage, key);
                method = "Elephant cipher.";
            }
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
    protected void btnDecryptFiles_Clicked(ActionEvent event) throws IOException
    {
        try
        {
            if(files == null)
                throw new NoFilesAttachedException("Please drag some files onto the highlighted area\n or copy some files followed by Edit -> Paste Files.");
            if(files.size() > 1)
                message = "Files are being decrypted using the ";
            else if (files.size() == 1)
                message = "File is being decrypted using the ";
            if (files.size() > 1)
                header = "Files Decrypting";
            else if (files.size() == 1)
                header = "File Decrypting";

            if(txtKey.getText().equals(""))
                throw new EmptyKeyException("Please Enter a Key");

            decryptFiles(files);

            Alert decryptionInformation = new Alert(Alert.AlertType.INFORMATION, message + method);
            decryptionInformation.setGraphic(new ImageView(this.getClass().getResource("/icons/success32.png").toString()));
            decryptionInformation.initModality(Modality.APPLICATION_MODAL);
            decryptionInformation.initOwner(getCurrentStage());
            Button okButton = (Button) decryptionInformation.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("OK");
            decryptionInformation.setHeaderText(header);
            decryptionInformation.initModality(Modality.APPLICATION_MODAL);
            decryptionInformation.initOwner(getCurrentStage());
            DialogPane dialogPane = decryptionInformation.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
            dialogPane.getStyleClass().add("dlgDefault");
            Optional<ButtonType> closeResponse = decryptionInformation.showAndWait();
            if (!ButtonType.OK.equals(closeResponse.get()))
            {
                event.consume();
            }
        }
        catch (NoFilesAttachedException ex)
        {
            pneFilePane.getStyleClass().remove("pneDefault");
            pneFilePane.getStyleClass().add("pneFilePaneError");
            handleException(ex, "Error", "Drag and Drop Files", ex.getMessage());
        }
        catch(EmptyKeyException ex)
        {
            txtKey.getStyleClass().remove("txtDefault");
            txtKey.getStyleClass().add("txtDefaultError");
            handleException(ex, "Error", "Empty Key Value", ex.getMessage());
        }
        catch(OutOfMemoryError e)
        {
            e.printStackTrace();
            Cryptography.handleException(null, "Out of Memory", "Out of Memory", "File too large, or restart the system.");
        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    @FXML
    protected void txtKey_OnKeyType()
    {
        pneKey.getStyleClass().remove("pneDefaultError");
        pneKey.getStyleClass().add("pneDefault");
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
        pneFilePane.getStyleClass().remove("pneFilePaneError");
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
                        /*for(int i = 0; i < files.size(); i++)
                            System.out.println(files.get(i).getAbsolutePath());*/

                        if(!stackPane.getChildren().isEmpty())
                        {
                            stackPane.getChildren().remove(0);
                        }
                        pneFilePane.getStyleClass().remove("pneFilePaneDrag");
                        pneFilePane.getStyleClass().remove("pneDefault");
                        pneFilePane.getStyleClass().add("pneFilePaneDropped");
                    }
                    catch (Exception ex)
                    {
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
     *
     * @param event
     */
    @FXML protected void mnuFile_PasteFiles_Clicked(ActionEvent event)
    {
        paste(null, DataFlavor.javaFileListFlavor);
        pneFilePane.getStyleClass().remove("pneFilePaneError");
        pneFilePane.getStyleClass().add("pneFilePaneDropped");
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
        DialogPane dialogPane = closeConfirmation.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        dialogPane.getStyleClass().add("dlgDefault");
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.OK.equals(closeResponse.get()))
            System.exit(0);
        else
            exiting = false;
    }

    /**
     *
     * @param event
     */
    @FXML protected void mnuEdit_ClearMessage_Clicked(ActionEvent event)
    {
        txtMessage.clear();
    }

    /**
     *
     * @param event
     */
    @FXML protected void mnuEdit_ClearKey_Clicked(ActionEvent event)
    {
        txtKey.clear();
    }

    /**
     *
     * @param event
     */
    @FXML protected void mnuEdit_CopyMessage_Clicked(ActionEvent event)
    {
        StringSelection stringSelection = new StringSelection(new String(txtMessage.getText()));
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    /**
     *
     * @param event
     */
    @FXML protected void mnuEdit_CutMessage_Clicked(ActionEvent event)
    {
        StringSelection stringSelection = new StringSelection(new String(txtMessage.getText()));
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        txtMessage.clear();
    }

    /**
     *
     * @param event
     */
    @FXML protected void mnuEdit_PasteMessage_Clicked(ActionEvent event)
    {
        paste(txtMessage, DataFlavor.stringFlavor);
    }

    /**
     *
     * @param event
     */
    @FXML protected void mnuEdit_PasteKey_Clicked(ActionEvent event)
    {
        paste(txtKey, DataFlavor.stringFlavor);
    }

    private void paste(TextArea tf, DataFlavor df)
    {
        try
        {
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clpbrd.getContents(this);
            if (contents == null)
            {
                Alert warning = new Alert(Alert.AlertType.WARNING, "Please copy something to paste.");
                DialogPane dialogPane = warning.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
                dialogPane.getStyleClass().add("dlgDefault");
                warning.initModality(Modality.APPLICATION_MODAL);
                warning.initOwner(getCurrentStage());
                warning.setTitle("Warning");
                warning.setHeaderText("Nothing to paste");
                warning.showAndWait();
            }
            else
                if(tf != null)
                    tf.setText((String) contents.getTransferData(df));
                else
                    files = (List)contents.getTransferData(df);

        }
        catch(Exception ex)
        {
            handleException(ex);
        }
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
        DialogPane dialogPane = closeConfirmation.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        dialogPane.getStyleClass().add("dlgDefault");
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

    protected void handleException(Exception ex, String title, String header, String content)
    {
        if(ex != null)
            ex.printStackTrace();
        Alert error = new Alert(Alert.AlertType.ERROR, content);
        error.initModality(Modality.APPLICATION_MODAL);
        error.initOwner(getCurrentStage());
        error.setTitle(title);
        error.setHeaderText(header);
        DialogPane dialogPane = error.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        dialogPane.getStyleClass().add("dlgDefault");
        error.showAndWait();
    }
}
