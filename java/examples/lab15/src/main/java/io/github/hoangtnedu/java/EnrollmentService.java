package io.github.hoangtnedu.java;
@org.springframework.stereotype.Service public class EnrollmentService {
 private final StudentRepository students;private final CourseRepository courses;private final EnrollmentRepository enrollments;
 public EnrollmentService(StudentRepository s,CourseRepository c,EnrollmentRepository e){students=s;courses=c;enrollments=e;}
 @org.springframework.transaction.annotation.Transactional public void enroll(Long s,Long c){if(enrollments.existsByStudentIdAndCourseId(s,c))throw new IllegalArgumentException("Da dang ky");Enrollment e=new Enrollment();e.setStudent(students.findById(s).orElseThrow(()->new IllegalArgumentException("Khong co sinh vien")));e.setCourse(courses.findById(c).orElseThrow(()->new IllegalArgumentException("Khong co mon hoc")));e.setEnrollDate(java.time.LocalDate.now());enrollments.saveAndFlush(e);}
}