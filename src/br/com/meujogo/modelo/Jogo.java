package br.com.meujogo.modelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Jogo extends JPanel implements ActionListener, KeyListener {

    private Image fundo;
    private Image imagemPersonagem;
    private Personagem personagem;
    private List<Plataforma> plataformas;

    private boolean movendoParaDireita = false;
    private boolean movendoParaEsquerda = false;
    private int velocidadeMovimento = 10; // Velocidade de movimento do personagem

    private int pontuacao = 0; // Variável para armazenar a pontuação

    private Random random;

    // Estrutura interna para representar uma plataforma
    private static class Plataforma {
        int x;
        int y;
        int largura;
        int altura;
        long tempoCriacao; // Timestamp de quando a plataforma foi criada

        public Plataforma(int x, int y, int largura, int altura, long tempoCriacao) {
            this.x = x;
            this.y = y;
            this.largura = largura;
            this.altura = altura;
            this.tempoCriacao = tempoCriacao;
        }

        public void desenhar(Graphics2D g) {
            g.setColor(Color.GRAY); // Cor da plataforma
            g.fillRect(x, y, largura, altura); // Desenha um retângulo representando a plataforma
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, largura, altura);
        }

        public int getY() {
            return y;
        }

        public long getTempoCriacao() {
            return tempoCriacao;
        }
    }

    // Botão de reiniciar
    private JButton botaoReiniciar;
    private JLabel labelPontuacao; // Label para exibir a pontuação

    public Jogo() {
        ImageIcon referenciaFundo = new ImageIcon("res/background.jpg");
        fundo = referenciaFundo.getImage();

        ImageIcon referenciaPersonagem = new ImageIcon("res/personagem.png");
        imagemPersonagem = referenciaPersonagem.getImage();

        personagem = new Personagem(550, -400); // Posição inicial do personagem
        plataformas = new ArrayList<>();

        random = new Random();

        setFocusable(true);
        addKeyListener(this);

        // Configura o temporizador para atualizar o jogo a cada 20 milissegundos (equivalente a 50 FPS)
        Timer timer = new Timer(1, this);
        timer.start();

        // Configuração do botão de reiniciar
        botaoReiniciar = new JButton("Reiniciar");
        botaoReiniciar.setBounds(550, 400, 180, 40); // Posição e tamanho do botão
        botaoReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarJogo(); // Chama o método para reiniciar o jogo quando o botão for pressionado
            }
        });
        botaoReiniciar.setVisible(false); // Inicialmente, o botão está invisível
        add(botaoReiniciar); // Adiciona o botão ao painel

        // Label para exibir a pontuação
        labelPontuacao = new JLabel("Pontuação: 0");
        labelPontuacao.setBounds(40, 40, 120, 30); // Posição e tamanho da label
        add(labelPontuacao); // Adiciona a label ao painel

        // Inicia o processo de criação de plataformas aleatórias
        Timer plataformaTimer = new Timer(800, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gerarPlataforma();
            }
        });
        plataformaTimer.start();
    }

    // Método para gerar plataformas aleatórias
    private void gerarPlataforma() {
        int x = (int) (Math.random() * getWidth()); // Posição X aleatória dentro da largura da tela
        int y = (int) (Math.random() * getHeight()); // Posição Y aleatória dentro da altura da tela
        int largura = 120; // Largura da plataforma
        int altura = 20; // Altura da plataforma

        Plataforma plataforma = new Plataforma(x, y, largura, altura, System.currentTimeMillis());
        plataformas.add(plataforma);

        // Configura o temporizador para remover a plataforma após 2 segundos
        Timer removerPlataformaTimer = new Timer(2500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator<Plataforma> iterator = plataformas.iterator();
                while (iterator.hasNext()) {
                    Plataforma p = iterator.next();
                    if (p.getTempoCriacao() == plataforma.getTempoCriacao()) {
                        iterator.remove();
                        break;
                    }
                }
            }
        });
        removerPlataformaTimer.setRepeats(false); // Apenas executa uma vez
        removerPlataformaTimer.start();
    }

    // Método para reiniciar o jogo
    private void reiniciarJogo() {
        // Oculta o botão de reiniciar
        botaoReiniciar.setVisible(false);
        // Reinicia a posição do personagem
        personagem = new Personagem(550, -400); // Posição inicial do personagem
        // Reseta a pontuação
        pontuacao = 0;
        // Atualiza a label de pontuação
        atualizarLabelPontuacao();
    }

    // Método para verificar se o personagem caiu fora da tela
    private boolean personagemCaiuForaDaTela() {
        return personagem.getY() > getHeight();
    }

    // Método para atualizar a label de pontuação
    private void atualizarLabelPontuacao() {
        labelPontuacao.setText("Pontuação: " + pontuacao);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graficos = (Graphics2D) g;
        graficos.drawImage(fundo, 0, 0, null);

        // Desenha o personagem usando a imagem carregada
        graficos.drawImage(imagemPersonagem, personagem.getX(), personagem.getY(), null);

        // Desenha as plataformas
        for (Plataforma plataforma : plataformas) {
            plataforma.desenhar(graficos); // Desenha a plataforma
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Verifica se o personagem caiu fora da tela
        if (personagemCaiuForaDaTela()) {
            // Exibe o botão de reiniciar
            botaoReiniciar.setVisible(true);
            return;
        }

        // Lógica para movimentar o personagem e verificar colisões
        if (movendoParaDireita) {
            personagem.setX(personagem.getX() + velocidadeMovimento);
        } else if (movendoParaEsquerda) {
            personagem.setX(personagem.getX() - velocidadeMovimento);
        }

        // Verifica se o personagem colide com alguma plataforma
        boolean colidiu = false;
        for (Plataforma plataforma : plataformas) {
            if (personagem.getBounds().intersects(plataforma.getBounds())) {
                colidiu = true;
                personagem.setY(plataforma.getY() - personagem.getAltura()); // Coloca o personagem sobre a plataforma

                // Teleporta o personagem um pouco para cima
                personagem.setY(personagem.getY() - 300); // Ajuste conforme necessário

                // Atualiza a posição do personagem para a nova posição
                personagem.atualizar(true);

                // Incrementa a pontuação
                pontuacao++;

                // Atualiza a label de pontuação
                atualizarLabelPontuacao();

                // Sai do loop, pois só queremos tratar uma colisão por vez
                break;
            }
        }

        // Se não houve colisão, ou após o ajuste de posição, atualiza a posição normalmente
        if (!colidiu) {
            personagem.atualizar(false);
            personagem.atualizar(false);
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            movendoParaDireita = true;
        } else if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            movendoParaEsquerda = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            movendoParaDireita = false;
        } else if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            movendoParaEsquerda = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não é necessário implementar este método, mas mantive para manter consistência com a interface KeyListener
    }
}
