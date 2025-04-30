package com.chaosdev.markdown;

import static io.noties.prism4j.Prism4j.*;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import androidx.annotation.NonNull;

import io.noties.prism4j.Prism4j;

public class Prism_html {

    @NonNull
    public static Prism4j.Grammar create(@NonNull Prism4j prism4j) {
        return grammar("html",
                // Tags: <div>, </div>, <br/>, etc.
                token("tag", pattern(
                        compile("</?(?!\\d)[^\\s>/=$<%]+(?:\\s+[^\\s>/=]+(?:=(?:(\"|')[^\"']*\\1|[^\\s'\">=]+))?)*/?>"),
                        false,
                        true,
                        null,
                        grammar("inside",
                                token("punctuation", pattern(compile("^<\\/?|>|\\/?>"))),
                                token("name", pattern(compile("^[^\\s/>]+")))
                        )
                )),
                // Attributes
                token("attr-name", pattern(compile("\\b\\w+\\b(?=\\s*=)"), false, false)),
                // Attribute values (strings)
                token("attr-value", pattern(compile("(\"|').*?\\1"), false, true)),
                // Comments: <!-- ... -->
                token("comment", pattern(compile("<!--[\\s\\S]*?-->"), false, false))
        );
    }
}