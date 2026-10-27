package io.github.hoangtnedu.java;
@jakarta.persistence.Entity @jakarta.persistence.Table(name="enrollments",uniqueConstraints=@jakarta.persistence.UniqueConstraint(columnNames={"student_id","course_id"})) public class Enrollment {
 @jakarta.persistence.Id @jakarta.persistence.GeneratedValue(strategy=jakarta.persistence.GenerationType.IDENTITY) private Long id;
 @jakarta.persistence.ManyToOne(optional=false) @jakarta.persistence.JoinColumn(name="student_id") private Student student;
 @jakarta.persistence.ManyToOne(optional=false) @jakarta.persistence.JoinColumn(name="course_id") private Course course;
 private java.time.LocalDate enrollDate;
 public Long getId(){return id;}public Student getStudent(){return student;}public void setStudent(Student x){student=x;}public Course getCourse(){return course;}public void setCourse(Course x){course=x;}public java.time.LocalDate getEnrollDate(){return enrollDate;}public void setEnrollDate(java.time.LocalDate x){enrollDate=x;}
}