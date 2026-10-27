package io.github.hoangtnedu.java;

public class Student implements java.io.Serializable {
  private Long id;private String code;private String name;private String email;private String className;
 public Student(){}public Student(String code,String name,String email,String className){this.code=code;this.name=name;this.email=email;this.className=className;}
 public Long getId(){return id;}public void setId(Long x){id=x;}public String getCode(){return code;}public void setCode(String x){code=x;}public String getName(){return name;}public void setName(String x){name=x;}public String getEmail(){return email;}public void setEmail(String x){email=x;}public String getClassName(){return className;}public void setClassName(String x){className=x;}
}