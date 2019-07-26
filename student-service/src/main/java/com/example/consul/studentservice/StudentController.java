package com.example.consul.studentservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
@Slf4j
public class StudentController {

  private List<Student> students = new ArrayList<>();

  @PostConstruct
  public void init() {
    students.add(new Student("Jim", "jim@gmail.com", 1));
    students.add(new Student("John", "john@gmail.com", 2));
  }

  @Autowired private RestTemplate restTemplate;
  @Autowired private DiscoveryClient discoveryClient;
  @Autowired private LoadBalancerClient loadBalancerClient;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @GetMapping("/list")
  public List<StudentDTO> getStudents() {
    log.info("Request received to list students");
    List<String> instances =
        discoveryClient.getInstances("course-service").stream()
            .map(serviceInstance -> serviceInstance.getHost() + ":" + serviceInstance.getPort())
            .collect(Collectors.toList());
    log.info("Server insances {}", instances);

    return students.stream().map(this::createStudentDTO).collect(Collectors.toList());
  }

  private StudentDTO createStudentDTO(Student student) {
    ServiceInstance serviceInstance = loadBalancerClient.choose("course-service");
    String url =
        "http://"
            + serviceInstance.getHost()
            + ":"
            + serviceInstance.getPort()
            + "/courses/details/"
            + student.getCourseId();
    log.info("Making call to {}", url);
    Course course = restTemplate.getForObject(url, Course.class);
    return new StudentDTO(student.getName(), student.getEmailId(), course);
  }
}
