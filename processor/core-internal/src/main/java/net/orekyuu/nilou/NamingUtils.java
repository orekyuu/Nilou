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
        int deps = 0;
        while (true) {
            int num = deps;
            Long collect = strings.stream().map(it -> num < it.length ? it[num] : null).distinct().count();
            if (collect.intValue() != 1) {
                break;
            }
            deps++;
        }
        int from = deps;
        return strings.stream()
                .map(it -> Arrays.copyOfRange(it, from, it.length))
                .map(it -> String.join(".", it))
                .toList();
    }
}
