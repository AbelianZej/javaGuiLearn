package org.example.layout;
import javax.swing.*;
import java.awt.*;
import java.util.Base64;

public class Base64EncoderDecoder {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Base64 编码与解码工具");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new BorderLayout(10, 10));
            frame.setLocationRelativeTo(null);

            // 输入区域
            JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
            inputPanel.setBorder(BorderFactory.createTitledBorder("输入文本"));
            JTextField inputField = new JTextField();
            inputPanel.add(inputField, BorderLayout.CENTER);
            frame.add(inputPanel, BorderLayout.NORTH);

            // 按钮区域
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton encodeButton = new JButton("Base64编码");
            JButton decodeButton = new JButton("Base64解码");
            buttonPanel.add(encodeButton);
            buttonPanel.add(decodeButton);
            frame.add(buttonPanel, BorderLayout.CENTER);

            // 输出区域
            JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
            outputPanel.setBorder(BorderFactory.createTitledBorder("结果"));
            JTextArea outputArea = new JTextArea();
            outputArea.setEditable(false);
            outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
            frame.add(outputPanel, BorderLayout.SOUTH);

            // 按钮功能实现
            encodeButton.addActionListener(e -> {
                String inputText = inputField.getText();
                if (inputText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "请输入要编码的文本！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    String encodedText = Base64.getEncoder().encodeToString(inputText.getBytes());
                    outputArea.setText(encodedText);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "编码失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            decodeButton.addActionListener(e -> {
                String inputText = inputField.getText();
                if (inputText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "请输入要解码的文本！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(inputText);
                    String decodedText = new String(decodedBytes);
                    outputArea.setText(decodedText);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "解码失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            });

            frame.setVisible(true);
        });
    }
}
