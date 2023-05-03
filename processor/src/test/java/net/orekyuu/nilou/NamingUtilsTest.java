package net.orekyuu.nilou;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NamingUtilsTest {
    @Test
    public void testCases() {
        assertEquals(NamingUtils.shortClasNames(List.of("net.example.controller.Hoge", "net.example.controller.Fuga")),
                List.of("Hoge", "Fuga"));

        assertEquals(NamingUtils.shortClasNames(List.of("net.example.controller.Hoge", "net.example.controller.piyo.Fuga")),
                List.of("Hoge", "piyo.Fuga"));

        assertEquals(NamingUtils.shortClasNames(List.of("net.example.controller.piyo.Hoge", "net.example.controller.piyo.Fuga")),
                List.of("Hoge", "Fuga"));

        assertEquals(NamingUtils.shortClasNames(List.of("net.example.controller.foo.Hoge", "net.example.controller.bar.Fuga")),
                List.of("foo.Hoge", "bar.Fuga"));

        assertEquals(NamingUtils.shortClasNames(List.of()),
                List.of());

        assertEquals(NamingUtils.shortClasNames(List.of("Hoge", "Fuga")),
                List.of("Hoge", "Fuga"));

        assertEquals(NamingUtils.shortClasNames(List.of("net.example.Hoge")),
                List.of("Hoge"));
    }

}