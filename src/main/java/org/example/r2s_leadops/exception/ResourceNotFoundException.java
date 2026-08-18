package org.example.r2s_leadops.exception;

public class ResourceNotFoundException extends RuntimeException {

    private ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException userNotFound(Long userId) {
        return new ResourceNotFoundException("User with id " + userId + " was not found");
    }

    public static ResourceNotFoundException leadNotFound(Long leadId) {
        return new ResourceNotFoundException("Lead with id " + leadId + " was not found");
    }
}
