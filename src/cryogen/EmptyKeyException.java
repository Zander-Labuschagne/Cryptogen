package cryogen;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * Java exception class used to handle the event where no key is given for encryption/decryption
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */

public class EmptyKeyException extends RuntimeException
{
    /**
     * Default Constructor
     */
    public EmptyKeyException()
    {
        super();
    }

    /**
     * Overloaded Constructor
     * @param ex Exception string containing error message
     */
    public EmptyKeyException(String ex)
    {
        super(ex);
    }
}
