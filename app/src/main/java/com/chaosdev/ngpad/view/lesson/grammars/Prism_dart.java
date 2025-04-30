package com.chaosdev.ngpad.view.lesson.grammars;

import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;


import androidx.annotation.NonNull;

import io.noties.prism4j.Prism4j;

public class Prism_dart {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {
    return grammar(
        "dart",
        // Strings (multi-line and single-line with interpolation)
        token(
            "string",
            pattern(
                compile("r?(\"\"\"|''')[\\s\\S]*?\\1"), // Multi-line strings
                false,
                true,
                null,
                grammar(
                    "inside",
                    token(
                        "interpolation",
                        pattern(
                            compile(
                                "\\$\\{[^}]*\\}|\\$[a-zA-Z_]\\w*"), // ${expression} or $identifier
                            false,
                            false,
                            null,
                            grammar(
                                "inside",
                                token("punctuation", pattern(compile("^\\$\\{|\\}$|^\\$"))))))))),
        token(
            "string",
            pattern(
                compile("r?['\"][^'\"\\n]*['\"]"), // Single-line strings
                false,
                true,
                null,
                grammar(
                    "inside",
                    token(
                        "interpolation",
                        pattern(
                            compile("\\$\\{[^}]*\\}|\\$[a-zA-Z_]\\w*"),
                            false,
                            false,
                            null,
                            grammar(
                                "inside",
                                token("punctuation", pattern(compile("^\\$\\{|\\}$|^\\$"))))))))),

        // Comments
        token(
            "comment",
            pattern(
                compile("/\\*[\\s\\S]*?\\*/"), // Multi-line comments
                false,
                true)),
        token(
            "comment",
            pattern(
                compile("//.*") // Single-line comments
                )),

        // Keywords
        token(
            "keyword",
            pattern(
                compile(
                    "\\b(?:abstract|async|await|break|case|catch|class|const|continue|covariant|"
                        + "default|deferred|do|dynamic|else|enum|export|extends|extension|external|"
                        + "factory|false|final|finally|for|Function|get|hide|if|implements|import|"
                        + "in|interface|is|late|library|mixin|new|null|on|operator|part|rethrow|"
                        + "return|set|show|static|super|switch|sync|this|throw|true|try|typedef|"
                        + "var|void|while|with|yield)\\b"))),

        // Class names (PascalCase convention)
        token("class-name", pattern(compile("\\b[A-Z][a-zA-Z0-9_]*\\b"))),

        // Numbers (including binary and octal)
        token(
            "number",
            pattern(
                compile(
                    "\\b(?:0x[\\da-fA-F]+|0b[01]+|0o[0-7]+|(?:\\d+\\.?\\d*|\\d*\\.?\\d+)(?:e[-+]?\\d+)?)\\b",
                    CASE_INSENSITIVE))),

        // Operators
        token(
            "operator",
            pattern(
                compile(
                    "=>|\\b(?:as|is)\\b|\\+\\+|--|&&|\\|\\||<<=?|>>>|>>=?|->|\\?|[-+*/%&|^!=<>]=?"))),

        // Functions
        token("function", pattern(compile("\\b[a-zA-Z_][a-zA-Z0-9_]*(?=\\s*\\()"))),

        // Booleans
        token("boolean", pattern(compile("\\b(?:true|false)\\b"))),

        // Punctuation
        token("punctuation", pattern(compile("[{}\\[\\];(),.:]"))),

        // Annotations
        token("annotation", pattern(compile("@[a-zA-Z_]\\w*\\b"))));
    }
}
