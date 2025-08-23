package com.tommasomolesti.cassa_sagra_be.exception;

public class ArticleInUseException extends RuntimeException {
    public ArticleInUseException(String message) {
        super(message);
    }
}
