package data;

import java.util.List;
import java.util.Optional;

public interface DataService{

    public List<Pelicula> findAll();
    public List<Pelicula> findByUsuarioId(String usuarioId);
    Optional<Pelicula> save(Pelicula pelicula);
    Optional<Pelicula> delete(Integer id);

}
