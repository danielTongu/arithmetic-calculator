import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicButtonUI;


/**
 * A utility class for performing arithmetic operations.
 *
 * @author Daniel Tongu <br>
 * Date: 3/30/2024
 */
class Arithmetic {

    /**
     * Performs addition of two numbers.
     * @param num1 The first number.
     * @param num2 The second number.
     * @return The result of the addition.
     */
    public static double addition(double num1, double num2) {
        return num1 + num2;
    }

    /**
     * Performs subtraction of two numbers.
     * @param num1 The first number.
     * @param num2 The second number.
     * @return The result of the subtraction.
     */
    public static double subtraction(double num1, double num2) {
        return num1 - num2;
    }

    /**
     * Performs multiplication of two numbers.
     * @param num1 The first number.
     * @param num2 The second number.
     * @return The result of the multiplication.
     */
    public static double multiplication(double num1, double num2) {
        return num1 * num2;
    }

    /**
     * Performs division of two numbers.
     * @param num1 The dividend.
     * @param num2 The divisor.
     * @return The result of the division.
     * @throws ArithmeticException if the divisor is zero.
     */
    public static double division(double num1, double num2) {
        if (num2 != 0) {
            return num1 / num2;
        } else {
            throw new ArithmeticException("Division by zero");
        }
    }
}




/**
 * The Calculator class creates a basic calculator application with a graphical user interface.
 * It allows users to perform arithmetic operations such as addition, subtraction, multiplication, and division.
 * It includes buttons to clear all, or one character from the screen.
 *
 * @author Daniel Tongu <br>
 * Date: 3/30/2024
 *
 */
public class Calculator {

    /** The size of the calculator buttons. */
    private final int buttonSize = 75;

    /** The size of the labels on the calculator buttons, calculated as half of the button size. */
    private final int buttonLabelSize = buttonSize / 2;

    /** Initial text displayed in the calculator display field. */
    public final String INITIAL_TXT = "_";

    /** Text displayed in case of an error. */
    public final String ERROR_TXT = "Error";

    /** The text field where the display text is shown. */
    private final JTextField DISPLAY;

    /** The titled border for the display field. */
    private final TitledBorder DISPLAY_BANNER;

    /** The result of the arithmetic operation. */
    private double result;

    /** The operator to perform the arithmetic operation. */
    private Operator operator;

    /** Indicates whether to append character to the display text. */
    private boolean appendChar;

    /**
     * Constructs a new calculator object.
     * Initializes the operator and sets up the user interface.
     */
    public Calculator() {
        operator = null;
        appendChar = false;
        int gridRows = 5;
        int gridCols = 4;
        int gridGap = 3;
        int padding = buttonSize / gridCols;
        int padding_top = buttonLabelSize;
        int screen_Height = buttonSize + padding_top;
        int WIDTH = 2 * padding + ((gridCols * (buttonSize + gridGap)) - gridGap);
        int HEIGHT = 2 * padding_top + screen_Height + ((gridRows * (buttonSize + gridGap)) - gridGap);

        // Create the frame for the calculator UI
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());

