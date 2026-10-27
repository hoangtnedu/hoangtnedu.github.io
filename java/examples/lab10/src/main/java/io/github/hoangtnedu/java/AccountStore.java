package io.github.hoangtnedu.java;
import jakarta.persistence.*;import java.util.*;
public class AccountStore implements AutoCloseable {
 private final EntityManagerFactory emf;
 public AccountStore(){emf=Persistence.createEntityManagerFactory("lab",Map.of("jakarta.persistence.jdbc.url",System.getenv().getOrDefault("DB_URL","jdbc:h2:./data/web-labs;MODE=MySQL"),"jakarta.persistence.jdbc.user",System.getenv().getOrDefault("DB_USER","sa"),"jakarta.persistence.jdbc.password",System.getenv().getOrDefault("DB_PASSWORD",""),"hibernate.hbm2ddl.auto","update"));}
 public void seed(){try(var em=emf.createEntityManager()){em.getTransaction().begin();for(String u:List.of("admin","staff","user"))if(em.find(Account.class,u)==null)em.persist(new Account(u,org.mindrot.jbcrypt.BCrypt.hashpw(System.getenv().getOrDefault("LAB_DEMO_PASSWORD","change-me-local"),org.mindrot.jbcrypt.BCrypt.gensalt()),u.toUpperCase()));em.getTransaction().commit();}}
 public String authenticate(String u,String p){try(var em=emf.createEntityManager()){var a=em.find(Account.class,u);return a!=null&&org.mindrot.jbcrypt.BCrypt.checkpw(p,a.passwordHash)?a.role:null;}}
 public void close(){emf.close();}
}