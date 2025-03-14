package dev.shorturl.exception;

public class TagInUseException extends ResourceNotFoundException {
  public TagInUseException(Long tagId) {
    super(
        "Tag with ID "
            + tagId
            + " cannot be deleted because it is associated with one or more links");
  }
}
