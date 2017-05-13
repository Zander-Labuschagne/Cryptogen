package cryogen;

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
            char[] cypher = new char[a];

            for(int j=0; j<b; j++)
                asck[j] = (int) key[j];

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                int r = ((int)plainText[i]) + asck[c];

                if(r > 126)
                    r = 127 - r + 32;

                cypher[i] = (char)r;

                c++;
            }

            return cypher;
        }

        /**
         *
         * @param cipherText
         * @param key
         * @return    -fx-border-color: rgb(15, 15, 15);
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
        public static byte[] encrypt(byte[] plainData, char[] key)
        {
            byte[] cipherData = new byte[plainData.length];

            for(int iii = 0; iii < plainData.length; iii++)
                cipherData[iii] = (byte) ((int) plainData[iii] + (int) key[iii % key.length]);

            return cipherData;
        }

        /**
         *
         * @param cipherData
         * @param key
         * @return
         */
        public static byte[] decrypt(byte[] cipherData, char[] key)
        {
            byte[] plainData = new byte[cipherData.length];

            for(int iv = 0; iv < cipherData.length; iv++)
                plainData[iv] = (byte) ((int) cipherData[iv] - (int) key[iv % key.length]);

            return plainData;
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
            char[] cypher = new char[a];

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                cypher[i] = (char) (plainText[i] ^ key[c]);

                c++;
            }

            return cypher;
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
            char[]  message = new char[a];

            for(int i=0; i<a; i++)
            {
                if(c == b)
                    c = 0;

                message[i] = (char)((int)cipherText[i] ^ key[c]);

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
        public static byte[] encrypt(byte[] plainData, char[] key)
        {
            byte[] cipherData = new byte[plainData.length];
            for(int vii = 0; vii < plainData.length; vii++)
                cipherData[vii] = (byte) ((int) plainData[vii] ^ (int) key[vii % key.length]);

            return cipherData;
        }

        /**
         *
         * @param cipherData
         * @param key
         * @return
         */
        public static byte[] decrypt(byte[] cipherData, char[] key)
        {
            byte[] plainData = new byte[cipherData.length];
            for(int viii = 0; viii < cipherData.length; viii++)
                plainData[viii] = (byte) ((int) cipherData[viii] ^ (int) key[viii % key.length]);

            return plainData;
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
        public static byte[] encrypt(byte[] plainData, char[] key)
        {
            return null;
        }

        /**
         *
         * @param cipherData
         * @param key
         * @return
         */
        public static byte[] decrypt(byte[] cipherData, char[] key)
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
        public static byte[] encrypt(byte[] plainData, char[] key)
        {
            return null;
        }

        /**
         *
         * @param cipherData
         * @param key
         * @return
         */
        public static byte[] decrypt(byte[] cipherData, char[] key)
        {
            return null;
        }
    }

    //Add more algorithms here
    /******************--------------------%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%--------------------******************/
}