        // Create panel for all calculator components
        JPanel framePanel = new JPanel(new BorderLayout());
        framePanel.setBackground(new Color(43, 45, 48));
        framePanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.WHITE),
                new EmptyBorder(padding_top, padding, padding, padding)
        ));

        // Create a panel for displaying computation inputs and results
        DISPLAY = new JTextField(INITIAL_TXT);
        DISPLAY.setEditable(false);
        DISPLAY.setHorizontalAlignment(JTextField.RIGHT);
        DISPLAY.setPreferredSize(new Dimension(0, screen_Height + padding));
        DISPLAY.setBackground(Color.BLACK);
        DISPLAY.setForeground(Color.WHITE);
        DISPLAY_BANNER = BorderFactory.createTitledBorder(" ");
        DISPLAY_BANNER.setTitleColor(Color.WHITE);
        DISPLAY.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(padding, 0, 0, 0),
                DISPLAY_BANNER
        ));

        // Create the grid
        JPanel buttonsPanel = new JPanel(new GridLayout(gridRows, gridCols, gridGap, gridGap));
        buttonsPanel.setBorder(new EmptyBorder(padding_top, 0, 0, 0));
        buttonsPanel.setOpaque(false);

        // Put the buttons in the grid
        final int GRID_SIZE = gridRows * gridCols;
        Operand[] operands = Operand.values();
        Operator[] operators = Operator.values();

        for (int i = 0, operatorIndex = 0, operandIndex = 0; i < GRID_SIZE; i++) {
            int row = i / gridCols;
            int col = i % gridCols;

            if (row == 0 || col == gridCols - 1) {
                createButton(operators[operatorIndex++], buttonsPanel);
            } else {
                createButton(operands[operandIndex++], buttonsPanel);
            }
        }

        // Display the calculator
        framePanel.add(DISPLAY, BorderLayout.NORTH);
        framePanel.add(buttonsPanel, BorderLayout.CENTER);
        frame.setContentPane(framePanel);
        frame.setVisible(true);

        fitDisplayedText();
    }

    /**
     * Main method to launch the calculator application.
     * @param args The command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }

    /**
     * Fits the displayed text in the display field according to its length.
     */
    private void fitDisplayedText() {
        String currentText = DISPLAY.getText();
        int length = currentText.length();
        int fontSize = Math.min(this.buttonSize, DISPLAY.getWidth() / length );
        DISPLAY.setText(currentText);
        DISPLAY.setFont(DISPLAY.getFont().deriveFont(Font.PLAIN, fontSize));
    }

    /**
     * Trims the trailing ".0" from a string representation of a double if present.
     * @param text The string representation of the number.
     * @return The trimmed string.
     */
    private String trimDouble(String text) {
        if (text.matches("^-?\\d+\\.0$")) {
            text = text.substring(0, text.length() - 2);
        }
        return text;
    }

    /**
     * Trims the trailing ".0" from a double if present.
     * @param number The double value.
     * @return The trimmed string representation of the number.
     */
    private String trimDouble(double number) {
        return trimDouble(String.valueOf(number));
    }

    /**
     * Displays the result of the computation in the display field.
     * @param previous The previous operand.
     * @param currentText The current input text.
     * @param operator The operator.
     */
    private void displayResult(double previous, String currentText, Operator operator) {
        if(operator == null || currentText.isBlank() || currentText.equals(ERROR_TXT)){
            return;
        }

        try {
            double current = Double.parseDouble(currentText);
            this.result = switch (operator) {
                case ADD -> Arithmetic.addition(previous, current);
                case SUBTRACT -> Arithmetic.subtraction(previous, current);
                case MULTIPLY -> Arithmetic.multiplication(previous, current);
                case DIVIDE -> Arithmetic.division(previous, current);
                default -> throw new IllegalArgumentException("Invalid operator: " + operator);
            };
            DISPLAY_BANNER.setTitle(trimDouble(previous) + ' ' + operator.SYMBOL + ' ' + trimDouble(current));
            DISPLAY.setText(trimDouble(result));

        } catch (Exception x){
            DISPLAY_BANNER.setTitle(" ");
            DISPLAY.setText(ERROR_TXT);
        }
        fitDisplayedText();
    }

    /**
     * Creates a button for the calculator.
     * @param type The button type to be created.
     * @param panel The panel to which the button will be added.
     */
    private void createButton(Enum type, JPanel panel) {
        JButton button = new JButton();

        // Set the button text
        if (type instanceof Operand) {
            button.setText(String.valueOf(((Operand) type).SYMBOL));
        } else if (type instanceof Operator) {
            button.setText(String.valueOf(((Operator) type).SYMBOL));
        }

        // Set the button functionality
        button.addActionListener(e -> {
            String currentText = DISPLAY.getText();

            if (type instanceof Operand currentOperand && !currentText.equals(ERROR_TXT)) {
                switch (currentOperand) {
                    case TOGGLE_SIGN:
                        String negation = "-";
                        if (!currentText.equals(INITIAL_TXT) && !currentText.equals("0") && !currentText.equals(Operand.DECIMAL.SYMBOL)) {
                            if (currentText.contains(negation)) {
                                DISPLAY.setText(currentText.substring(1));
                            } else {
                                DISPLAY.setText(negation + currentText);
                            }
                            appendChar = true;
                        }
                        break;
                    case DECIMAL:
                        if (currentText.equals(INITIAL_TXT) || (currentText.contains(currentOperand.SYMBOL) && operator != null)){
                            DISPLAY.setText(currentOperand.SYMBOL);
                            appendChar = true;
                        } else if (!currentText.contains(currentOperand.SYMBOL)) {
                            DISPLAY.setText(currentText + currentOperand.SYMBOL);
                            appendChar = true;
                        }
                        break;
                    default:
                        if(appendChar) {
                            DISPLAY.setText(currentText + currentOperand.SYMBOL);
                        } else {
                            DISPLAY.setText(currentOperand.SYMBOL);
                            appendChar = true;
                        }
                }
                fitDisplayedText();

            } else if (type instanceof Operator currentOperator) {
                String displayedText = DISPLAY.getText().trim();
                appendChar = false;

                switch (currentOperator) {
                    case CLEAR:
                        result = 0;
                        operator = null;
                        DISPLAY_BANNER.setTitle(" ");
                        DISPLAY.setText(INITIAL_TXT);
                        fitDisplayedText();
                        break;
                    case DELETE:
                        if (currentText.length() > 1) {
                            DISPLAY.setText(currentText.substring(0, currentText.length() - 1));
                        } else {
                            DISPLAY.setText(INITIAL_TXT);
                        }
                        fitDisplayedText();
                        break;
                    case PERCENTAGE:
                        try {
                            double value = Double.parseDouble(displayedText);
                            DISPLAY.setText(trimDouble(Arithmetic.division(value, 100)));
                        } catch (Exception x){
                            DISPLAY.setText(ERROR_TXT);
                        }
                        fitDisplayedText();
                        break;
                    case EQUALS:
                        displayResult(result, displayedText, operator);
                        operator = null;
                        break;
                    default:
                        if (operator == null) {
                            try {
                                result = Double.parseDouble(displayedText);
                                DISPLAY_BANNER.setTitle(trimDouble(displayedText) + ' ' + currentOperator.SYMBOL);
                                operator = currentOperator;

                            } catch(NumberFormatException x){
                                DISPLAY.setText(ERROR_TXT);
                            }
                        } else {
                            displayResult(result, displayedText, operator);
                            operator = currentOperator;
                        }
                        break;
                }

            } else {
                DISPLAY.setText(ERROR_TXT);
            }

            DISPLAY.repaint();
        });

        // Style the button
        Color background = (type instanceof Operand) ? new Color(98, 101, 103) : new Color(230, 126, 34);

        if (type == Operand.DECIMAL || type == Operand.TOGGLE_SIGN || type == Operator.CLEAR || type == Operator.DELETE || type == Operator.PERCENTAGE) {
            background = (type == Operand.DECIMAL || type == Operand.TOGGLE_SIGN || type == Operator.PERCENTAGE) ? new Color(70, 73, 75) : new Color(168, 78, 75);
        }

        button.setFocusPainted(false);
        button.setUI(new CustomButtonUI(background, Color.WHITE, 10, type == Operand.TOGGLE_SIGN ? buttonLabelSize / 3: buttonLabelSize));
        panel.add(button);
    }
}

