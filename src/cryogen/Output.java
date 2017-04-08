package cryogen;

/**
 * @author Zander Labuschagne
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by zander on 2016/10/13.
 */
public class Output implements Initializable
{
    //GUI Instance Variables
    @FXML private TextField txtPassword;
    @FXML private Button btnCopyPassword;
    @FXML private Button btnRevealPassword;

    //Instance Variables
    private char[] password;
    private int handler;
    private String s;
    ///private Font show = Font.createFont(Font.TRUETYPE_FONT, new File(String.valueOf(this.getClass().getResource("/font/DejaVuSansMono.ttf")))).deriveFont(12f);
    private Font hide = new Font("Noto Sans", 12);
    private Font show = new Font("DejaVu Sans Mono", 12);

    //Default Constructor
    public Output()
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    public void initialize(char[] password)
    {
        txtPassword.setFont(hide);
        s = "";
        for(int i = 0; i < password.length; i++)
            s += '\u25cf';
        txtPassword.setText(s);
        this.password = password;
        handler = 0;
        txtPassword.setEditable(false);
        btnCopyPassword.requestFocus();
    }

    @FXML
    protected void btnOK_Clicked(ActionEvent event)
    {
        ((Node) (event.getSource())).getScene().getWindow().hide();//Hide This Window
        //dispose();
    }

    @FXML
    protected void btnCopyPassword_Clicked(ActionEvent event)
    {
        StringSelection stringSelection = new StringSelection(new String(password));
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        ((Node) (event.getSource())).getScene().getWindow().hide();//Hide This Window
    }

    @FXML
    protected void btnRevealPassword_Clicked(ActionEvent event)
    {
        if(handler == 0)
        {
            txtPassword.setFont(show);
            txtPassword.setText(new String(password));
            btnRevealPassword.setText("Hide Password");
            handler = 1;
        }
        else
        {
            txtPassword.setFont(hide);
            txtPassword.setText(s);
            btnRevealPassword.setText("Reveal Password");
            handler = 0;
        }
    }
}
