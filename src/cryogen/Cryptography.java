package cryogen;

import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.stage.Modality;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * @author Elnette Moller
 * E-Mail: elnette.moller@gmail.com
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
        if(ex != null)
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
            int a = plainText.length;
            int b = key.length;
            int c = 0;
            int[] asck = new int[b];
            char[] cipher = new char[a];

            for(int j=0; j<b; j++)
                asck[j] = (int) key[j];

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                int r = ((int)plainText[i]) + asck[c];

                if(r > 126)
                    r = 127 - r + 32;

                cipher[i] = (char)r;

                c++;
            }

            return cipher;
        }

        /**
         *
         * @param cipherText
         * @param key
         * @return
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            int a = cipherText.length;
            int b = key.length;
            int c = 0;
            int[] asck = new int[b];
            char[]  message = new char[a];

            for(int j=0; j<b; j++)
                asck[j] = (int) key[j];

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                int r = ((int)cipherText[i]) - asck[c];

                if(r < 32)
                    r = 127 - (32 - r);

                message[i] = (char)r;

                c++;
            }

            return message;
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

                Progress progress = new Progress("Encryption");

                // In real life this task would do something useful and return
                // some meaningful result:
                Task<byte[]> task = new Task<byte[]>()
                {
                    @Override
                    public byte[] call() throws InterruptedException
                    {
                        for(int iii = 0; iii < plainData.length; iii++)
                        {
                            updateProgress(iii, plainData.length);
                            cipherData[iii] = (byte) ((int) plainData[iii] + (int) key[iii % key.length]);
                        }

                        return cipherData;
                    }
                };
                // binds progress of progress bars to progress of task:
                progress.activateProgressBar(task);

                // in real life this method would get the result of the task
                // and update the UI based on its value:
                task.setOnSucceeded(event ->
                {
                    try
                    {
                        progress.getDialogStage().close();
                        FileOutputStream fos = new FileOutputStream(plainFile.getAbsoluteFile() + ".cg");
                        fos.write(task.getValue());
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
                });

                progress.getDialogStage().show();

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
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

                Progress progress = new Progress("Decryption");
                // In real life this task would do something useful and return
                // some meaningful result:
                Task<byte[]> task = new Task<byte[]>()
                {
                    @Override
                    public byte[] call() throws InterruptedException
                    {
                        for(int iv = 0; iv < cipherData.length; iv++)
                        {
                            updateProgress(iv, cipherData.length);
                            plainData[iv] = (byte) ((int) cipherData[iv] - (int) key[iv % key.length]);
                        }

                        return plainData;
                    }
                };
                // binds progress of progress bars to progress of task:
                progress.activateProgressBar(task);

                // in real life this method would get the result of the task
                // and update the UI based on its value:
                task.setOnSucceeded(event ->
                {
                    try
                    {
                        progress.getDialogStage().close();
                        FileOutputStream fos = new FileOutputStream(cipherFile.getAbsolutePath().substring(0, cipherFile.getAbsolutePath().length() - 3));
                        fos.write(task.getValue());
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
                });

                progress.getDialogStage().show();

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
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
            int a = plainText.length;
            int b = key.length;
            int c = 0;
            int[] asck = new int[b];
            char[] cipher = new char[a];

            for(int j=0; j<b; j++)
                asck[j] = (int)key[j];

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                cipher[i] = (char) (plainText[i] ^ asck[c]);

                c++;
            }

            return cipher;
        }

        /**
         *
         * @param cipherText
         * @param key
         * @return
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            int a = cipherText.length;
            int b = key.length;
            int c = 0;
            int[] asck = new int[b];
            char[]  message = new char[a];

            for(int j=0; j<b; j++)
                asck[j] = (int)key[j];

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                message[i] = (char)((int)cipherText[i] ^ asck[c]);

                c++;
            }

            return message;
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

                Progress progress = new Progress("Encryption");
                // In real life this task would do something useful and return
                // some meaningful result:
                Task<byte[]> task = new Task<byte[]>()
                {
                    @Override
                    public byte[] call() throws InterruptedException
                    {
                        for (int vi = 0; vi < plainData.length; vi++)
                        {
                            updateProgress(vi, plainData.length);
                            cipherData[vi] = (byte) ((int) plainData[vi] ^ (int) key[vi % key.length]);
                        }

                        return cipherData;
                    }
                };
                // binds progress of progress bars to progress of task:
                progress.activateProgressBar(task);

                // in real life this method would get the result of the task
                // and update the UI based on its value:
                task.setOnSucceeded(event ->
                {
                    try
                    {
                        progress.getDialogStage().close();
                        FileOutputStream fos = new FileOutputStream(plainFile.getAbsoluteFile() + ".cg");
                        fos.write(task.getValue());
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
                });

                progress.getDialogStage().show();

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
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

                Progress progress = new Progress("Decryption");
                // In real life this task would do something useful and return
                // some meaningful result:
                Task<byte[]> task = new Task<byte[]>()
                {
                    @Override
                    public byte[] call() throws InterruptedException
                    {
                        for(int vii = 0; vii < cipherData.length; vii++)
                        {
                            updateProgress(vii, cipherData.length);
                            plainData[vii] = (byte) ((int) cipherData[vii] ^ (int) key[vii % key.length]);
                        }

                        return plainData;
                    }
                };
                // binds progress of progress bars to progress of task:
                progress.activateProgressBar(task);

                // in real life this method would get the result of the task
                // and update the UI based on its value:
                task.setOnSucceeded(event ->
                {
                    try
                    {
                        progress.getDialogStage().close();
                        FileOutputStream fos = new FileOutputStream(cipherFile.getAbsolutePath().substring(0, cipherFile.getAbsolutePath().length() - 3));
                        fos.write(task.getValue());
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
                });

                progress.getDialogStage().show();

                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
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
            int a = plainText.length;
            int b = key.length;
            int c = 0;
            int[] asck = new int[b];
            int step1;
            int step2;
            int step3 = b + 1;
            char[] cipher = new char[a];

            for(int j=0; j<b; j++)
                asck[j] = (int)key[j];

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                if(step3>a)
                    step3 = 0;

                step1 = (char)((int)plainText[i] ^ asck[c]);
                step2 = step1 + b;

                if(step2>126)
                    step2 = 127 - step2 + 32;

                cipher[step3] = (char)step2;

                c++;
                step3++;
            }
            return cipher;
        }

        /**
         *
         * @param cipherText
         * @param key
         * @return
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            int a = cipherText.length;
            int b = key.length;
            int c = 0;
            int[] asck = new int[b];
            int step1 = b + 1;
            int step2;
            int step3;
            char[] message = new char[a];

            for(int j=0; j<b; j++)
                asck[j] = (int)key[j];

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                if(step1>a)
                    step1 = 0;

                step2 = (int)cipherText[step1] - b;

                if(step2<32)
                    step2 = 127 - (32-step2);

                step3 = step2 ^ asck[c];

                message[i] = (char)step3;

                c++;
                step1++;
            }
            return message;
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
