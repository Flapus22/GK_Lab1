package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.*;

public class Transforms2D extends JPanel {
    private class Display extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.translate(300, 300);  // Moves (0,0) to the center of the display.
            int whichTransform = transformSelect.getSelectedIndex();

            //////////////////////------------------------ Właściwy kod---------------------------
            g2.setRenderingHints(new RenderingHints(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON));// ustawienia Renderowania obrazka
            g2.setStroke(new BasicStroke(2)); //Grubość obrysu wielokąta


            var n = 14;// liczba wierzchołków w wielokącie
            int[] X = new int[n]; //Współrzędne wierzchołków na osi X
            int[] Y = new int[n]; //Współrzędne wierzchołków na osi Y


            for (int i = 0; i < n; i++) {
                X[i] = (int) (Math.cos(((Math.PI / 2) + (2 * Math.PI * i)) / n) * 30);
                Y[i] = (int) (Math.sin(((Math.PI / 2) + (2 * Math.PI * i)) / n) * 30);
            }//Stworzenie Wielokąta

            Polygon polygon = new Polygon(X, Y, n);// normalny wielokąt
            Polygon polygon2 = new Polygon(X, Y, n);// pochylony wielokąt

            // Pochylenie bez wykorzystania Metody
            var angle = 45;
            for (int i = 0; i < n; i++) {
                if (angle > 0) {
                    polygon2.xpoints[i] += (polygon2.ypoints[i] / Math.PI) * Math.toRadians(angle);
                    polygon2.ypoints[i] -= (polygon2.ypoints[i] / Math.PI) * Math.toRadians(angle);
                }
                if (angle < 0) {
                    polygon2.xpoints[i] += (polygon2.ypoints[i] / Math.PI) * Math.toRadians(angle);
                    polygon2.ypoints[i] += (polygon2.ypoints[i] / Math.PI) * Math.toRadians(angle);
                }

            }//Pochylenie wielokąta o dany Kąt


            switch (whichTransform) {
                case 1:
                    g2.scale(0.3, 0.3);//Zeskalowanie obrazu
                    break;
                case 2:
                    g2.rotate(0.785398);// Obrót obrazu. wartość wpisywana w radianach
                    // można wpisać od razu wartość lub wykorzystać metode do tego
                    break;
                case 3:
                    g2.scale(0.5, -1);
                    break;
                case 4:
                    g2.shear(0.5, 0);// Pochylenie obrazka za pomocą metody
                    break;
                case 5:
//                    for (int i = 0; i < sides; i++) {
//                        polygon.ypoints[i] -= 850 ;
//                    } //inny sposób na przesunięcie obiektu
                    g2.translate(0, -250);// przesunięcie wielokąta
                    g2.scale(1, 0.3);
                    break;
                case 6:
                    g2.rotate(Math.toRadians(90));
                    g2.shear(0.5, 0);
                    break;
                case 7:
                    g2.rotate(Math.toRadians(180));
                    g2.scale(0.5, 1);
                    break;
                case 8:
                    g2.translate(0, 200);
                    g2.rotate(Math.toRadians(30));
                    g2.scale(1, 0.3);

                    break;
                case 9:
                    g2.translate(120, 0);
                    g2.rotate(Math.toRadians(207));
                    g2.shear(0.5, 0);
                    break;
                default:

                    break;
            }// W zależności od wybranego indeksu będą różne modyfikacje wielokąta

            g2.setPaint(new GradientPaint(new Point(polygon.xpoints[0], polygon.ypoints[0]), new Color(180, 66, 14),
                    new Point(polygon.xpoints[n / 2], polygon.ypoints[n / 2]), new Color(90, 206, 56)));
            //zmiana koloru dla wielokąta

            g2.fillPolygon(polygon);// narysowanie pełnego wielokąta
            g2.setPaint(Color.black);// Zmiana koloru obrysu wielokąta
            g2.drawPolygon(polygon);// Narysowanie obrysu wielokąta


            // TODO Apply transforms here, depending on the value of whichTransform!
            //g2.drawImage(pic, -200, -150, null); // Draw image with center at (0,0).
        }
    }

    private Display display;
    private BufferedImage pic;
    private JComboBox<String> transformSelect;

    public Transforms2D() throws IOException {
        //pic = ImageIO.read(getClass().getResourceAsStream("shuttle.jpg"));
        pic = ImageIO.read(new FileInputStream("shuttle.jpg")); //Zmianna pobrania pliku z powodu błędu tego wyżej
        display = new Display();
        display.setBackground(Color.YELLOW);
        display.setPreferredSize(new Dimension(600, 600));
        transformSelect = new JComboBox<String>();
        transformSelect.addItem("None");
        for (int i = 1; i < 10; i++) {
            transformSelect.addItem("No. " + i);
        }
        transformSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                display.repaint();
            }
        });
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.GRAY);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 10));
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.CENTER));
        top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        top.add(new JLabel("Transform: "));
        top.add(transformSelect);
        add(display, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
    }


    public static void main(String[] args) throws IOException {
        JFrame window = new JFrame("2D Transforms");
        window.setContentPane(new Transforms2D());
        window.pack();
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((screen.width - window.getWidth()) / 2, (screen.height - window.getHeight()) / 2);
        window.setVisible(true);
    }
}
