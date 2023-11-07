package ru.abolsoft.infr.api.utils;

import java.util.regex.Pattern;

class InnerUtils {

    private final static Pattern VERSION_NUMBER_PATTERN = Pattern.compile("^\\d+(\\.\\d+){0,2}$");

    public static void checkVersionNumber(String version, Object targetMethodOrType) {
        if (!matchVersionNumber(version)) {
            throw new IllegalArgumentException(String.format("Invalid version number: @ApiVersion(\"%s\") at %s", version, targetMethodOrType));
        }
    }

    public static boolean matchVersionNumber(String version) {
        return !version.isEmpty();
    }
}
