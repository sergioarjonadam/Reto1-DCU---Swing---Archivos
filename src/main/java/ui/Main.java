package ui;

import context.ContextService;
import data.DataService;
import data.Pelicula;
import user.UserService;
import user.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Main extends JFrame {
    private JTable table1;
    private JPanel panel1;

    private DataService dataservice;
    private UserService userservice;

    private ArrayList<Pelicula> peliculas = new ArrayList<>();

    /* Es necesario que este accesible para poder modificarlo */
    private JMenuItem menuItemAñadir;

    public Main(DataService ds, UserService us) {
        dataservice = ds;
        userservice = us;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Filmteca");
        this.setResizable(false);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel1);

        /* Añadir menu de forma programática */
        JMenuBar menuBar = PrepareMenuBar();
        panel1.add(menuBar, BorderLayout.NORTH);


        /* Configuración y carga de la tabla */
        var modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Título");
        modelo.addColumn("Año");
        modelo.addColumn("Director");
        modelo.addColumn("Descripción");
        modelo.addColumn("Género");
        modelo.addColumn("Imagen");

        table1.setModel(modelo);

        loadDataTable();

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table1.getSelectionModel().addListSelectionListener( (e)->{
                if(!e.getValueIsAdjusting() && table1.getSelectedRow()>=0 ){
                    System.out.println("seleccionado: "+table1.getSelectedRow());
                    Pelicula pelicula = peliculas.get(table1.getSelectedRow());

                    // AppContext.juegoSeleccionado = juego;
                    ContextService.getInstance().addItem("juegoSeleccionado", pelicula);

                    (new Details(this)).setVisible(true);
                }
            }
        );

    }

    private void loadDataTable() {
        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
        modelo.setRowCount(0);

        // Obtener el usuario activo y cargar solo sus películas
        ContextService.getInstance().getItem("usuarioActivo").ifPresent(user -> {
            Usuario usuario = (Usuario) user;
            peliculas = (ArrayList<Pelicula>) dataservice.findByUsuarioId(usuario.getUsuarioId());
            peliculas.forEach( (j)->{
                var fila =  new Object[]{j.getId() , j.getTitulo(), j.getAño(),j.getDirector(), j.getDescripcion(), j.getGenero(), j.getImageUrl() };
                modelo.addRow(fila);
            });
        });
    }

    private JMenuBar PrepareMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu jMenuInicio = new JMenu("Inicio");
        JMenuItem menuEliminar = new JMenuItem("Eliminar");
        menuItemAñadir = new JMenuItem("Añadir");
        JMenuItem menuItemSalir = new JMenuItem("Salir");

        menuBar.add(jMenuInicio);
        jMenuInicio.add(menuItemAñadir);
        jMenuInicio.add(menuEliminar);
        jMenuInicio.addSeparator();
        jMenuInicio.add(menuItemSalir);

        /* Eventos del menú */

        menuItemSalir.addActionListener(e -> { System.exit(0); });

        menuItemAñadir.addActionListener(e -> {
            (new CreateForm(dataservice)).setVisible(true);
            loadDataTable();
        });

        menuEliminar.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow >= 0) {
                Pelicula pelicula = peliculas.get(selectedRow);
                int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar la película: " + pelicula.getTitulo() + "?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (dataservice.delete(pelicula.getId()).isPresent()) {
                        JOptionPane.showMessageDialog(this, "Película eliminada correctamente.");
                        loadDataTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al eliminar la película.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona una película para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });


        return menuBar;
    }

    public void start(){
        this.setVisible(true);
    }
}