/**
 * Enumeration representing the operands for the calculator.
 */
enum Operand {
    SEVEN('7'), EIGHT('8'), NINE('9'),
    FOUR('4'), FIVE('5'), SIX('6'),
    ONE('1'), TWO('2'), THREE('3'),
    ZERO('0'), DECIMAL('.'), TOGGLE_SIGN("+/-");

    /**
     * Symbol of the operand.
     */
    public final String SYMBOL;

    /**
     * Constructs an operand enum with the given symbol.
     * @param symbol The symbol representing the operand.
     */
    Operand(char symbol) {
        this.SYMBOL = String.valueOf(symbol);
    }

    /**
     * Constructs an operand enum with the given symbol.
     * @param symbol The symbol representing the operand.
     */
    Operand(String symbol) {
        this.SYMBOL = symbol;
    }
}

/**
 * Enumeration representing the operators for the calculator.
 */
enum Operator {
    CLEAR('C'), DELETE('D'), PERCENTAGE('%'), DIVIDE('÷'),
    MULTIPLY('x'), SUBTRACT('-'), ADD('+'), EQUALS('=');

    /**
     * Symbol of the operator.
     */
    public final char SYMBOL;

    /**
     * Constructs an operator enum with the given symbol.
     * @param symbol The symbol representing the operator.
     */
    Operator(char symbol) {
        this.SYMBOL = symbol;
    }
}

/**
 * The CustomButtonUI class provides custom UI for calculator buttons.
 */
class CustomButtonUI extends BasicButtonUI {
    /** Background color of the button. */
    public final Color BACKGROUND;

    /** Foreground color of the button. */
    public final Color FOREGROUND;

    /** Border radius of the button. */
    public final int RADIUS;

    /** Font size of the button text. */
    public final int FONT_SIZE;

    /**
     * Constructs a custom button UI with specified background, foreground, radius, and font size.
     * @param background The background color.
     * @param foreground The foreground color.
     * @param radius The radius of the button corners.
     * @param fontSize The font size of the button text.
     */
    public CustomButtonUI(Color background, Color foreground, int radius, int fontSize) {
        this.BACKGROUND = background;
        this.FOREGROUND = foreground;
        this.RADIUS = radius;
        this.FONT_SIZE = fontSize;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g.create();
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(model.isArmed() ? BACKGROUND.darker() : BACKGROUND);
        g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
        super.paint(g2d, c);
        g2d.dispose();
    }

    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        b.setOpaque(false);
        b.setBorderPainted(false);
        b.setFont(b.getFont().deriveFont(Font.PLAIN, FONT_SIZE));
        b.setForeground(FOREGROUND);
    }
}
