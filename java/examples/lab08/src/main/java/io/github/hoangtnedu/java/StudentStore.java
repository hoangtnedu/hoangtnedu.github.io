package io.github.hoangtnedu.java;
import java.util.*;
public class StudentStore implements AutoCloseable {
 private final Map<Long,Student> data=new LinkedHashMap<>();private long next=1;
 public synchronized List<Student> list(String q){return data.values().stream().filter(s->s.getName().toLowerCase().contains(q.toLowerCase())).toList();}
 public synchronized void save(Student s){validate(s);if(data.values().stream().anyMatch(x->x.getCode().equals(s.getCode())&&!Objects.equals(x.getId(),s.getId())))throw new IllegalArgumentException("Trung ma");if(s.getId()==null)s.setId(next++);else if(!data.containsKey(s.getId()))throw new IllegalArgumentException("Khong tim thay");data.put(s.getId(),s);}
 public synchronized Student get(long id){if(!data.containsKey(id))throw new IllegalArgumentException("Khong tim thay");return data.get(id);}
 public synchronized void delete(long id){if(data.remove(id)==null)throw new IllegalArgumentException("Khong tim thay");}
 public static void validate(Student s){if(s.getCode()==null||s.getCode().isBlank()||s.getName()==null||s.getName().isBlank()||s.getEmail()==null||!s.getEmail().matches("[^@ ]+@[^@ ]+[.][^@ ]+"))throw new IllegalArgumentException("Du lieu khong hop le");}
 public void close(){}
}