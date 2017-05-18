package cryogen;

import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.stage.Modality;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.misc.Queue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
     * method to handle exceptions
     * @param ex Exception thrown to handle
     */
    public static void handleException(Exception ex)
    {
        handleException(ex, "Error");
    }

    /**
     * method to handle exceptions with optional window title
     * @param ex Exception thrown to handle
     * @param title to be displayed in message box
     */
    public static void handleException(Exception ex, String title)
    {
        handleException(ex, title, ex.getMessage());
    }

    /**
     * method to handle exceptions with optional window title and header
     * @param ex Exception thrown to handle
     * @param title to be displayed in message box
     * @param header caption to be displayed in message box
     */
    public static void handleException(Exception ex, String title, String header)
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
        dialogPane.getStylesheets().add(Cryptography.class.getResource(Cryptogen.laf).toExternalForm());
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
         * Encryption method to encrypt text messages with Vigenère cipher
         * @param plainText text about to be encrypted
         * @param key used to encrypt the text
         * @return the encrypted text
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
         * Decryption method to decrypt Vigenère encrypted text.
         * @param cipherText encrypted text about to be decrypted
         * @param key used to decrypt the cryptogram
         * @return the plain text after decryption of encrypted text
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
         * Encryption method to encrypt files with Vigenère cipher
         * @param plainFile to be encrypted
         * @param key used to encrypt the file
         * @return the encrypted file
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
         * Decryption method to decrypt Vigenère encrypted files
         * @param cipherFile encrypted file to decrypt
         * @param key key used to decrypt the file
         * @return the decrypted original file
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
         * Encryption method used to encrypt text message with Vernam cipher
         * @param plainText message about to be encrypted
         * @param key used to encrypt the text
         * @return the encrypted text message
         */
        public static char[] encrypt(char[] plainText, char[] key)
        {
            byte[] cipher = new byte[String.valueOf(plainText).getBytes().length];
            for (int xxxii = 0; xxxii < String.valueOf(plainText).getBytes().length; xxxii++)
                cipher[xxxii] = (byte) (String.valueOf(plainText).getBytes()[xxxii] ^ String.valueOf(key).getBytes()[xxxii % String.valueOf(key).getBytes().length]);
            BASE64Encoder enc = new BASE64Encoder();

            return enc.encode(cipher).replaceAll("\\s", "").toCharArray();
        }

        /**
         * Decryption method used to decrypt Vernam encrypted text messages
         * @param cipherText message about to be decrypted
         * @param key used for decryption
         * @return the plain message text
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            byte[] message = new byte[String.valueOf(cipherText).getBytes().length];

            try
            {
                BASE64Decoder dec = new BASE64Decoder();

                for (int xxxiii = 0; xxxiii < dec.decodeBuffer(String.valueOf(cipherText)).length; xxxiii++)
                    message[xxxiii] = (byte)(dec.decodeBuffer(String.valueOf(cipherText))[xxxiii] ^ String.valueOf(key).getBytes()[xxxiii % String.valueOf(key).getBytes().length]);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            return (new String(message)).toCharArray();
        }

        /***********------------File Cryptography------------***********/

        /**
         * Encryption method used to encrypt files with the Vernam cipher
         * @param plainFile file about to be encrypted
         * @param key used to encrypt the file
         * @return the encrypted file
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
         * Decryption method used to decrypt files encrypted with Vernam cipher
         * @param cipherFile about to be decrypted
         * @param key used for decryption
         * @return decrypted file
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
      /**
      * method to check if array contains a specific element
      * @param arr Array to check
      * @param xvii element to search for
      * @return false if not found and true if FileNotFoundException
      */
        private static boolean contains(int[] arr, int xvii)
        {
            for(int xvi = 0; xvi < arr.length; xvi++)
                if(arr[xvi] == xvii)
                    return true;
            return false;
        }

        /***********------------Text Cryptography------------***********/

        /**
         * Encryption method used to encrypt message via columnar transposition
         * @param plainText about to be encrypted
         * @param key used to encrypt the text
         * @return the encrypted text
         */
        public static char[] encrypt(char[] plainText, char[] key)
        {
            /*char[] cipherText = new char[key.length * (plainText.length / key.length + 1)];
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
            }*/

            int a = key.length+1;
            int b = (plainText.length / a) + 1;
            int c = 0;
            //int d = (b*a) - plainText.length;
            char[] cipherText = new char[b*a];
            char[][] coltrans = new char[a][b];
            char[][] coltrans2 = new char[b][a];

            for(int i=0; i<a; i++)
                for(int j=0; j<b; j++)
                {
                    if(c < plainText.length)
                    {
                        coltrans[i][j] = plainText[c];
                        c++;
                    }
                    else
                        coltrans[i][j] = 0;
                }

            for(int i = 0; i<b; i++)
            {
                for(int j = 0; j<a; j++)
                {
                    coltrans2[i][j] = coltrans[j][i];
                }
            }

            c = 0;

            for(int i=0; i<b; i++)
                for(int j=0; j<a; j++)
                {
                    /*if(j==d)
                        if(i == (a-1))
                            i++;
                    else
                        {
                            cipherText[c] = coltrans2[i][j];
                            c++;
                        }*/

                    cipherText[c] = coltrans2[i][j];
                    c++;
                }

            return cipherText;
        }

        /**
         * Decryption method used to decrypt text via columnar transposition
         * @param cipherText about to be decrypted
         * @return plain text after decryption
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            int a = key.length;
            int b = (cipherText.length / a) + 1;
            int c = 0;
            //int d = (b*a) - cipherText.length;
            char[] plainText = new char[b*a];
            char[][] coltrans = new char[b][a];
            char[][] coltrans2 = new char[a][b];

            for(int i=0; i<b; i++)
                for(int j=0; j<a; j++)
                {
                    if(c < cipherText.length)
                    {
                        coltrans[i][j] = cipherText[c];
                        c++;
                    }
                    else
                        coltrans[i][j] = 0;
                }

            for(int i = 0; i<a; i++)
            {
                for(int j = 0; j<b; j++)
                {
                    coltrans2[i][j] = coltrans[j][i];
                }
            }

            c = 0;

            for(int i=0; i<a; i++)
                for(int j=0; j<b; j++)
                {
                    /*if(j==d)
                        if(i == (a-1))
                            i++;
                    else
                        {
                            cipherText[c] = coltrans2[i][j];
                            c++;
                        }*/

                    plainText[c] = coltrans2[i][j];
                    c++;
                }

            /*char[] plainText = new char[key.length * (cipherText.length / key.length + 2)];
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

           /* int xxx = 0;
            for(int xiii = 1; xiii <= cipherText.length; xiii++)
            {
                plainText[xiii-1] = plainTextCol[xxx][(xiii-1) % key.length] == '|' ? ' ' : plainTextCol[xxx][(xiii-1) % key.length];
                if(xiii % key.length == 0)
                {
                    xxx++;
                }
            }*/

            return plainText;
        }

        /***********------------File Cryptography------------***********/

        /**
         * Encryption method used to encrypt files via columnar transposition
         * @param plainFile about to be encrypted
         * @param key used to encrypt the file
         * @return the encrypted file
         */
        public static void encrypt(File plainFile, char[] key)
        {
            try
            {
                Path path = Paths.get(plainFile.getAbsolutePath());
                final byte[] plainData = Files.readAllBytes(path);
                int a = key.length;
                int b = (plainData.length/a)+1;
               // final int c = 0;
                final byte[][] coltrans = new byte[a][b];
                final byte[][] coltrans2 = new byte[b][a];
                final byte[] cipherData = new byte[a*b];

                Progress progress = new Progress("Encryption");
                // In real life this task would do something useful and return
                // some meaningful result:
                Task<byte[]> task = new Task<byte[]>()
                {
                    @Override
                    public byte[] call() throws InterruptedException
                    {
                       /* for (int vi = 0; vi < plainData.length; vi++)
                        {
                            updateProgress(vi, plainData.length);
                            cipherData[vi] = (byte) ((int) plainData[vi] ^ (int) key[vi % key.length]);
                        }*/

                       int c =0;
                       for(int i=0; i<a; i++)
                       {
                           updateProgress(i, a + 2 * b);
                           for(int j=0; j<b; j++)
                           {
                               if(c<plainData.length)
                               {
                                   coltrans[i][j] = plainData[c];
                                   c++;
                               }
                               else
                                   coltrans[i][j] = 0;
                           }
                       }

                       for(int i=0; i<b; i++)
                       {
                           updateProgress(i, 2 * b);
                           for (int j = 0; j < a; j++)
                               coltrans2[i][j] = coltrans[j][i];
                       }

                       c = 0;

                       for(int i=0; i<b; i++)
                       {
                           updateProgress(i, b);
                           for(int j=0; j<a; j++)
                           {
                               cipherData[c] = coltrans2[i][j];
                               c++;
                           }
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
         * Decryption method used to decrypt files via columnar transposition
         * @param cipherFile about to be decrypted
         * @param key used to decrypt the file
         * @return the original file after decryption
         */
        public static void decrypt(File cipherFile, char[] key)
        {
            try
            {
                Path path = Paths.get(cipherFile.getAbsolutePath());
                final byte[] cipherData = Files.readAllBytes(path);
                int a = key.length;
                int b = (cipherData.length/a)+1;
                // final int c = 0;
                final byte[][] coltrans = new byte[a][b];
                final byte[][] coltrans2 = new byte[b][a];
                final byte[] plainData = new byte[a*b];

                Progress progress = new Progress("Encryption");
                // In real life this task would do something useful and return
                // some meaningful result:
                Task<byte[]> task = new Task<byte[]>()
                {
                    @Override
                    public byte[] call() throws InterruptedException
                    {
                       /* for (int vi = 0; vi < plainData.length; vi++)
                        {
                            updateProgress(vi, plainData.length);
                            cipherData[vi] = (byte) ((int) plainData[vi] ^ (int) key[vi % key.length]);
                        }*/

                        int c =0;
                        for(int i=0; i<a; i++)
                        {
                            updateProgress(i, a + 2 * b);
                            for(int j=0; j<b; j++)
                            {
                                if(c<cipherData.length)
                                {
                                    coltrans[i][j] = cipherData[c];
                                    c++;
                                }
                                else
                                    coltrans[i][j] = 0;
                            }
                        }

                        for(int i=0; i<b; i++)
                        {
                            updateProgress(i, 2 * b);
                            for (int j = 0; j < a; j++)
                                coltrans2[i][j] = coltrans[j][i];
                        }

                        c = 0;

                        for(int i=0; i<b; i++)
                        {
                            updateProgress(i, b);
                            for(int j=0; j<a; j++)
                            {
                                plainData[c] = coltrans2[i][j];
                                c++;
                            }
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


    /**********************------------------------Elephant Cipher------------------------**********************/


    /**
     * This class contains the necessary algorithms for the Elephant cipher to encrypt messages and files
     * Cipher derived by Zander Labuschagne & Elnette Moller
     */
    public static class ElephantCipher
    {

        /***********------------Text Cryptography------------***********/

        /**
         * Encryption method to encrypt text with Elephant Cipher
         * @param plainText text about to be encrypted
         * @param key used for encryption
         * @return encrypted text message
         */
        public static char[] encrypt(char[] plainText, char[] key)
        {
            char[] cipher1 = Cryptography.VernamCipher.encrypt(plainText, key);
            char[] cipher12 = new char[cipher1.length];

            SLL<Character> cipher = new SLL<Character>();

            for(int xxxiv = 0; xxxiv < cipher1.length; xxxiv++)
                cipher.addToTail (new Character(cipher1[xxxiv]));

            for(int xxxv = 0; xxxv < key.length; xxxv++)
                cipher.addToTail(cipher.deleteFromHead());

            for(int xxxvi = 0; !cipher.isEmpty(); xxxvi++)
                cipher12[xxxvi] = cipher.deleteFromHead();

            return cipher12;
        }

        /**
         * Decryption method to decrypt text with Elephant cipher
         * @param cipherText text about to be decrypted
         * @param key used for decryption
         * @return decrypted text
         */
        public static char[] decrypt(char[] cipherText, char[] key)
        {
            char[] message;
            char[] cipher1 = new char[cipherText.length];
            SLL<Character> cipher = new SLL<>();

            for(int xxxvii = 0; xxxvii < cipherText.length; xxxvii++)
                cipher.addToTail(cipherText[xxxvii]);

            for(int xxxviii = 0; xxxviii < key.length; xxxviii++)
                cipher.addToHead(cipher.deleteFromTail());

            for(int xxxix = 0; !cipher.isEmpty(); xxxix++)
                cipher1[xxxix] = cipher.deleteFromHead();

            message = Cryptography.VernamCipher.decrypt(cipher1, key);

            return message;
        }

        /***********------------File Cryptography------------***********/

        /**
         * Encryption used to encrypt files with Elephant cipher
         * @param plainFile to be encrypted
         * @param key used for encryption
         * @return encrypted file
         */
        public static void encrypt(File plainFile, char[] key)
        {
            try
            {
                Path path = Paths.get(plainFile.getAbsolutePath());
                final byte[] plainData = Files.readAllBytes(path);
                final byte[] cipherData = new byte[plainData.length];

                final byte[] cipher12 = new byte[cipherData.length];

                final SLL<Byte> cipher = new SLL<Byte>();

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
                            updateProgress(vi, plainData.length * 3 + key.length);
                            cipherData[vi] = (byte) ((int) plainData[vi] ^ (int) key[vi % key.length]);
                        }

                        for(int xxxx = 0; xxxx < cipher12.length; xxxx++)
                        {
                            updateProgress(xxxx, plainData.length * 2 + key.length);
                            cipher.addToTail(new Byte(cipherData[xxxx]));
                        }

                        for(int xxxxi = 0; xxxxi < key.length; xxxxi++)
                        {
                            updateProgress(xxxxi, plainData.length + key.length);
                            cipher.addToTail(cipher.deleteFromHead());
                        }

                        for(int xxxxii = 0; !cipher.isEmpty(); xxxxii++)
                        {
                            updateProgress(xxxxii, plainData.length);
                            cipher12[xxxxii] = cipher.deleteFromHead();
                        }

                        return cipher12;
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
         * Decryption method for files encrypted with Elephant cipher
         * @param cipherFile to be decrypted
         * @param key used for decryption
         * @return decrypted file
         */
        public static void decrypt(File cipherFile, char[] key)
        {
            try
            {
                Path path = Paths.get(cipherFile.getAbsolutePath());
                final byte[] cipherData = Files.readAllBytes(path);
                final byte[] plainData = new byte[cipherData.length];

                final byte[] cipher1 = new byte[cipherData.length];
                final SLL<Byte> cipher = new SLL<>();

                Progress progress = new Progress("Decryption");
                // In real life this task would do something useful and return
                // some meaningful result:
                Task<byte[]> task = new Task<byte[]>()
                {
                    @Override
                    public byte[] call() throws InterruptedException
                    {
                        for(int xxxxiv = 0; xxxxiv < cipherData.length; xxxxiv++)
                        {
                            updateProgress(xxxxiv, cipherData.length * 3 + key.length);
                            cipher.addToTail(cipherData[xxxxiv]);
                        }

                        for(int xxxxv = 0; xxxxv < key.length; xxxxv++)
                        {
                            updateProgress(xxxxv, cipherData.length * 2 + key.length);
                            cipher.addToHead(cipher.deleteFromTail());
                        }

                        for(int xxxxvi = 0; !cipher.isEmpty(); xxxxvi++)
                        {
                            updateProgress(xxxxvi, cipherData.length * 2);
                            cipher1[xxxxvi] = cipher.deleteFromHead();
                        }

                        for(int xxxxiii = 0; xxxxiii < cipherData.length; xxxxiii++)
                        {
                            updateProgress(xxxxiii, cipherData.length);
                            plainData[xxxxiii] = (byte) ((int) cipher1[xxxxiii] ^ (int) key[xxxxiii % key.length]);
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

    //Add more algorithms here
    /******************--------------------%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--------------------******************/
}
