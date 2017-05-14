package cryogen;

import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.stage.Modality;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * This class contains methods for the encryption and decryption of messages and files
 * Additional cryptosystems will be added
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */
public class Cryptography
{
    /**
     *
     * @param ex
     */
    public static void handleException(Exception ex)
    {
        handleException(ex, "Error");
    }

    /**
     *
     * @param ex
     * @param title
     */
    public static void handleException(Exception ex, String title)
    {
        handleException(ex, title, ex.getMessage());
    }

    /**
     *
     * @param ex
     * @param title
     * @param header
     */
    public static void handleException(Exception ex, String title, String header)
    {
        handleException(ex, title, header, ex.toString());
    }

    /**
     *
     * @param ex
     * @param title
     * @param header
     * @param content
     */
    public static void handleException(Exception ex, String title, String header, String content)
    {
        ex.printStackTrace();
        Alert error = new Alert(Alert.AlertType.ERROR, content);
        error.initModality(Modality.APPLICATION_MODAL);
        error.initOwner(null);
        error.setTitle(title);
        error.setHeaderText(header);
        DialogPane dialogPane = error.getDialogPane();
        dialogPane.getStylesheets().add(Cryptography.class.getResource("MidnaDark.css").toExternalForm());
        dialogPane.getStyleClass().add("dlgDefault");
        error.showAndWait();
    }


    /**********************------------------------Vigenère Cipher------------------------**********************/


    /**
     * This class contains the necessary  algorithms for the Vigenère cipher
     */
    public static class VigenereCipher
    {
        /***********------------Text Cryptography------------***********/

        /**
         *
         * @param plainText
         * @param key
         * @return
         */
        public static char[] encrypt(char[] plainText, char[] key)
        {
            return null;
        }

        /**
         *
         * @param cipherText
         * @param key
         * @return
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            return null;
        }

        /***********------------File Cryptography------------***********/

        /**
         *
         * @param plainData
         * @param key
         * @return
         */
        public static void encrypt(File plainFile, char[] key)
        {
            try
            {
                Path path = Paths.get(plainFile.getAbsolutePath());
                final byte[] plainData = Files.readAllBytes(path);
                byte[] cipherData = new byte[plainData.length];

                for(int iii = 0; iii < plainData.length; iii++)
                    cipherData[iii] = (byte) ((int) plainData[iii] + (int) key[iii % key.length]);

                FileOutputStream fos = new FileOutputStream(plainFile.getAbsoluteFile() + ".cg");
                fos.write(cipherData);
                fos.write(cipherData);

                fos.close();
                plainFile.delete();
            }
            catch (FileNotFoundException ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
        }

        /**
         *
         * @param cipherData
         * @param key
         * @return
         */
        public static void decrypt(File cipherFile, char[] key)
        {
            try
            {
                Path path = Paths.get(cipherFile.getAbsolutePath());
                final byte[] cipherData = Files.readAllBytes(path);
                byte[] plainData = new byte[cipherData.length];
                for(int iv = 0; iv < cipherData.length; iv++)
                    plainData[iv] = (byte) ((int) cipherData[iv] - (int) key[iv % key.length]);

                FileOutputStream fos = new FileOutputStream(cipherFile.getAbsolutePath().substring(0, cipherFile.getAbsolutePath().length() - 3));
                fos.write(plainData);
                fos.close();
                cipherFile.delete();
            }
            catch (FileNotFoundException ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
        }
    }


    /**********************------------------------Vernam Cipher------------------------**********************/


    /**
     * This class contains the necessary algorithms for the Vernam cipher to encrypt messages and files
     */
    public static class VernamCipher
    {

        /***********------------Text Cryptography------------***********/

        /**
         *
         * @param plainText
         * @param key
         * @return
         */
        public static char[] encrypt(char[] plainText, char[] key)
        {
            return null;
        }

        /**
         *
         * @param cipherText
         * @param key
         * @return
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            return null;
        }

        /***********------------File Cryptography------------***********/

        /**
         *
         * @param plainData
         * @param key
         * @return
         */
        public static void encrypt(File plainFile, char[] key)
        {
            try
            {
                Path path = Paths.get(plainFile.getAbsolutePath());
                final byte[] plainData = Files.readAllBytes(path);
                byte[] cipherData = new byte[plainData.length];

                for (int vi = 0; vi < plainData.length; vi++)
                    cipherData[vi] = (byte) ((int) plainData[vi] ^ (int) key[vi % key.length]);

                FileOutputStream fos = new FileOutputStream(plainFile.getAbsoluteFile() + ".cg");
                fos.write(cipherData);
                fos.close();
                plainFile.delete();
            }
            catch (FileNotFoundException ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
        }

        /**
         *
         * @param cipherData
         * @param key
         * @return
         */
        public static void decrypt(File cipherFile, char[] key)
        {
            try
            {
                Path path = Paths.get(cipherFile.getAbsolutePath());
                final byte[] cipherData = Files.readAllBytes(path);
                byte[] plainData = new byte[cipherData.length];

                for(int vii = 0; vii < cipherData.length; vii++)
                    plainData[vii] = (byte) ((int) cipherData[vii] ^ (int) key[vii % key.length]);

                FileOutputStream fos = new FileOutputStream(cipherFile.getAbsolutePath().substring(0, cipherFile.getAbsolutePath().length() - 3));
                fos.write(plainData);
                fos.close();
                cipherFile.delete();
            }
            catch (FileNotFoundException ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Cryptography.handleException(ex);
            }
        }
    }


    /******************--------------------Columnar Transposition Cipher--------------------******************/


    /**
     * This class contains the necessary algorithms for the Columnar Transposition cipher to encrypt messages and files
     */
    public static class ColumnarTranspositionCipher
    {

        /***********------------Text Cryptography------------***********/

        /**
         *
         * @param plainText
         * @return
         */
        public static char[] encrypt(char[] plainText)
        {
            return null;
        }

        /**
         *
         * @param cipherText
         * @return
         */
        public static char[] decrypt(char[] cipherText)
        {
            return null;
        }

        /***********------------File Cryptography------------***********/

        /**
         *
         * @param plainData
         * @param key
         * @return
         */
        public static void encrypt(File plainFile, char[] key)
        {
        }

        /**
         *
         * @param cipherData
         * @param key
         * @return
         */
        public static void decrypt(File cipherFile, char[] key)
        {

        }
    }


    /**********************------------------------Elephant Cipher------------------------**********************/


    /**
     * This class contains the necessary algorithms for the Elephant cipher to encrypt messages and files
     * Cipher derived by Zander Labuschagne & Elnette Moller
     */
    public static class ElephantCipher
    {

        /***********------------Text Cryptography------------***********/

        /**
         *
         * @param plainText
         * @param key
         * @return
         */
        public static char[] encrypt(char[] plainText, char[] key)
        {
            return null;
        }

        /**
         *
         * @param cipherText
         * @param key
         * @return
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            return null;
        }

        /***********------------File Cryptography------------***********/

        /**
         *
         * @param plainData
         * @param key
         * @return
         */
        public static void encrypt(File plainFile, char[] key)
        {

        }

        /**
         *
         * @param cipherData
         * @param key
         * @return
         */
        public static void decrypt(File cipherFile, char[] key)
        {

        }
    }

    //Add more algorithms here
    /******************--------------------%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--------------------******************/
}
