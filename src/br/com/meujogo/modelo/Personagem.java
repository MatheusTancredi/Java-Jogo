package br.com.meujogo.modelo;
import java.awt.*;

public class Personagem {
    private int x; // Posição x do personagem
    private int y; // Posição y do personagem
    private static final int LARGURA_PERSONAGEM = 70; // Largura do personagem
    private static final int ALTURA_PERSONAGEM = 100; // Altura do personagem
    private static final int VELOCIDADE_QUEDA = 2; // Velocidade de queda do personagem
    private boolean naPlataforma; // Indica se o personagem está atualmente em uma plataforma

    public Personagem(int x, int y) {
        this.x = x;
        this.y = y;
        this.naPlataforma = false; // Inicialmente, o personagem não está em uma plataforma
    }

    public void desenhar(Graphics2D graficos) {
        // Desenha o personagem como um retângulo azul
        graficos.setColor(Color.BLUE);
        graficos.fillRect(x, y, LARGURA_PERSONAGEM, ALTURA_PERSONAGEM);
    }

    public void atualizar(boolean colidiuPlataforma) {
        if (!naPlataforma) {
            y += VELOCIDADE_QUEDA;
        } else if (colidiuPlataforma) {
            // Teleporta o personagem um pouco para cima
            y -= 10; // Ajuste conforme necessário

            // Atualiza o estado da plataforma para false
            naPlataforma = false;
        }
    }


    // Métodos getters e setters para acessar e modificar os atributos privados, se necessário
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLargura() {
        return LARGURA_PERSONAGEM;
    }

    public int getAltura() {
        return ALTURA_PERSONAGEM;
    }

    public boolean isNaPlataforma() {
        return naPlataforma;
    }

    public void setNaPlataforma(boolean naPlataforma) {
        this.naPlataforma = naPlataforma;
    }

    public Rectangle getBounds() {
        int larguraAjustada = LARGURA_PERSONAGEM;
        int alturaAjustada = ALTURA_PERSONAGEM;

        // Se o personagem estiver em uma plataforma, ajusta a posição Y do retângulo de colisão
        if (naPlataforma) {
            alturaAjustada += 15; // Ajusta a altura do retângulo para incluir uma área abaixo do personagem
        }

        return new Rectangle(x, y - (alturaAjustada - ALTURA_PERSONAGEM), larguraAjustada, alturaAjustada);
    }


}
