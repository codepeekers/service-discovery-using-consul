package com.example.consul.courseservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
  private int courseId;
  private String courseName;
  private int fees;
}
