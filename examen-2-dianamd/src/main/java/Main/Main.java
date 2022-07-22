package Main;

import Services.BootStrapServices;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        String mensaje = "Software ORM - JPA";
        System.out.println(mensaje);

        BootStrapServices.getInstancia().init();

        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/public";
                staticFileConfig.location = Location.CLASSPATH;
            });

        });
        app.start(7000);
        new Controller(app).applyRoutes();
    }
}
