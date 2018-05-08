package com.duplex.demo.dto;

import java.io.Serializable;

public class CSVDataDTO implements Serializable {

  private String name;
  private int age;
  private String height;

  public CSVDataDTO(String name, int age, String height) {
    this.name = name;
    this.age = age;
    this.height = height;
  }

  public CSVDataDTO() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }
}
