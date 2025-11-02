package user;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class FileUserService implements UserService {

    private String archivo;

    public FileUserService(String userFile) {
        archivo = userFile;
    }

    @Override
    public Optional<Usuario> validate(String usuario, String password) {

        try (var br = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0].trim();
                String mail = parts[1].trim();
                String pass = parts[2].trim();
                if (mail.equals(usuario) && pass.equals(password)) {
                    Usuario user = new Usuario();
                    user.setUsuarioId(id);
                    user.setUsuario(mail);
                    user.setPassword(pass);
                    return Optional.of(user);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
}
}
