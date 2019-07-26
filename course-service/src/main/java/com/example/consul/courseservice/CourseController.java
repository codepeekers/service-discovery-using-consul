package com.example.consul.courseservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {

  private Map<Integer, Course> courseMap = new HashMap<>();

  @PostConstruct
  public void init() {
    courseMap.put(1, new Course(1, "Data Science", 10000));
    courseMap.put(2, new Course(2, "Data Structures", 10023));
  }

  @GetMapping("/details/{courseId}")
  public Course getCourse(@PathVariable("courseId") int courseId) {
    return courseMap.get(courseId);
  }
}
