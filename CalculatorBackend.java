import java.util.*;

public class CalculatorBackend {

    // Evaluate the expression using the Shunting Yard Algorithm
    public double evaluateExpression(String expression) {
        // Remove all whitespace
        expression = expression.replaceAll("\\s+", "");

        // Stack for numbers
        Stack<Double> numbers = new Stack<>();

        // Stack for operators
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If the character is a digit or decimal point, parse the number
            if (Character.isDigit(c) || c == '.') {
                StringBuilder numBuilder = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    numBuilder.append(expression.charAt(i));
                    i++;
                }
                i--; // Move back one step
                numbers.push(Double.parseDouble(numBuilder.toString()));
            }
            // If the character is an operator or parenthesis
            else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '√') {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    applyOperation(numbers, operators.pop());
                }
                operators.push(c);
            }
            // If the character is an opening parenthesis
            else if (c == '(') {
                operators.push(c);
            }
            // If the character is a closing parenthesis
            else if (c == ')') {
                while (operators.peek() != '(') {
                    applyOperation(numbers, operators.pop());
                }
                operators.pop(); // Remove the '(' from the stack
            }
        }

        // Apply remaining operations
        while (!operators.isEmpty()) {
            applyOperation(numbers, operators.pop());
        }

        // The final result is the only number left in the stack
        return numbers.pop();
    }

    // Helper method to apply an operation
    private void applyOperation(Stack<Double> numbers, char operator) {
        double b = numbers.pop();
        double a = numbers.isEmpty() ? 0 : numbers.pop(); // Handle unary operators like square root

        switch (operator) {
            case '+':
                numbers.push(a + b);
                break;
            case '-':
                numbers.push(a - b);
                break;
            case '*':
                numbers.push(a * b);
                break;
            case '/':
                numbers.push(a / b);
                break;
            case '^':
                numbers.push(Math.pow(a, b));
                break;
            case '√':
                numbers.push(Math.sqrt(b)); // Square root is a unary operator
                break;
        }
    }

    // Helper method to determine operator precedence
    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
            case '√':
                return 3;
            case '(':
            case ')':
                return 0; // Parentheses have the lowest precedence
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}