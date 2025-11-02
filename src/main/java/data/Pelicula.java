package data;

import lombok.Data;

@Data
public class Pelicula {
    private Integer id;
    private String titulo;
    private Integer año;
    private String director;
    private String descripcion;
    private String genero;
    private String imageUrl;
    private String usuarioId;
}
