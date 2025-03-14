package dev.shorturl.helpers;

public class NameUtils {

  public static String[] splitFullName(String fullName) {
    if (fullName == null || fullName.trim().isEmpty()) {
      return new String[] {"", ""};
    }

    String[] parts = fullName.trim().split("\\s+");

    return switch (parts.length) {
      case 1 -> new String[] {parts[0], ""};
      case 2 -> new String[] {parts[0], parts[1]};
      case 3 -> new String[] {parts[0], parts[1] + " " + parts[2]};
      case 4 -> new String[] {parts[0] + " " + parts[1], parts[2] + " " + parts[3]};
      default -> processMoreThanFour(parts);
    };
  }

  private static String[] processMoreThanFour(String[] parts) {
    String firstName = parts[0];
    StringBuilder lastName = new StringBuilder();
    for (int i = 1; i < parts.length; i++) {
      lastName.append(parts[i]);
      if (i < parts.length - 1) {
        lastName.append(" ");
      }
    }
    return new String[] {firstName, lastName.toString()};
  }
}
