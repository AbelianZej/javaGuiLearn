package org.example.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorFrame frame = new CalculatorFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class CalculatorFrame extends JFrame {
    private final JTextField displayField; // 显示屏
    private String currentInput = ""; // 当前输入的数字或符号
    private String previousInput = ""; // 上一个输入的数字
    private String operator = ""; // 当前运算符
    private boolean isResultDisplayed = false; // 是否已显示结果

    public CalculatorFrame() {
        setTitle("计算器");
        setSize(400, 500);
        setLayout(new BorderLayout(5, 5));
        setLocationRelativeTo(null); // 居中显示

        // 显示屏区域
        displayField = new JTextField("0");
        displayField.setFont(new Font("Arial", Font.BOLD, 32));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        add(displayField, BorderLayout.NORTH);

        // 按钮区域
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));

        // 数字按钮和操作符按钮
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", ".", "+"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        // 等号按钮
        JButton equalsButton = new JButton("=");
        equalsButton.setFont(new Font("Arial", Font.BOLD, 24));
        equalsButton.addActionListener(new EqualsButtonListener());
        add(buttonPanel, BorderLayout.CENTER);

        // 底部按钮区
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(equalsButton, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 按钮点击监听器
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if ("C".equals(command)) {
                // 清除功能
                currentInput = "";
                previousInput = "";
                operator = "";
                isResultDisplayed = false;
                displayField.setText("0");
            } else if (".".equals(command)) {
                // 小数点输入
                if (!currentInput.contains(".")) {
                    currentInput += ".";
                }
                displayField.setText(currentInput);
            } else if ("+".equals(command) || "-".equals(command) || "*".equals(command) || "/".equals(command)) {
                // 运算符输入
                if (!currentInput.isEmpty()) {
                    if (!operator.isEmpty()) {
                        calculateIntermediateResult();
                    } else {
                        previousInput = currentInput;
                    }
                    operator = command;
                    currentInput = "";
                }
            } else {
                // 数字输入
                if (isResultDisplayed) {
                    currentInput = command; // 覆盖结果开始新的输入
                    isResultDisplayed = false;
                } else {
                    currentInput += command;
                }
                displayField.setText(currentInput);
            }
        }

        private void calculateIntermediateResult() {
            if (!previousInput.isEmpty() && !currentInput.isEmpty() && !operator.isEmpty()) {
                double num1 = Double.parseDouble(previousInput);
                double num2 = Double.parseDouble(currentInput);
                double result = calculate(num1, num2, operator);
                previousInput = String.valueOf(result);
                displayField.setText(formatResult(result));
            }
        }
    }

    // 等号按钮监听器
    private class EqualsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!previousInput.isEmpty() && !currentInput.isEmpty() && !operator.isEmpty()) {
                double num1 = Double.parseDouble(previousInput);
                double num2 = Double.parseDouble(currentInput);
                double result = calculate(num1, num2, operator);
                displayField.setText(formatResult(result));
                currentInput = String.valueOf(result);
                previousInput = "";
                operator = "";
                isResultDisplayed = true;
            }
        }
    }

    // 计算核心逻辑
    private double calculate(double num1, double num2, String operator) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    JOptionPane.showMessageDialog(this, "错误: 除数不能为 0", "计算错误", JOptionPane.ERROR_MESSAGE);
                    return 0;
                }
            default:
                return 0;
        }
    }

    // 格式化结果（移除多余的小数点和零）
    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.format("%s", result);
        }
    }
}
