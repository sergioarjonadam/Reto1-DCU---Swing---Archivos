package ui;

import context.ContextService;
import data.DataService;
import data.Pelicula;
import user.Usuario;

import javax.swing.*;
import java.awt.event.*;

public class CreateForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textTitulo;
    private JComboBox comboBox1;
    private JSpinner spinnerAño;
    private JTextField textDirector;
    private JTextField textDescripcion;
    private JTextField textImagen;

    private DataService dataService;

    public CreateForm(DataService ds) {

        dataService = ds;

        setContentPane(contentPane);
        setModalityType(ModalityType.APPLICATION_MODAL);
        getRootPane().setDefaultButton(buttonOK);
        setResizable(false);

        //( (DefaultComboBoxModel) comboBox1.getModel()).addAll(...);

        spinnerAño.setModel(new SpinnerNumberModel(1990, 1900, 2025, 1));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
    }

    private void onOK() {

        Pelicula peli = new Pelicula();

        peli.setTitulo( textTitulo.getText());
        peli.setAño((Integer) spinnerAño.getValue());
        peli.setDirector( textDirector.getText());
        peli.setDescripcion( textDescripcion.getText());
        peli.setGenero( (String) comboBox1.getSelectedItem());
        peli.setImageUrl( textImagen.getText());

        // Asignar el usuarioId del usuario logueado
        ContextService.getInstance().getItem("usuarioActivo").ifPresent(user -> {
            Usuario usuario = (Usuario) user;
            peli.setUsuarioId(usuario.getUsuarioId());
        });

        if(dataService.save(peli).isEmpty()){
            JOptionPane.showMessageDialog(this, "Error al guardar","",JOptionPane.WARNING_MESSAGE);
        } else dispose();

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
