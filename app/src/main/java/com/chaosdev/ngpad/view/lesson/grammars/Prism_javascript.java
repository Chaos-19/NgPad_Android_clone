package com.chaosdev.markdown;

import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;

import io.noties.prism4j.Prism4j;

public class Prism_javascript {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {
        return grammar("javascript",
                token("keyword", pattern(
                        compile("\\b(?:function|let|const|var|if|else|for|while|return|class|export|import|new|this)\\b"),
                        false
                )),
                token("function", pattern(
                        compile("\\b[A-Za-z_$][A-Za-z0-9_$]*(?=\\()")
                )),
                token("number", pattern(
                        compile("\\b\\d+(\\.\\d+)?\\b")
                )),
                token("string", pattern(
                        compile("[\"'](?:\\\\.|[^\\\\\"'\\n])*[\"']"),
                        false,
                        true
                )),
                token("operator", pattern(
                        compile("[+\\-*/%&|^<>!]=?|&&|\\|\\||\\?|:|===|==|=|!=|!==|\\b(?:instanceof|in)\\b")
                )),
                token("comment", pattern(
                        compile("//.*|/\\*[\\s\\S]*?\\*/")
                ))
        );
    }
}