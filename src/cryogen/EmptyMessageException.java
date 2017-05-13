package cryogen;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * Java exception class used to handle the event where there is no message for encryption/decryption
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */

public class EmptyMessageException extends RuntimeException
{
    /**
     * Default Constructor
     */
    public EmptyMessageException()
    {
        super();
    }

    /**
     * Overloaded Constructor
     * @param ex Exception string containing error message
     */
    public EmptyMessageException(String ex)
    {
        super(ex);
    }
}
