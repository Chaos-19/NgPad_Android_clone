package com.chaosdev.ngpad.model.main;

public class UserQuizAnswer {
  public String optionKey;
  public Integer selectedOptionIndex;

  public UserQuizAnswer(String optionKey, Integer selectedOptionIndex) {
    this.optionKey = optionKey;
    this.selectedOptionIndex = selectedOptionIndex;
  }
}
