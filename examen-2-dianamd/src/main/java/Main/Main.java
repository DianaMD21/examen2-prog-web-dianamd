package Main;

import Controllers.ControllerApp;
import Entities.UserApp;
import Services.BootStrapServices;
import Services.UserService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        String mensaje = "Software ORM - JPA";
        System.out.println(mensaje);
        //EntityManagerFactory emf;
        //emf=Persistence.createEntityManagerFactory("MiUnidadPersistencia");

        BootStrapServices.getInstancia().init();
        UserService userService=UserService.UserService();
        //UserApp user=new UserApp("test","Test");
        //userService.createUser(user);
        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
            });

        });

        app.start(7000);
        new ControllerApp(app).applyRoutes();

    }
}
