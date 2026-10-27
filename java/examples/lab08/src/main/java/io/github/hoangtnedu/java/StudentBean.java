package io.github.hoangtnedu.java;
@jakarta.inject.Named @jakarta.enterprise.context.RequestScoped public class StudentBean {
 @jakarta.validation.constraints.NotBlank private String code;
 @jakarta.validation.constraints.NotBlank private String name;
 @jakarta.validation.constraints.Email private String email;
 public String getCode(){return code;}public void setCode(String x){code=x;}public String getName(){return name;}public void setName(String x){name=x;}public String getEmail(){return email;}public void setEmail(String x){email=x;}
 public java.util.List<Student> getStudents(){return AppState.store.list("");}
 public String save(){try{AppState.store.save(new Student(code,name,email,"Demo"));jakarta.faces.context.FacesContext.getCurrentInstance().addMessage(null,new jakarta.faces.application.FacesMessage("Saved"));}catch(RuntimeException ex){jakarta.faces.context.FacesContext.getCurrentInstance().addMessage(null,new jakarta.faces.application.FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_ERROR,"Invalid data",null));}return null;}
}