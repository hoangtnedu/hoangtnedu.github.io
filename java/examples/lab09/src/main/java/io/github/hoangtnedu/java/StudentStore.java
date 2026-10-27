package io.github.hoangtnedu.java;
import jakarta.persistence.*;import java.util.*;
public class StudentStore implements AutoCloseable {
 private final EntityManagerFactory emf;
 public StudentStore(){String url=System.getenv().getOrDefault("DB_URL","jdbc:h2:./data/web-labs;MODE=MySQL");emf=Persistence.createEntityManagerFactory("lab",Map.of("jakarta.persistence.jdbc.url",url,"jakarta.persistence.jdbc.user",System.getenv().getOrDefault("DB_USER","sa"),"jakarta.persistence.jdbc.password",System.getenv().getOrDefault("DB_PASSWORD",""),"hibernate.hbm2ddl.auto","update"));}
 public List<Student> list(String q){try(var em=emf.createEntityManager()){return em.createQuery("select s from Student s where lower(s.name) like :q order by s.id",Student.class).setParameter("q","%"+q.toLowerCase()+"%").getResultList();}}
 public void save(Student s){validate(s);try(var em=emf.createEntityManager()){var tx=em.getTransaction();try{tx.begin();if(s.getId()==null)em.persist(s);else{if(em.find(Student.class,s.getId())==null)throw new IllegalArgumentException("Khong tim thay");em.merge(s);}tx.commit();}catch(RuntimeException e){if(tx.isActive())tx.rollback();throw e;}}}
 public Student get(long id){try(var em=emf.createEntityManager()){Student s=em.find(Student.class,id);if(s==null)throw new IllegalArgumentException("Khong tim thay");return s;}}
 public void delete(long id){try(var em=emf.createEntityManager()){var tx=em.getTransaction();try{tx.begin();var s=em.find(Student.class,id);if(s==null)throw new IllegalArgumentException("Khong tim thay");em.remove(s);tx.commit();}catch(RuntimeException e){if(tx.isActive())tx.rollback();throw e;}}}
 public static void validate(Student s){if(s.getCode()==null||s.getCode().isBlank()||s.getName()==null||s.getName().isBlank()||s.getEmail()==null||!s.getEmail().matches("[^@ ]+@[^@ ]+[.][^@ ]+"))throw new IllegalArgumentException("Du lieu khong hop le");}
 public void close(){emf.close();}
}