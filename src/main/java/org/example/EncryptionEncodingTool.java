package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionEncodingTool extends JFrame {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JComboBox<String> operationComboBox;
    private JComboBox<String> algorithmComboBox;
    private JTextField keyField;

    public EncryptionEncodingTool() {
        setTitle("加密编码转换工具");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(null); // 居中显示
    }

    private void initComponents() {
        // 输入面板
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("输入"));
        inputTextArea = new JTextArea();
        JScrollPane inputScroll = new JScrollPane(inputTextArea);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        // 输出面板
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("输出"));
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputTextArea);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        // 操作面板
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // 操作类型
        controlPanel.add(new JLabel("操作类型:"));
        operationComboBox = new JComboBox<>(new String[]{"编码", "解码", "加密", "解密"});
        controlPanel.add(operationComboBox);

        // 算法选择
        controlPanel.add(new JLabel("算法:"));
        algorithmComboBox = new JComboBox<>(new String[]{"Base64", "Hex", "AES"});
        controlPanel.add(algorithmComboBox);

        // 密钥（仅用于加密/解密）
        controlPanel.add(new JLabel("密钥:"));
        keyField = new JTextField();
        controlPanel.add(keyField);

        // 按钮
        JButton executeButton = new JButton("执行");
        executeButton.addActionListener(new ExecuteAction());
        controlPanel.add(executeButton);

        // 布局管理
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);
    }

    private class ExecuteAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String operation = (String) operationComboBox.getSelectedItem();
            String algorithm = (String) algorithmComboBox.getSelectedItem();
            String input = inputTextArea.getText();
            String key = keyField.getText();

            try {
                String result = "";
                switch (operation) {
                    case "编码":
                        result = encode(input, algorithm);
                        break;
                    case "解码":
                        result = decode(input, algorithm);
                        break;
                    case "加密":
                        if (algorithm.equals("AES")) {
                            if (key.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "请输入密钥进行AES加密！");
                                return;
                            }
                            result = encryptAES(input, key);
                        } else {
                            JOptionPane.showMessageDialog(null, "当前加密仅支持AES算法！");
                            return;
                        }
                        break;
                    case "解密":
                        if (algorithm.equals("AES")) {
                            if (key.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "请输入密钥进行AES解密！");
                                return;
                            }
                            result = decryptAES(input, key);
                        } else {
                            JOptionPane.showMessageDialog(null, "当前解密仅支持AES算法！");
                            return;
                        }
                        break;
                }
                outputTextArea.setText(result);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "操作失败: " + ex.getMessage());
            }
        }
    }

    // 编码方法
    private String encode(String data, String algorithm) {
        switch (algorithm) {
            case "Base64":
                return Base64.getEncoder().encodeToString(data.getBytes());
            case "Hex":
                return bytesToHex(data.getBytes());
            default:
                return "不支持的编码算法！";
        }
    }

    // 解码方法
    private String decode(String data, String algorithm) {
        switch (algorithm) {
            case "Base64":
                byte[] decodedBytes = Base64.getDecoder().decode(data);
                return new String(decodedBytes);
            case "Hex":
                byte[] bytes = hexToBytes(data);
                return new String(bytes);
            default:
                return "不支持的解码算法！";
        }
    }

    // AES加密
    private String encryptAES(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(fixKey(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // AES解密
    private String decryptAES(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(fixKey(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(data);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted);
    }

    // 辅助方法：将字节转换为Hex字符串
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b: bytes){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // 辅助方法：将Hex字符串转换为字节
    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len/2];
        for(int i=0;i<len;i+=2){
            data[i/2] = (byte) ((Character.digit(hex.charAt(i),16) << 4)
                    + Character.digit(hex.charAt(i+1),16));
        }
        return data;
    }

    // 辅助方法：修正密钥长度为16字节（128位）
    private byte[] fixKey(String key) {
        byte[] keyBytes = key.getBytes();
        byte[] fixedKey = new byte[16];
        for(int i=0;i<16;i++) {
            if(i < keyBytes.length){
                fixedKey[i] = keyBytes[i];
            } else {
                fixedKey[i] = 0;
            }
        }
        return fixedKey;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EncryptionEncodingTool().setVisible(true);
        });
    }
}
