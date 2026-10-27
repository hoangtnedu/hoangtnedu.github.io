package io.github.hoangtnedu.java;
@org.springframework.stereotype.Service public class StudentService {
 private final StudentRepository repo;public StudentService(StudentRepository r){repo=r;}
 public java.util.List<Student> list(String q){return repo.findByFullNameContainingIgnoreCase(q);}
 public Student get(long id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("Khong tim thay sinh vien"));}
 @org.springframework.transaction.annotation.Transactional public void save(Student s){if(repo.existsByStudentCodeAndIdNot(s.getStudentCode(),s.getId()==null?-1L:s.getId()))throw new IllegalArgumentException("Trung ma sinh vien");if(s.getId()!=null)get(s.getId());repo.saveAndFlush(s);}
 public void delete(long id){get(id);repo.deleteById(id);}
}