package me.vaxry.harakiri.framework.util;

import me.vaxry.harakiri.Harakiri;

import java.util.logging.Level;

public final class StringUtil {

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            Harakiri.get().getLogger().log(Level.WARNING, "Number format exception thrown when parsing int.");
        }
        return false;
    }

    public static boolean isFloat(String s) {
        try {
            Float.parseFloat(s);
            return true;
        } catch (Exception e) {
            Harakiri.get().getLogger().log(Level.WARNING, "Number format exception thrown when parsing float.");
        }
        return false;
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            Harakiri.get().getLogger().log(Level.WARNING, "Number format exception thrown when parsing double.");
        }
        return false;
    }

    public static boolean isBoolean(String s) {
        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            Harakiri.get().getLogger().log(Level.WARNING, "Number format exception thrown when parsing boolean.");
        }
        return false;
    }

    public static boolean isLong(String s, int radix) {
        try {
            Long.parseLong(s, radix);
            return true;
        } catch (Exception e) {
            Harakiri.get().getLogger().log(Level.WARNING, "Number format exception thrown when parsing long.");
        }
        return false;
    }

    public static boolean isNumber(String s) {
        return isInt(s) || isFloat(s) || isDouble(s) || isLong(s, 16);
    }

    public static float similarityLength(String first, String second) {
        return (float) Math.abs(first.length() - second.length()) / 100;
    }

    public static boolean findMatching(String first, String second) {
        return second.toLowerCase().contains(first.toLowerCase());
    }

    /**
     * Credits https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
     *
     * @param s1 the first string to compare with
     * @param s2 the second string to compare to
     * @returns the levenshtein distance between string s1 and string s2
     */
    public static double levenshteinDistance(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
