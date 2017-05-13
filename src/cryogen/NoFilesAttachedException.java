package cryogen;

/**
 * @author Zander Labuschagne
 * E-Mail: ZANDER.LABUSCHAGNE@PROTONMAIL.CH
 * Java exception class used to handle the event where no files are attached for encryption/decryption
 * Copyright (C) 2017  Zander Labuschagne and Elnette Moller
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation
 */

public class NoFilesAttachedException extends RuntimeException
{
    /**
     * Default Constructor
     */
    public NoFilesAttachedException()
    {
        super();
    }

    /**
     * Overloaded Constructor
     * @param ex Exception string containing error message
     */
    public NoFilesAttachedException(String ex)
    {
        super(ex);
    }
}
