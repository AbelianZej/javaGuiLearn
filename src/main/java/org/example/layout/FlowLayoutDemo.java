package org.example.layout;

import java.awt.*;

public class FlowLayoutDemo {
    public static void main(String[] args) {
        Frame frame = new Frame();

        // setLayout设置布局管理器方法
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        //添加按钮，这里使用for循环添加吧
        for (int i = 1; i <= 10; i++) {
            frame.add(new Button("按钮" + i));
        }
        //设置最佳大小
        frame.pack();
        frame.setVisible(true);
    }
}
