import context.ContextService;
import data.CsvDataService;
import data.DataService;
import user.FileUserService;
import user.UserService;
import ui.Login;
import ui.Main;

import javax.swing.*;

public class App {
    public static void main(String[] args) {

        DataService ds = new CsvDataService("peliculas.csv");
        UserService us = new FileUserService("src/main/resources/usuarios.csv");

        JFrame tempFrame = new JFrame();
        tempFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Login loginDialog = new Login(tempFrame, us);
        loginDialog.setVisible(true);

        ContextService.getInstance().getItem("usuarioActivo").ifPresentOrElse(
            (usuario) -> {
                tempFrame.dispose();
                // Si el login fue exitoso, abrir la ventana principal
                (new Main(ds, us)).start();
            },
            () -> {
                // Si no se autenticó, cerrar la aplicación
                System.exit(0);
            }
        );
    }
}