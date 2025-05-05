package com.chaosdev.ngpad.model.main;

public class CarouselItem {
  private int imageResId;
  private String title;
  private String buttonAction;

  public CarouselItem(int imageResId, String title, String buttonAction) {
    this.imageResId = imageResId;
    this.title = title;
    this.buttonAction = buttonAction;
  }

  public int getImageResId() {
    return imageResId;
  }

  public String getTitle() {
    return title;
  }

  public String getButtonAction() {
    return buttonAction;
  }
}
