package cryogen;

import java.io.File;

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
         * @param plainFile
         * @param key
         * @return
         */
        public static File encrypt(File plainFile, char[] key)
        {
            return null;
        }

        /**
         *
         * @param cipherFile
         * @param key
         * @return
         */
        public static File decrypt(File cipherFile, char[] key)
        {
            return null;
        }
    }


    /**********************------------------------Vernam Cipher------------------------**********************/


    /**
     * This class contains the necessary algorithms for the Vernam cipher to encrypt messages and files
     */
    public static class VernamCipher
    {

        /***********------------Text Cryptography------------***********/

        public static char[] encrypt(char[] plainText, char[] key)
        {
            return null;
        }

        public static char[] decrypt(char[] cipherText, char[] key)
        {
            return null;
        }

        /***********------------File Cryptography------------***********/

        public static File encrypt(File plainFile, char[] key)
        {
            return null;
        }

        public static File decrypt(File cipherFile, char[] key)
        {
            return null;
        }
    }


    /******************--------------------Columnar Transposition Cipher--------------------******************/


    /**
     * This class contains the necessary algorithms for the Columnar Transposition cipher to encrypt messages and files
     */
    public static class ColumnarTranspositionCipher
    {

        /***********------------Text Cryptography------------***********/

        public static char[] encrypt(char[] plainText)
        {
            return null;
        }

        public static char[] decrypt(char[] cipherText)
        {
            return null;
        }

        /***********------------File Cryptography------------***********/

        public static File encrypt(File plainFile, char[] key)
        {
            return null;
        }

        public static File decrypt(File cipherFile, char[] key)
        {
            return null;
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

        public static char[] encrypt(char[] plainText, char[] key)
        {
            return null;
        }

        public static char[] decrypt(char[] cipherText, char[] key)
        {
            return null;
        }

        /***********------------File Cryptography------------***********/

        public static File encrypt(File plainFile, char[] key)
        {
            return null;
        }

        public static File decrypt(File cipherFile, char[] key)
        {
            return null;
        }
    }


    /******************--------------------%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--------------------******************/
}
