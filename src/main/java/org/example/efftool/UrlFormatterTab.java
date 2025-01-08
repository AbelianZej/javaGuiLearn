package main.java.org.example.efftool;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class UrlFormatterTab {
    private static JPanel panel;

    public UrlFormatterTab() {
        panel = new JPanel(new BorderLayout());

        // 输入框
        JTextArea inputTextArea = new JTextArea(10, 40);
        inputTextArea.setBorder(BorderFactory.createTitledBorder("请输入URL，每行一个："));
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);

        // 输出框
        JTextArea outputTextArea = new JTextArea(10, 40);
        outputTextArea.setBorder(BorderFactory.createTitledBorder("整理后的URL："));
        outputTextArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        // 按钮：整理 URL
        JButton processButton = new JButton("整理 URL");
        processButton.addActionListener(e -> {
            String inputText = inputTextArea.getText();
            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "输入不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder outputBuilder = new StringBuilder();
            String[] urls = inputText.split("\\n");
            for (String url : urls) {
                url = url.trim();
                if (!url.startsWith("https://")) {
                    outputBuilder.append("https://").append(url).append("\n");
                } else {
                    outputBuilder.append(url).append("\n");
                }
            }
            outputTextArea.setText(outputBuilder.toString());
        });

        // 按钮：复制全部
        JButton copyButton = new JButton("复制全部");
        copyButton.addActionListener(e -> {
            String outputText = outputTextArea.getText();
            if (outputText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "没有可复制的内容！", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // 使用 Java 内置类实现复制到剪贴板
            StringSelection stringSelection = new StringSelection(outputText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            JOptionPane.showMessageDialog(panel, "整理后的 URL 已复制到剪贴板！", "成功", JOptionPane.INFORMATION_MESSAGE);
        });

        // 按钮布局
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(processButton);
        bottomPanel.add(copyButton);

        panel.add(inputScrollPane, BorderLayout.NORTH);
        panel.add(outputScrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return panel;
    }
}
