package io.github.hoangtnedu.java;
@jakarta.persistence.Entity @jakarta.persistence.Table(name="web_accounts")
public class Account {@jakarta.persistence.Id public String username;public String passwordHash;public String role;public Account(){}public Account(String u,String h,String r){username=u;passwordHash=h;role=r;}}