package net.orekyuu.nilou;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NamingUtils {
    public static String upperCamel(String... words) {
        if (words.length <= 1) {
            return String.join("", words);
        }
        return Arrays.stream(words).map(NamingUtils::upperCamel).collect(Collectors.joining());
    }

    public static String upperCamel(String word) {
        if (word.length() <= 1) {
            word = word.toUpperCase();
        } else {
            word = String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1);
        }
        return word;
    }

    public static String lowerCamel(String... words) {
        if (words.length <= 1) {
            return String.join("", words);
        }
        StringBuilder builder = new StringBuilder(lowerCamel(words[0]));
        for (int i = 1; i < words.length; i++) {
            builder.append(upperCamel(words[i]));
        }
        return builder.toString();
    }

    public static String lowerCamel(String word) {
        if (word.length() <= 1) {
            word = word.toLowerCase();
        } else {
            word = String.valueOf(word.charAt(0)).toLowerCase() + word.substring(1);
        }
        return word;
    }

    public static List<String> shortClasNames(List<String> classNames) {
        List<String[]> strings = classNames.stream().map(it -> it.split("\\.")).toList();
        int maxDeps = strings.stream().mapToInt(it -> it.length).max().orElse(0);
        int diffPosition = maxDeps - 1;
        for (int i = 0; i < maxDeps; i++) {
            int num = i;
            Long collect = strings.stream().map(it -> it[num]).distinct().count();
            if (collect.intValue() != 1) {
                diffPosition = i;
                break;
            }
        }
        int num = diffPosition;
        return strings.stream()
                .map(it -> Arrays.copyOfRange(it, num, it.length))
                .map(it -> String.join(".", it))
                .toList();
    }
}
