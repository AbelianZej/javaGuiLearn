package org.example.layout;

import java.awt.*;

public class BorderLayoutDemo {
    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.setLayout(new BorderLayout(20, 20));

        frame.add(new Button("North"), BorderLayout.NORTH);
        frame.add(new Button("South"), BorderLayout.SOUTH);
        frame.add(new Button("East"), BorderLayout.EAST);
        frame.add(new Button("West"), BorderLayout.WEST);
        frame.add(new Button("Center"), BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}

