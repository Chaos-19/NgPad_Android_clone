package com.chaosdev.ngpad.model.main;

import com.chaosdev.ngpad.model.Category;
import java.util.ArrayList;
import java.util.List;

public class NgPad {
  private List<Category> categories;

  public NgPad() {
    this.categories = new ArrayList<>();
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void addCategory(Category category) {
    categories.add(category);
  }
}
