package com.chaosdev.markdown;

import androidx.annotation.NonNull;
import com.chaosdev.ngpad.view.lesson.grammars.Prism_dart;
import io.noties.prism4j.GrammarLocator;
import io.noties.prism4j.Prism4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MyGrammarLocator implements GrammarLocator {

  @Override
  public Prism4j.Grammar grammar(@NonNull Prism4j prism4j, @NonNull String language) {
    switch (language) {
      case "json":
        return Prism_json.create(prism4j);
      case "javascript":
        return Prism_javascript.create(prism4j);
      case "typescript":
        return Prism_typescript.create(prism4j);
      case "html":
        return Prism_html.create(prism4j);
      case "css":
        return Prism_css.create(prism4j);
      case "dart":
        return Prism_dart.create(prism4j);
      default:
        return null;
    }
  }

  @Override
  public Set<String> languages() {
    final Set<String> languages = new HashSet<>();
    languages.add("json");
    languages.add("javascript");
    languages.add("typescript");
    languages.add("html");
    languages.add("css");
    languages.add("dart");
    return Collections.unmodifiableSet(languages);
  }
}
