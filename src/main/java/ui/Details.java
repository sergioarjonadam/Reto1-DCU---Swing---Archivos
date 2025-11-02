package ui;

import context.ContextService;
import data.Pelicula;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Details extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel labelId;
    private JLabel labelTitulo;
    private JLabel labelDirector;
    private JLabel labelAño;
    private JLabel LabelImagen;
    private JLabel labelTipo;
    private JLabel labelDescripcion;


    public Details(JFrame parent) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);


        // Juego juego = AppContext.juegoSeleccionado;
        Pelicula peli = (Pelicula) ContextService.getInstance().getItem("juegoSeleccionado").get();

        //nombre la ventana
        setTitle(peli.getTitulo());

        labelId.setText(peli.getId().toString());
        labelTitulo.setText(peli.getTitulo());
        labelDirector.setText(peli.getDirector());
        labelAño.setText(peli.getAño().toString());
        labelDescripcion.setText(peli.getDescripcion());
        labelTipo.setText(peli.getGenero());
        LabelImagen.setText(peli.getImageUrl());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
    }

    private void onOK() {
        // add your code here
        dispose();
    }

}
