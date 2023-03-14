package edu.westminstercollege.cmpt355.minijava;

import edu.westminstercollege.cmpt355.minijava.node.*;

public class SyntaxException extends Exception {


    private Node node;

    public SyntaxException() {
        super();
    }

    public SyntaxException(Node node) {
        super();
        this.node = node;
    }

    public SyntaxException(Node node, String message) {
        super(message);
        this.node = node;
    }

    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyntaxException(Throwable cause) {
        super(cause);
    }

    protected SyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Node getNode() {
        return node;
    }
}
