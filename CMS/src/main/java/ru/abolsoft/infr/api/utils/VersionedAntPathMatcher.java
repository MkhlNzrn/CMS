package ru.abolsoft.infr.api.utils;

import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VersionedAntPathMatcher extends AntPathMatcher {
    private final Pattern VERSIONED_PATH_REGEX;
    Set<String> lastVersionAliases = new HashSet<>();

    public VersionedAntPathMatcher(ApiVersionProperties apiVersionProperties) {
        String prefix = apiVersionProperties.getUriPrefix();
        String versionPrefix = apiVersionProperties.getVersionPrefix();
        lastVersionAliases = apiVersionProperties.getLastVersionAliases();
        StringBuilder aliasesBuilder = new StringBuilder();
        String aliases;
        if (!lastVersionAliases.isEmpty()){
            lastVersionAliases.forEach(string -> {
                aliasesBuilder.append("|");
                aliasesBuilder.append(string);
            });
        }
        aliases = aliasesBuilder.toString();

        VERSIONED_PATH_REGEX = Pattern.compile("/"+prefix+"/(?<version>"+versionPrefix+"\\d{1,2}"+
                aliases
                +")/.*");
    }

    @Override
    protected boolean doMatch(String pattern, String path, boolean fullMatch, Map<String, String> uriTemplateVariables) {
        if (path == null) {
            return super.doMatch(pattern, path, fullMatch, uriTemplateVariables);
        }

        Matcher patternMatcher = VERSIONED_PATH_REGEX.matcher(pattern);
        Matcher pathMatcher = VERSIONED_PATH_REGEX.matcher(path);
        var p1 = !patternMatcher.find();
        var p2= !pathMatcher.find();
        if (p1 || p2) {
            return super.doMatch(pattern, path, fullMatch, uriTemplateVariables);
        }
        var pathMatcherVersion = getApiVersion(pathMatcher);
        var patternMatcherVersion = getApiVersion(patternMatcher);
        var p = wildcardVersionApi(patternMatcher);
        boolean superMath = super.doMatch(p, path, fullMatch, uriTemplateVariables);;

        return pathMatcherVersion >= patternMatcherVersion && superMath;
    }

    private int getApiVersion(Matcher pathMatcher) {
        Optional<String> versionStr = Optional.ofNullable(pathMatcher.group("version"));
        if (versionStr.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        if (lastVersionAliases.stream().anyMatch(string -> string.equals(versionStr.get()))) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(versionStr.map(it -> it.substring(1)).get());
    }

    private String wildcardVersionApi(Matcher pattern) {
        String versionStr = pattern.group("version");
        if (versionStr == null) return pattern.group();
        return pattern.group().replaceFirst(versionStr, "*");
    }


    @Override
    public Comparator<String> getPatternComparator(String path) {
        return ((Comparator<String>) (pattern1, pattern2) -> {
            if (pattern1 == null || pattern2 == null) {
                return 0;
            }

            Matcher pathMatcher = VERSIONED_PATH_REGEX.matcher(path);
            Matcher pattern1Matcher = VERSIONED_PATH_REGEX.matcher(pattern1);
            Matcher pattern2Matcher = VERSIONED_PATH_REGEX.matcher(pattern2);
            if (pathMatcher.matches() && pattern1Matcher.matches() && pattern2Matcher.matches()) {
                int pathVersion = getApiVersion(pathMatcher);
                int pattern1Version = getApiVersion(pattern1Matcher);
                int pattern2Version = getApiVersion(pattern2Matcher);

                boolean pattern1EqualsPath = pattern1Version == pathVersion;
                boolean pattern2EqualsPath = pattern2Version == pathVersion;
                if (pattern1EqualsPath && pattern2EqualsPath) {
                    return 0;
                } else if (pattern1EqualsPath) {
                    return -1;
                } else if (pattern2EqualsPath) {
                    return 1;
                }
                return pattern2Version - pattern1Version;
            }
            return 0;
        }).thenComparing(super.getPatternComparator(path));
    }

}
