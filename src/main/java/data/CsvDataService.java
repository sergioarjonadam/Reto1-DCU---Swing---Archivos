package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class CsvDataService implements DataService {

    private String archivo;

    private static Integer lastId = -1;

    private static Logger logger = Logger.getLogger(CsvDataService.class.getName());

    public CsvDataService(String csvFile) {
        archivo = csvFile;
    }

    @Override
    public List<Pelicula> findAll() {
        var salida = new ArrayList<Pelicula>();

        logger.info("Abriendo archivo");
        try( BufferedReader br = new BufferedReader(new FileReader( new File(archivo) ))){
            var contenido = br.lines();

            contenido.forEach(line -> {
                String[] lineArray = line.split(",");
                if(lineArray.length < 8) {
                    logger.severe("Linea mal formada");
                } else {
                    Pelicula pelicula = new Pelicula();
                    pelicula.setId(Integer.parseInt(lineArray[0]));
                    pelicula.setTitulo(lineArray[1]);
                    pelicula.setAño(Integer.parseInt(lineArray[2]));
                    pelicula.setDirector(lineArray[3]);
                    pelicula.setDescripcion(lineArray[4]);
                    pelicula.setGenero(lineArray[5]);
                    pelicula.setImageUrl(lineArray[6]);
                    pelicula.setUsuarioId(lineArray[7]);
                    salida.add(pelicula);
                }
            });
            lastId = salida.stream().mapToInt(Pelicula::getId).max().orElse(0);
            logger.info("Actualizo tamaño: " + lastId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }

    @Override
    public List<Pelicula> findByUsuarioId(String usuarioId) {
        return findAll().stream()
                .filter(p -> p.getUsuarioId().equals(usuarioId))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<Pelicula> save(Pelicula pelicula) {

        logger.info("Abriendo el archivo para escribir");
        try(var bfw = new BufferedWriter(new FileWriter(new File(archivo),true))){

            // actualizo el nuevo id
            lastId++;
            pelicula.setId( lastId);
            logger.info("Actualizando id: "+lastId);

            String salida = new StringBuilder()
                    .append(pelicula.getId()).append(",")
                    .append(pelicula.getTitulo()).append(",")
                    .append(pelicula.getAño()).append(",")
                    .append(pelicula.getDirector()).append(",")
                    .append(pelicula.getDescripcion()).append(",")
                    .append(pelicula.getGenero()).append(",")
                    .append(pelicula.getImageUrl()).append(",")
                    .append(pelicula.getUsuarioId()).toString();

            logger.info("Nueva pelicula añadida");
            bfw.write( salida );
            bfw.newLine();
            logger.info("Pelicula guardada en el archivo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(pelicula);
    }

    @Override
    public Optional<Pelicula> delete(Integer id) {
        List<Pelicula> peliculas = findAll();
        Optional<Pelicula> peliculaToDelete = peliculas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (peliculaToDelete.isPresent()) {
            peliculas.removeIf(p -> p.getId().equals(id));

            // Reescribir el archivo sin la película eliminada
            try (BufferedWriter bfw = new BufferedWriter(new FileWriter(new File(archivo)))) {
                for (Pelicula p : peliculas) {
                    String linea = new StringBuilder()
                            .append(p.getId()).append(",")
                            .append(p.getTitulo()).append(",")
                            .append(p.getAño()).append(",")
                            .append(p.getDirector()).append(",")
                            .append(p.getDescripcion()).append(",")
                            .append(p.getGenero()).append(",")
                            .append(p.getImageUrl()).append(",")
                            .append(p.getUsuarioId()).toString();
                    bfw.write(linea);
                    bfw.newLine();
                }
                logger.info("Película eliminada correctamente");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return peliculaToDelete;
    }
}
