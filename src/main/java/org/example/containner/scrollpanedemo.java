package org.example.containner;

import java.awt.*;

public class scrollpanedemo {
    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.setBounds(300, 600, 800, 600);

        //创建一个scrollpane
        ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
        scrollPane.add(new Label("这是一个滚动面板"));
        scrollPane.add(new Button("这是一个按钮"));

        frame.add(scrollPane);

        //添加scrollpane到frame
        frame.setVisible(true);
    }
}
