package org.example.apiexam.error;

public class ResourceException extends RuntimeException{
    public ResourceException(String message){
        super(message);
    }
}
