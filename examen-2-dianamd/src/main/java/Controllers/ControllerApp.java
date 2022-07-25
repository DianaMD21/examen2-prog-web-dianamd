package Controllers;

import Entities.FormApp;
import Entities.UserApp;
import Enums.RolesApp;
import Services.FormService;
import Services.UserService;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControllerApp extends BaseController{
    UserService userService;
    FormService formService;
    public ControllerApp(Javalin app) {
        super(app);
        userService=UserService.UserService();
        formService=FormService.FormService();
        UserApp user=new UserApp("admin","admin");
        user.getRoles().add(RolesApp.ROLE_ADMIN);
        user.getRoles().add(RolesApp.ROLE_USUARIO);
        userService.createUser(user);
    }
    @Override
    public void applyRoutes() {
        app.routes(()->{
            app.get("",ctx->{
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("titulo","Inicio");
                modelo.put("user",ctx.sessionAttribute("user"));
                ctx.render("/public/index.html",modelo);
            });
            app.before("/login",ctx->{
                if(ctx.sessionAttribute("user")!=null){
                    ctx.redirect("/");
                }
            });
            app.get("/login",ctx->{
                ctx.render("/public/login.html");
            });
            app.post("/login",ctx->{
                Map<String, Object> modelo = new HashMap<>();
                String usuario = ctx.formParam("user");
                String contrasena = ctx.formParam("password");
                System.out.println("Usuario: -"+usuario);
                //String rememberMe= ctx.formParam("rememberMe");
                UserApp user =userService.getUserByUser(usuario);
                if(user==null){
                    System.out.println("Es null");
                    modelo.put("check",true);
                    ctx.render("/public/login.html",modelo);
                }else{
                    if(user.getPassword().equals(contrasena)){
                        if(userService.isAdmin(user)){
                            ctx.sessionAttribute("user","admin");
                        }else{
                            ctx.sessionAttribute("user","normal");
                        }
                        ctx.redirect("/");
                    }else{
                        System.out.println("La contrasena esta mal");
                        modelo.put("check",true);
                        ctx.render("/public/login.html",modelo);
                    }
                }
            });
            app.before("/fill-form",ctx->{
                if(ctx.sessionAttribute("user")==null){
                    System.out.println("El usuario no esta logeado");
                    ctx.redirect("/login");
                }
            });
            app.get("/fill-form",ctx->{
                ctx.render("/public/form_student.html");
            });
            app.post("/fill-form",ctx->{
                Map<String, Object> modelo = new HashMap<>();
                String name = ctx.formParam("name");
                String sector = ctx.formParam("sector");
                String level = ctx.formParam("schoollevel");
                String latitud = ctx.formParam("latitud");
                String longitud = ctx.formParam("longitud");
                FormApp form=new FormApp();
                form.setUserForm(ctx.sessionAttribute("user"));
                form.setName(name);
                form.setSector(sector);
                form.setEducationLevel(level);
                form.setLatitud(latitud);
                form.setLongitud(longitud);
                this.formService.createForm(form);
                System.out.println("Nombre: "+name+" sector: "+sector+" level: "+level+" latitud: "+latitud+" longitud: "+longitud);
            });
            app.get("/register",ctx->{
                Map<String, Object> modelo = new HashMap<>();
                ctx.render("/public/register.html",modelo);
            });
            app.post("/register",ctx->{
                Map<String, Object> modelo = new HashMap<>();
                String usuario = ctx.formParam("user");
                String contrasena = ctx.formParam("password");

                if(userService.getUserByUser(usuario)!=null){
                    modelo.put("check",true);
                    ctx.render("/public/register.html",modelo);
                }
                else{
                    UserApp user=new UserApp(usuario,contrasena);
                    user.getRoles().add(RolesApp.ROLE_USUARIO);
                    this.userService.createUser(user);
                    ctx.sessionAttribute("user",user);
                    ctx.redirect("/");
                }
            });
            app.get("/log-out",ctx->{
                ctx.sessionAttribute("user","");
                ctx.redirect("/");
            });
        });
    }

}
