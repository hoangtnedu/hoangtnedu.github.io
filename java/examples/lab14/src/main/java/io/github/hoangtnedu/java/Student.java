package io.github.hoangtnedu.java;
@jakarta.persistence.Entity @jakarta.persistence.Table(name="students",uniqueConstraints=@jakarta.persistence.UniqueConstraint(columnNames="studentCode")) public class Student {
 @jakarta.persistence.Id @jakarta.persistence.GeneratedValue(strategy=jakarta.persistence.GenerationType.IDENTITY) private Long id;
 @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(min=5) private String studentCode;
 @jakarta.validation.constraints.NotBlank private String fullName;
 @jakarta.validation.constraints.Email private String email;
 @jakarta.validation.constraints.NotBlank private String className;
 public Long getId(){return id;}public void setId(Long x){id=x;}public String getStudentCode(){return studentCode;}public void setStudentCode(String x){studentCode=x;}public String getFullName(){return fullName;}public void setFullName(String x){fullName=x;}public String getEmail(){return email;}public void setEmail(String x){email=x;}public String getClassName(){return className;}public void setClassName(String x){className=x;}
}