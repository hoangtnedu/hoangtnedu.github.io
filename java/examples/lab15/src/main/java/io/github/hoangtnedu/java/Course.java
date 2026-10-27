package io.github.hoangtnedu.java;
@jakarta.persistence.Entity @jakarta.persistence.Table(name="courses") public class Course {
 @jakarta.persistence.Id @jakarta.persistence.GeneratedValue(strategy=jakarta.persistence.GenerationType.IDENTITY) private Long id;
 private String courseCode;private String courseName;private Integer credits;
 public Long getId(){return id;}public String getCourseCode(){return courseCode;}public void setCourseCode(String x){courseCode=x;}public String getCourseName(){return courseName;}public void setCourseName(String x){courseName=x;}public Integer getCredits(){return credits;}public void setCredits(Integer x){credits=x;}
}