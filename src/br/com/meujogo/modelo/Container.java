package br.com.meujogo.modelo;

import javax.swing.*;

public class Container extends JFrame {

    private final Jogo fase;

    public Container() {
        fase = new Jogo();
        add(fase);
        setTitle("Meu Jogo");
        setSize(1280, 960);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Container::new);
    }
}
