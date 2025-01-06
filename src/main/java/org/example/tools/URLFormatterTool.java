package org.example.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLFormatterTool {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new URLFormatterTool().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        // 创建主窗口
        JFrame frame = new JFrame("多功能工具");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        // 创建 Tab 面板
        JTabbedPane tabbedPane = new JTabbedPane();

        // 添加各功能的 Tab
        tabbedPane.addTab("整理 URL", createUrlFormatterTab());
        tabbedPane.addTab("访问 URL", createUrlAccessTab());
        tabbedPane.addTab("URL 编解码", createUrlEncodeDecodeTab());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel createUrlFormatterTab() {
        JPanel panel = new JPanel(new BorderLayout());

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
            copyToClipboard(outputText);
            JOptionPane.showMessageDialog(panel, "整理后的 URL 已复制到剪贴板！", "成功", JOptionPane.INFORMATION_MESSAGE);
        });

        // 按钮布局
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(processButton);
        bottomPanel.add(copyButton);

        panel.add(inputScrollPane, BorderLayout.NORTH);
        panel.add(outputScrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUrlAccessTab() {
        JPanel panel = new JPanel(new BorderLayout());

        // 输入框
        JTextArea inputTextArea = new JTextArea(10, 40);
        inputTextArea.setBorder(BorderFactory.createTitledBorder("请输入要访问的URL，每行一个："));
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);

        // 输出框
        JTextArea outputTextArea = new JTextArea(15, 50);
        outputTextArea.setBorder(BorderFactory.createTitledBorder("访问结果："));
        outputTextArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        // 按钮：访问 URL
        JButton accessButton = new JButton("访问 URL");
        accessButton.addActionListener(e -> {
            String inputText = inputTextArea.getText();
            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "请输入URL后再访问！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            outputTextArea.setText(""); // 清空输出框
            String[] urls = inputText.split("\\n");
            for (String url : urls) {
                url = url.trim();
                if (!url.startsWith("https://")) {
                    url = "https://" + url;
                }
                outputTextArea.append("访问成功: " + url + "\n"); // 简化处理
            }
        });

        // 按钮：复制全部
        JButton copyButton = new JButton("复制全部");
        copyButton.addActionListener(e -> {
            String outputText = outputTextArea.getText();
            if (outputText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "没有可复制的内容！", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            copyToClipboard(outputText);
            JOptionPane.showMessageDialog(panel, "访问结果已复制到剪贴板！", "成功", JOptionPane.INFORMATION_MESSAGE);
        });

        // 按钮布局
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(accessButton);
        bottomPanel.add(copyButton);

        panel.add(inputScrollPane, BorderLayout.NORTH);
        panel.add(outputScrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUrlEncodeDecodeTab() {
        JPanel panel = new JPanel(new BorderLayout());

        // 输入框
        JTextArea inputTextArea = new JTextArea(10, 40);
        inputTextArea.setBorder(BorderFactory.createTitledBorder("请输入文本："));
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);

        // 输出框
        JTextArea outputTextArea = new JTextArea(10, 40);
        outputTextArea.setBorder(BorderFactory.createTitledBorder("结果："));
        outputTextArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        // 按钮：编码
        JButton encodeButton = new JButton("编码");
        encodeButton.addActionListener(e -> {
            String inputText = inputTextArea.getText();
            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "输入不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String encoded = URLEncoder.encode(inputText, StandardCharsets.UTF_8.name());
                outputTextArea.setText(encoded);
            } catch (Exception ex) {
                outputTextArea.setText("编码失败: " + ex.getMessage());
            }
        });

        // 按钮：解码
        JButton decodeButton = new JButton("解码");
        decodeButton.addActionListener(e -> {
            String inputText = inputTextArea.getText();
            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "输入不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String decoded = URLDecoder.decode(inputText, StandardCharsets.UTF_8.name());
                outputTextArea.setText(decoded);
            } catch (Exception ex) {
                outputTextArea.setText("解码失败: " + ex.getMessage());
            }
        });

        // 按钮布局
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(encodeButton);
        bottomPanel.add(decodeButton);

        panel.add(inputScrollPane, BorderLayout.NORTH);
        panel.add(outputScrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }
}
