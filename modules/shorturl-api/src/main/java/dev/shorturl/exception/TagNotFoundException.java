package dev.shorturl.exception;

public class TagNotFoundException extends ResourceNotFoundException {
    public TagNotFoundException(String message) {
        super(message);
    }
    
    public TagNotFoundException(Long id) {
        super("Tag", id);
    }
}