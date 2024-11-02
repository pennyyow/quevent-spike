package com.quevent.backend.exception;

public class UsernameExistsException extends RuntimeException {
  public UsernameExistsException(String message) {
    super(message);
  }
}
