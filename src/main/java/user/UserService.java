package user;

import java.util.Optional;

public interface UserService {
    Optional<Usuario> validate(String usuario, String password);
}
