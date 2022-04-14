package com.project.reddit.exception;

public class Unauthorized extends RuntimeException{
    private static final long serialVersionUID = -4146860850978717726L;

    public Unauthorized(final String message){
        super(message);
    }

}
