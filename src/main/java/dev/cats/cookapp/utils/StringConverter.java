package dev.cats.cookapp.utils;

public class StringConverter {

    public static String toKebabCase(final String input) {
        if (null == input) {
            return null;
        }
        String s = input.trim().toLowerCase();
        s = s.replaceAll("[^a-z0-9 ]+", "");
        s = s.replaceAll("\\s+", " ");
        return s.replace(' ', '-');
    }
}
