package org.example.layout;

import java.awt.*;

public class GridLayoutDemo {
    public static void main(String[] args) {
        Frame frame = new Frame("计算器");
        Panel p1 = new Panel();
        p1.add(new TextField(45));
        frame.add(p1, BorderLayout.NORTH);
        Panel p2 = new Panel();
        p2.setLayout(new GridLayout(3, 5,3,3));

        for (int i = 0; i < 10; i++) {
            p2.add(new Button(String.valueOf(i)));
        }
        p2.add(new Button("+"));
        p2.add(new Button("-"));
        p2.add(new Button("*"));
        p2.add(new Button("/"));
        p2.add(new Button("."));
        frame.add(p2);
        frame.pack();
        frame.setVisible(true);

    }
}
