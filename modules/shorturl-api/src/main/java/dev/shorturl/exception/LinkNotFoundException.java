package dev.shorturl.exception;

public class LinkNotFoundException extends ResourceNotFoundException {
    public LinkNotFoundException(String message) {
        super(message);
    }

    public LinkNotFoundException(Long id) {
        super("Link", id);
    }
}
