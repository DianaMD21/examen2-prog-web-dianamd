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
                modelo.put("user",ctx.sessionAttribute("userType"));
                ctx.render("/public/index.html",modelo);
            });
            app.before("/login",ctx->{
                if(ctx.sessionAttribute("userType")=="admin" || ctx.sessionAttribute("userType")=="normal"){
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
                            ctx.sessionAttribute("userType","admin");
                        }else{
                            ctx.sessionAttribute("userType","normal");
                        }
                        ctx.sessionAttribute("user",user);
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
                ctx.redirect("/");

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
                ctx.sessionAttribute("user",null);
                ctx.sessionAttribute("userType","");
                ctx.redirect("/");
            });
            app.get("/historial-formularios",ctx->{
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("user",ctx.sessionAttribute("userType"));
                modelo.put("forms",formService.getAllForms());
                ctx.render("/public/show_forms.html",modelo);
            });
            app.get("/show-form/{id}",ctx->{
                long id=ctx.pathParamAsClass("id", Long.class).get();
                FormApp form= this.formService.getFormById(id);
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("form",form);
                ctx.render("/public/visualize_form.html",modelo);
            });
        });
    }

}
