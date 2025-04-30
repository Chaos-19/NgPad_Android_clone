package com.chaosdev.markdown;

import static io.noties.prism4j.Prism4j.grammar;
import static io.noties.prism4j.Prism4j.pattern;
import static io.noties.prism4j.Prism4j.token;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;

import io.noties.prism4j.Prism4j;

public class Prism_typescript {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {
        return grammar("typescript",
                token("keyword", pattern(
                        compile("\\b(?:function|let|const|var|if|else|for|while|return|class|export|import|new|this|type|interface|implements|extends|public|private|protected|readonly)\\b"),
                        false
                )),
                token("type", pattern(
                        compile("\\b[A-Z][A-Za-z0-9_$]*\\b") // Simple type names (e.g., `string`, `number`, `MyType`)
                )),
                token("function", pattern(
                        compile("\\b[A-Za-z_$][A-Za-z0-9_$]*(?=\\()")
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