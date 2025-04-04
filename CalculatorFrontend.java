import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class CalculatorFrontend extends JFrame implements ActionListener, KeyListener {
    // Components
    private JTextField inputDisplay; // Display for the input expression
    private JTextField outputDisplay; // Display for the result
    private JTextArea historyArea;
    private JButton[] numberButtons = new JButton[10];
    private JButton[] functionButtons = new JButton[12];
    private JButton addButton, subButton, mulButton, divButton;
    private JButton decButton, equButton, delButton, clrButton, sqrtButton, powButton, clrHistoryButton;
    private JPanel panel;

    // Backend and Database instances
    private CalculatorBackend backend;
    private DatabaseConnector database;

    public CalculatorFrontend() {
        // Initialize backend and database
        backend = new CalculatorBackend();
        database = new DatabaseConnector();

        // Frame setup
        setTitle("Maths Calculator");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(45, 45, 45)); // Dark background

        // Input display field
        inputDisplay = new JTextField();
        inputDisplay.setBounds(50, 25, 400, 40);
        inputDisplay.setEditable(false);
        inputDisplay.setFont(new Font("Arial", Font.PLAIN, 18));
        inputDisplay.setBackground(new Color(60, 60, 60));
        inputDisplay.setForeground(Color.WHITE);
        inputDisplay.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inputDisplay.addKeyListener(this); // Enable keyboard input
        add(inputDisplay);

        // Output display field
        outputDisplay = new JTextField();
        outputDisplay.setBounds(50, 75, 400, 40);
        outputDisplay.setEditable(false);
        outputDisplay.setFont(new Font("Arial", Font.PLAIN, 24));
        outputDisplay.setBackground(new Color(60, 60, 60));
        outputDisplay.setForeground(Color.WHITE);
        outputDisplay.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(outputDisplay);

        // History area
        historyArea = new JTextArea();
        historyArea.setBounds(50, 500, 400, 100);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 16));
        historyArea.setBackground(new Color(60, 60, 60));
        historyArea.setForeground(Color.WHITE);
        historyArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(historyArea);

        // Buttons
        addButton = createButton("+", new Color(70, 70, 70));
        subButton = createButton("-", new Color(70, 70, 70));
        mulButton = createButton("*", new Color(70, 70, 70));
        divButton = createButton("/", new Color(70, 70, 70));
        decButton = createButton(".", new Color(70, 70, 70));
        equButton = createButton("=", new Color(0, 150, 200));
        delButton = createButton("Del", new Color(200, 50, 50));
        clrButton = createButton("Clr", new Color(200, 50, 50));
        sqrtButton = createButton("√", new Color(70, 70, 70));
        powButton = createButton("^", new Color(70, 70, 70));
        clrHistoryButton = createButton("Clear History", new Color(200, 50, 50));

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;
        functionButtons[8] = sqrtButton;
        functionButtons[9] = powButton;
        functionButtons[10] = clrHistoryButton;

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = createButton(String.valueOf(i), new Color(100, 100, 100));
        }

        // Panel for buttons
        panel = new JPanel();
        panel.setBounds(50, 150, 400, 300);
        panel.setLayout(new GridLayout(5, 4, 10, 10));
        panel.setBackground(new Color(45, 45, 45));

        // Adding buttons to the panel
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);
        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(divButton);
        panel.add(sqrtButton);
        panel.add(powButton);
        panel.add(clrButton);
        panel.add(delButton);

        // Adding extra buttons
        add(panel);
        clrHistoryButton.setBounds(50, 450, 400, 40);
        add(clrHistoryButton);

        setVisible(true);

        // Load history on startup
        loadHistory();
    }

    // Helper method to create styled buttons
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusable(false);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleButtonClick(e.getSource());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Handle key typed events
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        char keyChar = e.getKeyChar();

        // Handle number keys
        if (keyChar >= '0' && keyChar <= '9') {
            inputDisplay.setText(inputDisplay.getText() + keyChar);
        }

        // Handle operator keys
        switch (keyChar) {
            case '+':
                handleButtonClick(addButton);
                break;
            case '-':
                handleButtonClick(subButton);
                break;
            case '*':
                handleButtonClick(mulButton);
                break;
            case '/':
                handleButtonClick(divButton);
                break;
            case '^':
                handleButtonClick(powButton);
                break;
            case '.':
                handleButtonClick(decButton);
                break;
            case '=':
            case '\n': // Enter key
                handleButtonClick(equButton);
                break;
        }

        // Handle special keys
        switch (keyCode) {
            case KeyEvent.VK_BACK_SPACE:
                handleButtonClick(delButton);
                break;
            case KeyEvent.VK_ESCAPE:
                handleButtonClick(clrButton);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Handle key released events
    }

    // Handle button clicks and key presses
    private void handleButtonClick(Object source) {
        if (source == equButton) {
            String expression = inputDisplay.getText();
            try {
                double result = backend.evaluateExpression(expression);
                outputDisplay.setText(String.valueOf(result));
                database.saveOperation(expression, result);
                loadHistory();
            } catch (Exception ex) {
                outputDisplay.setText("Error: Invalid Expression");
            }
        } else if (source == clrButton) {
            inputDisplay.setText("");
            outputDisplay.setText("");
        } else if (source == delButton) {
            String currentText = inputDisplay.getText();
            if (!currentText.isEmpty()) {
                inputDisplay.setText(currentText.substring(0, currentText.length() - 1));
            }
        } else if (source == clrHistoryButton) {
            database.clearHistory();
            loadHistory();
        } else if (source instanceof JButton) {
            JButton button = (JButton) source;
            inputDisplay.setText(inputDisplay.getText() + button.getText());
        }
    }

    // Load history from database
    private void loadHistory() {
        List<String> history = database.loadHistory();
        historyArea.setText("");
        for (String operation : history) {
            historyArea.append(operation + "\n");
        }
    }

    public static void main(String[] args) {
        new CalculatorFrontend();
    }
}