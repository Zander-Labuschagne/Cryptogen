package cryogen;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
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
    public static String laf;
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
    @FXML private VBox vbox;
    @FXML private Button btnEncryptMessage;
    @FXML private Button btnEncryptFiles;
    @FXML private Button btnDecryptMessage;
    @FXML private Button btnDecryptFiles;
    @FXML private TitledPane pneMessagePane;
    private final ToggleGroup algorithms;
    private Stage currentStage;

    /**
     * Default Constructor
     */
    public Cryptogen()
    {
        files = null;
        exiting = false;
        algorithms = new ToggleGroup();
        message = "";
        header = "";
        method = "";
        laf = "BreathDark.css";
    }

    /**
     * Initialize method for GUI
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    /**
     * Initialize methof ofr GUI
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
        mnuLaF_BreathDark_Clicked(new ActionEvent());

    }

    public Stage getCurrentStage()
    {
        return this.currentStage;
    }


    /**
     * Event method where Encrypt Message button is clicked
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
                throw new EmptyMessageException("Please Enter a Message to Encrypt");
            }
            char[] key = txtKey.getText().toCharArray();
            if (new String(key).equals(""))
            {
                txtKey.requestFocus();
                throw new EmptyKeyException("Please Enter a Key");
            }

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
            else if(radColumnarTrans.isSelected())
            {
                cipherMessage = Cryptography.ColumnarTranspositionCipher.encrypt(newMessage, key);
                method = "columnar transposition cipher.";
            }
            else if(radElephant.isSelected())
            {
                cipherMessage = Cryptography.ElephantCipher.encrypt(newMessage, key);
                method = "Elephant cipher.";
            }
            txtMessage.setText(String.valueOf(cipherMessage));
        }
        catch (VigenereKeyOutOfRangeException ex)
        {
            ex.printStackTrace();
            handleException(ex, "Key Out of Bounds", "Key contains illegal characters", ex.getMessage());
        }
        catch(EmptyKeyException ex)
        {
            pneKey.getStyleClass().remove("pneDefault");
            pneKey.getStyleClass().add("pneDefaultError");
            txtKey.requestFocus();
            handleException(ex, "Error", "Empty Key Value", ex.getMessage());
        }
        catch (EmptyMessageException ex)
        {
            pneMessagePane.getStyleClass().remove("pneDefault");
            pneMessagePane.getStyleClass().add("pneDefaultError");
            txtMessage.requestFocus();
            handleException(ex, "Error", "Empty Message", ex.getMessage());
        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    /**
     * Handle encryption of multiple files and directories
     * @param files List of files to be encrypted
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
     * Handle decryption of multiple directories
     * @param files List of Files to be decrypted
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
     * Event method handles click event on Encrypt Files button
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
            dialogPane.getStylesheets().add(getClass().getResource(laf).toExternalForm());
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
            txtKey.requestFocus();
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
     * Method handles event where Decrypt Message button is clicked
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
                throw new EmptyMessageException("Please Enter a Message to Decrypt");
            }
            char[] key = txtKey.getText().toCharArray();
            if (new String(key).equals(""))
            {
                txtKey.requestFocus();
                throw new EmptyKeyException("Please Enter a Key");
            }
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
            else if(radColumnarTrans.isSelected())
            {
                plainMesage = Cryptography.ColumnarTranspositionCipher.decrypt(newMessage, key);
                method = "columnar transposition cipher.";
            }
            else if(radElephant.isSelected())
            {
                plainMesage = Cryptography.ElephantCipher.decrypt(newMessage, key);
                method = "Elephant cipher.";
            }
            txtMessage.setText(String.valueOf(plainMesage));
        }
        catch (VigenereKeyOutOfRangeException ex)
        {
            ex.printStackTrace();
            handleException(ex, "Key Out of Bounds", "Key contains illegal characters", ex.getMessage());
        }
        catch(EmptyKeyException ex)
        {
            pneKey.getStyleClass().remove("pneDefault");
            pneKey.getStyleClass().add("pneDefaultError");
            txtKey.requestFocus();
            handleException(ex, "Error", "Empty Key Value", ex.getMessage());
        }
        catch (EmptyMessageException ex)
        {
            pneMessagePane.getStyleClass().remove("pneDefault");
            pneMessagePane.getStyleClass().add("pneDefaultError");
            txtMessage.requestFocus();
            handleException(ex, "Error", "Empty Message", ex.getMessage());
        }
        catch (Exception ex)
        {
            handleException(ex);
        }
    }

    /**
     * Method handles event where Decrypt Files button is clicked
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
            dialogPane.getStylesheets().add(getClass().getResource(laf).toExternalForm());
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
            pneKey.getStyleClass().remove("pneDefault");
            pneKey.getStyleClass().add("pneDefaultError");
            txtKey.requestFocus();
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
    * Method handles event where key is typed in Key text area
    */
    @FXML
    protected void txtKey_OnKeyType()
    {
        pneKey.getStyleClass().remove("pneDefaultError");
        pneKey.getStyleClass().add("pneDefault");
    }
    /**
     * Method handles event where key is typed in Message text area
     */
    @FXML
    protected void txtMessage_OnKeyType()
    {
        pneMessagePane.getStyleClass().remove("pneDefaultError");
        pneMessagePane.getStyleClass().add("pneDefault");
    }

    /**
     * Event necessary to execute before DragDropped may be executed
     * Allows DragDropped to receive files by Copy or Move
     * @param event
     */
    @FXML
    protected void onDragOver(DragEvent event)
    {
        //data is dragged over the target
        if(event.getGestureSource() != stackPane && event.getDragboard().hasString())
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        event.consume();
    }

    /**
     * Method to execute when file is dragged over area
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
     * Method to execute when file is no longer above area
     * @param event
     */
    @FXML
    protected void onDragExited(DragEvent event)
    {
        pneFilePane.getStyleClass().remove("pneFilePaneDrag");
        pneFilePane.getStyleClass().add("pneDefault");
    }

    /**
     * Method to execute when file(s) is/are dropped in area
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
     * Event handler to attach files
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
        dialogPane.getStylesheets().add(getClass().getResource(laf).toExternalForm());
        dialogPane.getStyleClass().add("dlgDefault");
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.OK.equals(closeResponse.get()))
            System.exit(0);
        else
            exiting = false;
    }

    /**
     * Event handler for clear message clicked
     * @param event
     */
    @FXML protected void mnuEdit_ClearMessage_Clicked(ActionEvent event)
    {
        txtMessage.clear();
    }

    /**
     * Event handler for clear key clicked
     * @param event
     */
    @FXML protected void mnuEdit_ClearKey_Clicked(ActionEvent event)
    {
        txtKey.clear();
    }

    /**
     * Event handler to copy message text
     * @param event
     */
    @FXML protected void mnuEdit_CopyMessage_Clicked(ActionEvent event)
    {
        StringSelection stringSelection = new StringSelection(new String(txtMessage.getText()));
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    /**
     * Event handler to cut message text
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
     * Event handler to paste inside message text area
     * @param event
     */
    @FXML protected void mnuEdit_PasteMessage_Clicked(ActionEvent event)
    {
        paste(txtMessage, DataFlavor.stringFlavor);
    }

    /**
     *Event handler to paste inside key text area
     * @param event
     */
    @FXML protected void mnuEdit_PasteKey_Clicked(ActionEvent event)
    {
        paste(txtKey, DataFlavor.stringFlavor);
    }

    /**
     * Method used to paste data
     * @param tf text area to paste into
     * @param df kind of data to paste, text or file
     */
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
                dialogPane.getStylesheets().add(getClass().getResource(laf).toExternalForm());
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
     * Event Handler where Help -> About is clicked
     * Opens About Window
     * @param event
     */
    @FXML
    private void mnuHelp_About_Clicked(ActionEvent event)
    {
        Parent about;
        try
        {
            Stage aboutWindow = new Stage(StageStyle.UNDECORATED);
            aboutWindow.initOwner(getCurrentStage());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("About.fxml"));
            aboutWindow.setResizable(false);
            aboutWindow.setScene(new Scene((Pane)loader.load()));
            //About about = loader.<About>getController();
            aboutWindow.showAndWait();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            handleException(ex);
        }
    }

    /**
     * Event handler to view user manual
     * @param event
     */
    @FXML
    protected void mnuHelp_UserManual_Clicked(ActionEvent event)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    File userGuide;
                    if(Desktop.isDesktopSupported())
                    {
                        InputStream resource = this.getClass().getResource("/Cryptogen Manual.pdf").openStream();
                        File file = File.createTempFile("User Manual", "pdf");
                        OutputStream out = new FileOutputStream(file);

                        byte[] buffer = new byte[1024];
                        int len;
                        while((len = resource.read(buffer)) != -1)
                            out.write(buffer, 0, len);
                        userGuide = file;
                        out.close();

                        Desktop.getDesktop().open(userGuide);
                        resource.close();
                    }
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                    handleException(ex, "User Manual Error", "Could not open user manual", "Something went wrong while trying to open the user manual.\nDo you hav a PDF reader installed?");
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    handleException(ex);
                }
            }
        }).start();
    }

    /**
     * Event handler for look and feel changed to Midna Dark
     * @param event
     */
    @FXML
    protected void mnuLaF_MidnaDark_Clicked(ActionEvent event)
    {
        laf = "MidnaDark.css";
        vbox.getStylesheets().clear();
        vbox.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        pneKey.getStylesheets().clear();
        pneKey.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        pneFilePane.getStylesheets().clear();
        pneFilePane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        pneAlgorithmsPane.getStylesheets().clear();
        pneAlgorithmsPane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        pneMessagePane.getStylesheets().clear();
        pneMessagePane.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        txtKey.getStylesheets().clear();
        txtKey.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        txtMessage.getStylesheets().clear();
        txtMessage.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        btnEncryptMessage.getStylesheets().clear();
        btnEncryptMessage.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        btnDecryptMessage.getStylesheets().clear();
        btnDecryptMessage.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        btnEncryptFiles.getStylesheets().clear();
        btnEncryptFiles.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        btnDecryptFiles.getStylesheets().clear();
        btnDecryptFiles.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        radElephant.getStylesheets().clear();
        radElephant.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        radColumnarTrans.getStylesheets().clear();
        radColumnarTrans.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        radVernam.getStylesheets().clear();
        radVernam.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
        radVigenere.getStylesheets().clear();
        radVigenere.getStylesheets().add(getClass().getResource("MidnaDark.css").toExternalForm());
    }

    /**
     * Event handler for look and feel changed to Midna
     * @param event
     */
    @FXML
    protected void mnuLaF_Midna_Clicked(ActionEvent event)
    {
        laf = "Midna.css";
        vbox.getStylesheets().clear();
        vbox.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        pneKey.getStylesheets().clear();
        pneKey.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        pneFilePane.getStylesheets().clear();
        pneFilePane.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        pneAlgorithmsPane.getStylesheets().clear();
        pneAlgorithmsPane.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        pneMessagePane.getStylesheets().clear();
        pneMessagePane.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        txtKey.getStylesheets().clear();
        txtKey.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        txtMessage.getStylesheets().clear();
        txtMessage.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        btnEncryptMessage.getStylesheets().clear();
        btnEncryptMessage.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        btnDecryptMessage.getStylesheets().clear();
        btnDecryptMessage.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        btnEncryptFiles.getStylesheets().clear();
        btnEncryptFiles.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        btnDecryptFiles.getStylesheets().clear();
        btnDecryptFiles.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        radElephant.getStylesheets().clear();
        radElephant.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        radColumnarTrans.getStylesheets().clear();
        radColumnarTrans.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        radVernam.getStylesheets().clear();
        radVernam.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
        radVigenere.getStylesheets().clear();
        radVigenere.getStylesheets().add(getClass().getResource("Midna.css").toExternalForm());
    }

    /**
     * Event handler for look and feel changed to Midna Dark
     * @param event
     */
    @FXML
    protected void mnuLaF_BreathDark_Clicked(ActionEvent event)
    {
        laf = "BreathDark.css";
        vbox.getStylesheets().clear();
        vbox.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        pneKey.getStylesheets().clear();
        pneKey.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        pneFilePane.getStylesheets().clear();
        pneFilePane.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        pneAlgorithmsPane.getStylesheets().clear();
        pneAlgorithmsPane.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        pneMessagePane.getStylesheets().clear();
        pneMessagePane.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        txtKey.getStylesheets().clear();
        txtKey.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        txtMessage.getStylesheets().clear();
        txtMessage.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        btnEncryptMessage.getStylesheets().clear();
        btnEncryptMessage.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        btnDecryptMessage.getStylesheets().clear();
        btnDecryptMessage.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        btnEncryptFiles.getStylesheets().clear();
        btnEncryptFiles.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        btnDecryptFiles.getStylesheets().clear();
        btnDecryptFiles.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        radElephant.getStylesheets().clear();
        radElephant.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        radColumnarTrans.getStylesheets().clear();
        radColumnarTrans.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        radVernam.getStylesheets().clear();
        radVernam.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
        radVigenere.getStylesheets().clear();
        radVigenere.getStylesheets().add(getClass().getResource("BreathDark.css").toExternalForm());
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
        dialogPane.getStylesheets().add(getClass().getResource(laf).toExternalForm());
        dialogPane.getStyleClass().add("dlgDefault");
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get()))
        {
            exiting = false;
            event.consume();
        }
    };

    /**
     * method to handle exceptions
     * @param ex Exception thrown to handle
     */
    protected void handleException(Exception ex)
    {
        handleException(ex, "Error");
    }

    /**
     * method to handle exceptions with optional window title
     * @param ex Exception thrown to handle
     * @param title to be displayed in message box
     */
    protected void handleException(Exception ex, String title)
    {
        handleException(ex, title, ex.getMessage());
    }

    /**
     * method to handle exceptions with optional window title and header
     * @param ex Exception thrown to handle
     * @param title to be displayed in message box
     * @param header caption to be displayed in message box
     */
    protected void handleException(Exception ex, String title, String header)
    {
        handleException(ex, title, header, ex.toString());
    }

    /**
     * method to handle exceptions with optional window title, header and message text
     * @param ex Exception thrown to handle
     * @param title to be displayed in message box
     * @param header caption to be displayed in message box
     * @param content message for message box to contain
     */
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
        dialogPane.getStylesheets().add(getClass().getResource(laf).toExternalForm());
        dialogPane.getStyleClass().add("dlgDefault");
        error.showAndWait();
    }
}
