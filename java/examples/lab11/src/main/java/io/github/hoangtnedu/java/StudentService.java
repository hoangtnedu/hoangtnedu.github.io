package io.github.hoangtnedu.java;
@org.springframework.stereotype.Service public class StudentService {
 private final java.util.Map<Long,Student> store=new java.util.LinkedHashMap<>();private long next=1;
 public StudentService(){Student s=new Student();s.setStudentCode("SV001");s.setFullName("Nguyen Van An");s.setEmail("an@example.com");s.setClassName("JAVA01");save(s);}
 public synchronized java.util.List<Student> list(String q){return store.values().stream().filter(s->s.getFullName().toLowerCase().contains(q.toLowerCase())).toList();}
 public synchronized Student get(long id){if(!store.containsKey(id))throw new IllegalArgumentException("Khong tim thay sinh vien");return store.get(id);}
 public synchronized void save(Student s){if(store.values().stream().anyMatch(x->x.getStudentCode().equals(s.getStudentCode())&&!java.util.Objects.equals(x.getId(),s.getId())))throw new IllegalArgumentException("Trung ma sinh vien");if(s.getId()==null)s.setId(next++);else get(s.getId());store.put(s.getId(),s);}
 public synchronized void delete(long id){get(id);store.remove(id);}
}