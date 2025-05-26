package dev.cats.cookapp.utils;

public class StringConverter {
    public static String toKebabCase(String input) {
        if (input == null) {
            return null;
        }
        String s = input.trim().toLowerCase();
        s = s.replaceAll("[^a-z0-9 ]+", "");
        s = s.replaceAll("\\s+", " ");
        return s.replace(' ', '-');
    }
}
