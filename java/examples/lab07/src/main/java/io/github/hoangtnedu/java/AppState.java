package io.github.hoangtnedu.java;
import jakarta.servlet.*;import jakarta.servlet.annotation.WebListener;import java.util.*;
@WebListener public class AppState implements ServletContextListener {
 public static StudentStore store;public static final Map<String,String> users=new HashMap<>();
 public void contextInitialized(ServletContextEvent e){store=new StudentStore();String hash=org.mindrot.jbcrypt.BCrypt.hashpw(System.getenv().getOrDefault("LAB_DEMO_PASSWORD","change-me-local"),org.mindrot.jbcrypt.BCrypt.gensalt());users.put("admin",hash);users.put("staff",hash);users.put("user",hash);e.getServletContext().log("HoangTN student application started");}
 public void contextDestroyed(ServletContextEvent e){if(store!=null)store.close();e.getServletContext().log("HoangTN student application stopped");}
 public static String authenticate(String user,String password){return users.containsKey(user)&&org.mindrot.jbcrypt.BCrypt.checkpw(password,users.get(user))?user.toUpperCase():null;}
}