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
import java.util.Arrays;

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
            char[] cipherText = new char[plainText.length];

            //To encrypt english alphabet as well as special characters
            /*for(int i = 0; i < plainText.length; i++)
                 cipherText[i] = (char)((plainText[i]) + key[i % key.length]);*/

            //To encrypt alphabet characters only
            for (int xxxii = 0; xxxii < key.length; xxxii++)//Convert all legal characters of key to uppercase
                if (key[xxxii] >= 97 && key[xxxii] <= 122)
                    key[xxxii] = (char) (key[xxxii] - 32);
                else if (!(key[xxxii] >= 65 && key[xxxii] <= 90))
                    throw new VigenereKeyOutOfRangeException("Key must consist of english alphabetical characters only!");
            for (int i = 0; i < plainText.length; i++)
                if (plainText[i] >= 65 && plainText[i] <= 90)
                    cipherText[i] = (char) (((plainText[i]) - 65 + key[i % key.length] - 65) % 26 + 65);
                else if (plainText[i] >= 97 && plainText[i] <= 122)
                    cipherText[i] = (char) (((plainText[i]) - 97 + key[i % key.length] + 32 - 97) % 26 + 97);
                else
                    cipherText[i] = plainText[i];

            return cipherText;
        }

        /**
         *
         * @param cipherText
         * @param key
         * @return
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            char[] plainText = new char[cipherText.length];

            //To decrypt english alphabet as well as special characters
            /*for(int viii = 0; i < cipherText.length; viii++)
                plainText[viii] = (char)((cipherText[viii]) + key[viii % key.length]);*/

            //To decrypt alphabet characters only
            for (int xxxi = 0; xxxi < key.length; xxxi++)//Convert all legal characters of key to uppercase
                if (key[xxxi] >= 97 && key[xxxi] <= 122)
                    key[xxxi] = (char) (key[xxxi] - 32);
                else if (!(key[xxxi] >= 65 && key[xxxi] <= 90))
                    throw new VigenereKeyOutOfRangeException("Key must consist of english alphabetical characters only!");
            for (int viii = 0; viii < cipherText.length; viii++)
                if (cipherText[viii] >= 65 && cipherText[viii] <= 90)
                    if((((cipherText[viii]) - 65 - key[viii % key.length] + 65) % 26 + 65) < 65)
                        plainText[viii] = (char) (((cipherText[viii]) - 65 - key[viii % key.length] + 65) % 26 + 65 + 26);
                    else
                        plainText[viii] = (char) (((cipherText[viii]) - 65 - key[viii % key.length] + 65) % 26 + 65);
                else if (cipherText[viii] >= 97 && cipherText[viii] <= 122)
                    if((((cipherText[viii]) - 97 - (key[viii % key.length] + 32 - 97)) % 26 + 97) < 97)
                        plainText[viii] = (char) (((cipherText[viii]) - 97 - (key[viii % key.length] + 32 - 97)) % 26 + 97 + 26);
                    else
                        plainText[viii] = (char) (((cipherText[viii]) - 97 - (key[viii % key.length] + 32 - 97)) % 26 + 97);
                else
                    plainText[viii] = cipherText[viii];

            return plainText;
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
            int d;
            int[] asck = new int[b];
            char[] cipher = new char[a];

            for(int j=0; j<b; j++)
                asck[j] = (int)key[j] - 32;

            for(int i = 0; i<a; i++)
            {
                if(c==b)
                    c = 0;

                d = (int)plainText[i] - 32;

                cipher[i] = (char)(((d + asck[c])%95) + 32);
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
            int d;
            int[] asck = new int[b];
            char[]  message = new char[a];

            for(int j=0; j<b; j++)
                asck[j] = (int)key[j] - 32;

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                d = ((int)cipherText[i] - 32) - asck[c];

                if(d<0)
                    message[i] = (char)((95 + d) + 32);
                else
                    message[i] = (char)(d + 32);

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
        private static boolean contains(int[] arr, int xvii)
        {
            for(int xvi = 0; xvi < arr.length; xvi++)
                if(arr[xvi] == xvii)
                    return true;
            return false;
        }

        /***********------------Text Cryptography------------***********/

        /**
         *
         * @param plainText
         * @return
         */
        public static char[] encrypt(char[] plainText, char[] key)
        {
            char[] cipherText = new char[key.length * (plainText.length / key.length + 1)];
            char[][] plainTextCol = new char[plainText.length / key.length + 1][key.length];
            int xiv = 0;
            int xii = 0;

            //Put text in column format
            int x = 0;
            for(int ix = 0; ix < key.length * (plainText.length / key.length + 1); ix++)
            {
                plainTextCol[x][ix % key.length] = ix >= plainText.length ? '|' : plainText[ix];
                if((ix + 1) % key.length == 0)
                    x++;
            }

            int[] blacklist = new int[key.length];
            for(int xv = 0; xv < key.length; xv++)
            {
                int min = 0;
                char cmin = key[0];

                //Finds smallest character in key other than the ones already used
                for (int xi = 0; xi < key.length; xi++)
                    if (cmin > key[xi] && !contains(blacklist, xi))
                    {
                        min = xi;
                        cmin = key[xi];
                    }
                blacklist[xii++] = min;

                //Add to cipher text from column of plain text
                for (int xiii = 0; xiii < plainTextCol.length; xiii++)
                    cipherText[xiv++] = plainTextCol[xiii][min];
            }

            return cipherText;
        }

        /**
         *
         * @param cipherText
         * @return
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            char[] plainText = new char[key.length * (cipherText.length / key.length + 2)];
            char[][] cipherTextCol = new char[key.length + 1][cipherText.length / key.length + 2];
            int xviii = 0;
            int xix = 0;
            char[][] plainTextCol = new char[cipherText.length / key.length + 1][key.length +1];

            //Put cipher text in column format
            int xx = 0;
            int xxvi = 0;
            int xxvii = 1;
            int xxviii = 0;
            int xxix = 0;
            for(int xxi = 0; xxvi < cipherTextCol.length && xxi < cipherText.length && xxix < cipherText.length; xxi++)
            {
                if(xxi == 0)
                {
                    cipherTextCol[xxvi][xx++] = cipherText[xxvi];
                    xxix++;
                }
                if((xxi + xxvii - xxviii) % key.length == 0)
                {
                    if(xxi + xxvii - xxviii != 0)
                    {
                        cipherTextCol[xxvi][xx++] = cipherText[xxi];
                        xxix++;
                        xxvii++;
                    }
                }
                if(xx == key.length)
                {
                    xx = 0;
                    xxvi++;
                    xxi = -1;
                    xxvii = 1;
                    xxviii++;
                }
            }


            int[] blacklist = new int[key.length];

            for(int xxii = 0; xxii < key.length; xxii++)
            {
                char cmin = key[0];
                int min = 0;

                //Finds smallest character in key other than the ones already used
                for (int xi = 0; xi < key.length; xi++)
                    if (cmin > key[xi] && !contains(blacklist, xi))
                    {
                        min = xi;
                        cmin = key[xi];
                    }
                blacklist[xix++] = min;


                for(int xxiv = 0; xxiv < plainTextCol.length; xxiv++)
                    plainTextCol[xxiv][min] = cipherTextCol[xxiv][xxii];


            }

            for (int xiii = 0; xiii < plainTextCol.length; xiii++)
            {
                for (int xxx = 0; xxx < plainTextCol[xiii].length; xxx++)
                    System.out.print(plainTextCol[xiii][xxx] + " ");
                System.out.print("\n");
            }


            //Add to plain text from column of plain text
            /*for (int xiii = 0; xiii < plainTextCol.length; xiii++)
                for(int xxx = 0; xxx < plainTextCol[xiii].length; xxx++)
                    plainText[xviii++] = plainTextCol[xiii][xxx] == '|' ? ' ' : plainTextCol[xiii][xxx];*/

            int xxx = 0;
            for(int xiii = 1; xiii <= cipherText.length; xiii++)
            {
                plainText[xiii-1] = plainTextCol[xxx][(xiii-1) % key.length] == '|' ? ' ' : plainTextCol[xxx][(xiii-1) % key.length];
                if(xiii % key.length == 0)
                {
                    xxx++;
                }
            }

            return plainText;
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
                asck[j] = (int)key[j]-32;

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                if(step3==a)
                    step3 = 0;

                step1 = ((int)plainText[i] - 32)^asck[c];

                step2 = ((step1 + b)%95) + 32;

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

                if(step1==a)
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
