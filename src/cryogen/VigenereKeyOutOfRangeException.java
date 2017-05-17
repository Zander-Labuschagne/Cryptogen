package cryogen;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * Java exception class used to handle the event where the characters for Vigenere text cryptography is not in english alphabet range
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */

public class VigenereKeyOutOfRangeException extends RuntimeException
{
    /**
     * Default Constructor
     */
    public VigenereKeyOutOfRangeException()
    {
        super();
    }

    /**
     * Overloaded Constructor
     * @param ex Exception string containing error message
     */
    public VigenereKeyOutOfRangeException(String ex)
    {
        super(ex);
    }
}
