package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {

    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image drop;
    private static float drop_left = 200; //где капля в начале
    private static float drop_top = -100;  //где капля в начале, вылетает из-за границы экрана
    private static float drop_v = 200;  //скорость капли
    private static int score;



    public static void main(String[] args) throws IOException {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("/background.png")); //файлы должны лежать в папке resources
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("/game_over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("/drop.png"));
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  //по крекстику завершаем программу
        game_window.setLocation(200, 100); //координаты появления окна, верхний левый угол
        game_window.setSize(906, 478);  //размер окна
        game_window.setResizable(false);  //запрещаем изменять размер окна мышкой
        last_frame_time = System.nanoTime();  //возвращает текущее время в наносекундах
        GameField game_field = new GameField();  //отрисовываем игровое поле
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { //метод вызывает только при клике мышкой
                int x = e.getX(); //получаем координаты клика
                int y = e.getY();  //получаем координаты клика
                float drop_right = drop_left + drop.getWidth(null); //узнаем границы капли, чтобы понять, попадает ли клик в нее
                float drop_bottom = drop_top + drop.getHeight(null); //узнаем границы капли, чтобы понять, попадает ли клик в нее
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom; //расчитываем попадает клик в границы капли или нет
                if(is_drop) {
                    drop_top = -100; //отбросили каплю по вертикали за границу окна
                    drop_left = (int) (Math.random() * (game_field.getWidth() - drop.getWidth(null))); //отбросили рандомно по горизонтали в пределах поля
                    drop_v = drop_v + 20; //увеличиваем скорость капли после каждого клика
                    score++; //каждый клик мыши прибавляем к значению единицу
                    game_window.setTitle("Score: " + score);
                }
            }
        });
        game_window.add(game_field);
        game_window.setVisible(true);  //делаем окно видимым, по дефолту оно невидимо
    }

    private static void onRepaint (Graphics g) {
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;

        drop_top = drop_top + drop_v * delta_time; //меняем координаты капли по времени
        // drop_left = drop_left + drop_v * delta_time; //меняем координаты капли по времени, чтобы она двигалась по диагонали
        g.drawImage(background, 0,0, null);
        g.drawImage(drop, (int) drop_left, (int) drop_top, null);
        if (drop_top > game_window.getHeight()) g.drawImage(game_over, 280, 120, null); //выводим надпись гейм овер, если капля вылетает за границу окна
    }

    private static class GameField extends JPanel { //это класс

        @Override //переопределение класса, заставляем его делать то, что нам надо
        protected void paintComponent (Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}

