package org.example.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

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

        // 添加 "整理 URL" 的 Tab
        tabbedPane.addTab("整理 URL", createUrlFormatterTab());

        // 添加 "访问 URL" 的 Tab
        tabbedPane.addTab("访问 URL", createUrlAccessTab());

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel createUrlFormatterTab() {
        // 创建一个 Panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 输入框 (多行文本)
        JTextArea inputTextArea = new JTextArea(10, 40);
        inputTextArea.setBorder(BorderFactory.createTitledBorder("请输入URL，每行一个："));
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);

        // 输出框 (多行文本，只读)
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

            // 处理输入的 URL 数据
            String[] urls = inputText.split("\\n");
            StringBuilder outputBuilder = new StringBuilder();
            for (String url : urls) {
                url = url.trim(); // 去除多余的空格
                if (!url.startsWith("https://")) {
                    outputBuilder.append("https://").append(url).append("\n");
                } else {
                    outputBuilder.append(url).append("\n");
                }
            }

            // 将结果显示在输出框
            outputTextArea.setText(outputBuilder.toString());
        });

        // 按钮区域布局
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(processButton);

        // 布局
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(inputScrollPane);
        centerPanel.add(outputScrollPane);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createUrlAccessTab() {
        // 创建一个 Panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 输入框 (多行文本)
        JTextArea inputTextArea = new JTextArea(10, 40);
        inputTextArea.setBorder(BorderFactory.createTitledBorder("请输入要访问的URL，每行一个："));
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);

        // 输出框 (多行文本，只读)
        JTextArea outputTextArea = new JTextArea(15, 50);
        outputTextArea.setBorder(BorderFactory.createTitledBorder("访问结果："));
        outputTextArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);

        // 按钮：访问 URL
        JButton accessButton = new JButton("访问 URL");
        accessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = inputTextArea.getText();
                if (inputText.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入URL后再访问！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 获取输入的 URL 数据
                String[] urls = inputText.split("\\n");
                outputTextArea.setText(""); // 清空输出框

                // 访问每个 URL 并获取结果
                for (String url : urls) {
                    url = url.trim(); // 去除多余的空格
                    if (!url.startsWith("https://")) {
                        url = "https://" + url; // 补全 https
                    }

                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000); // 超时时间 5 秒
                        connection.connect();

                        int statusCode = connection.getResponseCode();
                        int contentLength = getContentLength(connection);

                        // 输出结果
                        outputTextArea.append("URL: " + url + "\n");
                        outputTextArea.append("状态码: " + statusCode + ", 内容长度: " + contentLength + " 字节\n\n");
                    } catch (SocketTimeoutException timeoutException) {
                        outputTextArea.append("URL: " + url + "\n");
                        outputTextArea.append("访问超时 (5秒)\n\n");
                    } catch (Exception ex) {
                        outputTextArea.append("URL: " + url + "\n");
                        outputTextArea.append("访问失败: " + ex.getMessage() + "\n\n");
                    }
                }
            }
        });

        // 按钮区域布局
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(accessButton);

        // 布局
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(inputScrollPane);
        centerPanel.add(outputScrollPane);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // 获取内容长度 (如果响应有内容)
    private int getContentLength(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.length();
        } catch (Exception ex) {
            return 0;
        }
    }
}
