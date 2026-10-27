package io.github.hoangtnedu.java;
import org.junit.jupiter.api.*;import static org.junit.jupiter.api.Assertions.*;import org.apache.catalina.startup.Tomcat;import java.nio.file.*;import java.net.*;import java.net.http.*;import java.util.*;import org.jsoup.Jsoup;
class HttpIT {
 static Tomcat server;static String base;static HttpClient client;static String csrf;
 @BeforeAll static void start()throws Exception{server=new Tomcat();Path baseDir=Files.createTempDirectory("tomcat-lab");Files.createDirectories(baseDir.resolve("webapps"));server.setBaseDir(baseDir.toString());server.setPort(0);server.getConnector();var context=server.addWebapp("",Path.of("target/lab10.war").toAbsolutePath().toString());context.setParentClassLoader(HttpIT.class.getClassLoader());org.apache.catalina.loader.WebappLoader loader=new org.apache.catalina.loader.WebappLoader();loader.setDelegate(true);context.setLoader(loader);server.start();assertTrue(context.getState().isAvailable());base="http://127.0.0.1:"+server.getConnector().getLocalPort();client=HttpClient.newBuilder().cookieHandler(new CookieManager(null,CookiePolicy.ACCEPT_ALL)).build();}
 @AfterAll static void stop()throws Exception{if(server!=null){server.stop();server.destroy();}}
 static HttpResponse<String> get(String path)throws Exception{return client.send(HttpRequest.newBuilder(URI.create(base+path)).timeout(java.time.Duration.ofSeconds(15)).GET().build(),HttpResponse.BodyHandlers.ofString());}
 static String form(Map<String,String> data){return data.entrySet().stream().map(x->URLEncoder.encode(x.getKey(),java.nio.charset.StandardCharsets.UTF_8)+"="+URLEncoder.encode(x.getValue(),java.nio.charset.StandardCharsets.UTF_8)).collect(java.util.stream.Collectors.joining("&"));}
 static HttpResponse<String> post(String path,Map<String,String> data)throws Exception{return client.send(HttpRequest.newBuilder(URI.create(base+path)).timeout(java.time.Duration.ofSeconds(15)).header("Content-Type","application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(form(data))).build(),HttpResponse.BodyHandlers.ofString());}
 static void login(String user)throws Exception{var page=get("/auth");assertEquals(200,page.statusCode());csrf=Jsoup.parse(page.body()).selectFirst("input[name=csrf]").val();assertEquals(302,post("/auth",Map.of("csrf",csrf,"username",user,"password",System.getenv().getOrDefault("LAB_DEMO_PASSWORD","change-me-local"))).statusCode());}
 @Test void servletSessionCrudAndCsrf()throws Exception{
 assertEquals(302,get("/students").statusCode());assertTrue(get("/hello").body().contains("Hello, Servlet"));login("admin");assertEquals(200,get("/students").statusCode());String code="SV"+UUID.randomUUID().toString().substring(0,8);
 assertEquals(403,post("/students",Map.of("code",code)).statusCode());
 assertEquals(302,post("/students",Map.of("csrf",csrf,"code",code,"name","<script>alert(1)</script>","email","demo@example.com","className","Demo")).statusCode());
 String html=get("/students").body();assertTrue(html.contains(code));assertTrue(html.contains("&lt;script&gt;"));
 assertEquals(400,post("/students",Map.of("csrf",csrf,"code",code,"name","Other","email","demo@example.com")).statusCode());
 assertEquals(400,post("/students",Map.of("csrf",csrf,"code","","name","","email","bad")).statusCode());
 var row=Jsoup.parse(html).select("tr").stream().filter(e->e.text().contains(code)).findFirst().orElseThrow();String id=row.selectFirst("input[name=id]").val();
 assertEquals(302,post("/students",Map.of("csrf",csrf,"id",id,"code",code,"name","Updated","email","new@example.com")).statusCode());
 assertTrue(get("/students?q=Updated").body().contains(code));

 assertEquals(302,post("/students",Map.of("csrf",csrf,"action","delete","id",id)).statusCode());
 assertFalse(get("/students").body().contains(code));
 assertEquals(302,post("/auth",Map.of("csrf",csrf,"action","logout")).statusCode());assertEquals(302,get("/students").statusCode());
 login("user");assertEquals(403,get("/admin/panel").statusCode());assertEquals(403,get("/staff/panel").statusCode());assertEquals(403,post("/students",Map.of("csrf",csrf,"code","NO","name","No","email","n@example.com")).statusCode());assertEquals(200,get("/user/profile").statusCode());
 }
}