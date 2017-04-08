package cryogen;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 */

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SecureSignIn implements Initializable
{
    //Instance Variables
    private char[] plainPassword;
    private char[] key;
    private char[] cipherPassword;
    private Stage currentStage;

    //GUI Instance Variables
    @FXML private PasswordField pswPassword;
    @FXML private PasswordField pswKey;
    @FXML private CheckBox cbxCompact;

    //Default Constructor
    public SecureSignIn()
    {
        plainPassword = null;
        key = null;
        cipherPassword = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }

    public void initialize(Stage currentStage)
    {
        this.currentStage = currentStage;
        pswPassword.requestFocus();
    }

    public Stage getCurrentStage()
    {
        return this.currentStage;
    }


    @FXML
    protected void btnEncrypt_Clicked(ActionEvent event) throws IOException
    {
        try
        {
            plainPassword = pswPassword.getText().toCharArray();
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
            passWindow.showAndWait();
        }
        catch (Exception ex)
        {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText(ex.getMessage());
            error.showAndWait();
        }
    }

    /**
     * Method to encrypt the password
     * Based on Viginere's Cipher Algorithm, modified by Zander
     *
     * @param userPassword the password to be encrypted
     * @param key          the key used to encrypt the password
     * @return the encrypted password
     */
    public static char[] encrypt(char[] userPassword, char[] key, int limit)
    {
        try
        {
            char[] systemPassword = new char[userPassword.length + 1];
            char[] finalPassword = new char[userPassword.length * 2 + 1];
            int keyIndex = 0;
            int i = 0;
            int ii = 0;
            int temp;
            int specCharCount = 0;
            int pos = 0;
            char[] specChars = new char[userPassword.length + 1];

            for (char t : userPassword)
            {
                if (t >= 65 && t <= 90)//Encrypting Uppercase Characters
                {
                    temp = t - 65 + (key[keyIndex] - 65);
                    if (temp < 0)
                        temp += 26;
                    if (temp <= 0)
                        temp += 26;

                    systemPassword[i++] = (char) (65 + (temp % 26));
                    if (++keyIndex == key.length)
                        keyIndex = 0;
                }
                else if (t >= 97 && t <= 122)//Encrypting Lower Case Characters
                {
                    temp = t - 97 + (key[keyIndex] - 97);
                    if (temp < 0)
                        temp += 26;
                    if (temp < 0)
                        temp += 26;

                    systemPassword[i++] = (char) (97 + (temp % 26));
                    if (++keyIndex == key.length)
                        keyIndex = 0;
                }
                else//Encrypting Special Characters
                {
                    specChars[ii++] = (char) (pos + 65);
                    specChars[ii++] = t;
                    specCharCount++;
                }
                pos++;
            }
            i = 0;
            finalPassword[i++] = (char) (specCharCount == 0 ? 65 : (--specCharCount + 65));//Encrypting Amount of Special Characters in Password
            for (char t = specChars[0]; t != 0; i++, t = specChars[i - 1])//Encrypting Special Characters & Positions of Special Characters
                finalPassword[i] = t;
            ii = i;
            for (char t = systemPassword[0]; t != 0; i++, t = systemPassword[i - ii])//Encrypting Password
                finalPassword[i] = t;

            int ext = -1;
            if(i > 32)
            {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Warning");
                confirm.setHeaderText( "Password is greater than 32 characters");
                confirm.setContentText("Would you like to shorten the password to the 32 limit?");
                confirm.getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);
                Optional<ButtonType> result = confirm.showAndWait();
                if(result.get() == ButtonType.YES)
                    ext = 1;
            }

            int length = 0;
            for(int x = 0; finalPassword[x] != '\0'; x++)
                length++;
            char[] cipherPassword = new char[length];
            for(int xi = 0; xi < cipherPassword.length && xi < length; xi++)
                cipherPassword[xi] = finalPassword[xi];

            //Shuffle Password
            LinkedList<Character> evens = new LinkedList<>();
            LinkedList<Character>odds = new LinkedList<>();
            for(int iii = 0; iii < cipherPassword.length; iii++)
                if((int)cipherPassword[iii] % 2 == 0)
                    evens.addLast(cipherPassword[iii]);
                else
                    odds.addFirst(cipherPassword[iii]);
            int iv = 0;
            while(!evens.isEmpty() || !odds.isEmpty())
            {
                if (!odds.isEmpty())
                {
                    cipherPassword[iv++] = odds.getFirst();
                    odds.removeFirst();
                }
                if(!evens.isEmpty())
                {
                    cipherPassword[iv++] = evens.getFirst();
                    evens.removeFirst();
                }
            }

            //encrypt special chars further
            for(int v = 0; v < cipherPassword.length; v++)
                if((int)cipherPassword[v] <= 47)
                    cipherPassword[v] += 10;
                else if((int)cipherPassword[v] > 47 && (int)cipherPassword[v] < 64)
                    cipherPassword[v] -= 5;
                else if((int)cipherPassword[v] > 90 && (int)cipherPassword[v] <= 96)
                    if(cipherPassword.length % 2 == 0)
                        cipherPassword[v] += 2;
                    else
                        cipherPassword[v] -= 2;

            //Replacing unloved characters
            for(int vi = 0; vi < cipherPassword.length; vi++)
                if((int)cipherPassword[vi] == 34)
                    cipherPassword[vi] = 123;
                else if((int)cipherPassword[vi] == 38)
                    cipherPassword[vi] = 124;
                else if((int)cipherPassword[vi] == 60)
                    cipherPassword[vi] = 125;
                else if((int)cipherPassword[vi] == 62)
                    cipherPassword[vi] = 126;

            //Limitations
            if(ext == 1 || limit < 32)
            {
                char[] cipherPasswordLimited = new char[limit < cipherPassword.length ? limit : cipherPassword.length];
                for (int vii = 0; vii < cipherPassword.length && vii < limit; vii++)
                    cipherPasswordLimited[vii] = cipherPassword[vii];
                return cipherPasswordLimited;
            }

            return cipherPassword;
        }
        catch (Exception ex)
        {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Failed to Encrypt Password");
            error.setHeaderText(null);
            error.setContentText(ex.getMessage());
            error.showAndWait();

            return null;
        }
    }
}
