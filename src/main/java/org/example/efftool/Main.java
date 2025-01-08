package main.java.org.example.efftool;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("这是一个效率工具");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                 Main.createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI(){
        JFrame frame = new JFrame("这是正在建设的效率工具QaQ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,500);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("整理 URL", new UrlFormatterTab().getPanel());
        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
