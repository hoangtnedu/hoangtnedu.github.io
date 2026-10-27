package io.github.hoangtnedu.java;
public record Student(String id,String name,double attendance,double midterm,double exam) {
 public Student { if(id==null||id.isBlank()||name==null||name.isBlank())throw new IllegalArgumentException("Ma va ten khong duoc rong");GradeCalculator.validate(attendance);GradeCalculator.validate(midterm);GradeCalculator.validate(exam); }
}