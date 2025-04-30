package com.chaosdev.markdown;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;

import io.noties.prism4j.Prism4j;

public class Prism_css {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {
        return grammar("css",
                // Selectors: .class, #id, div, etc.
                token("selector", pattern(
                        compile("[^{}\\s][^{}]*?(?=\\s*\\{)"),
                        false,
                        false
                )),
                // Properties: color, font-size
                token("property", pattern(
                        compile("\\b(?:color|font-size|margin|padding|width|height|display|background)\\b(?=\\s*:)"),
                        false,
                        false
                )),
                // Values: red, 12px, #fff, etc.
                token("value", pattern(
                        compile("(?:\\b(?:red|blue|green|block|none|absolute)\\b|\\d+px|#[A-Fa-f0-9]{3,6}|rgba?\\([^)]+\\))"),
                        false,
                        false
                )),
                // Punctuation: {, }, :, ;
                token("punctuation", pattern(compile("[{}:;]"))),
                // Numbers: 12, 3.5, 100%
                token("number", pattern(compile("\\b\\d+(\\.\\d+)?%?\\b"))),
                // !important
                token("keyword", pattern(compile("!important")))
        );
    }
}