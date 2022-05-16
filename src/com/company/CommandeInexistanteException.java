package com.company;

class CommandeInexistanteException extends Exception{
    public CommandeInexistanteException (String message) {
        super(message);
    }
}
