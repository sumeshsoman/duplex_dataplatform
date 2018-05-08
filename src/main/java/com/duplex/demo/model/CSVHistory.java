package com.duplex.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CSVHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonIgnore
  private long id;

  private long numFiles = 0;
  private long numLines = 0;

  public long getNumFiles() {
    return numFiles;
  }

  public void setNumFiles(long numFiles) {
    this.numFiles = numFiles;
  }

  public void addNumFiles(long numFiles) {
    this.numFiles += numFiles;
  }

  public long getNumLines() {
    return numLines;
  }

  public void setNumLines(long numLines) {
    this.numLines = numLines;
  }

  public void addNumLines(long numLines) {
    this.numLines += numLines;
  }
}
