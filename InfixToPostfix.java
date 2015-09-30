/*
 *      github.com/oscar0812
 *      SUPPORTING THE FREE CODE OF CONDUCT
 *
 *      Example input:               Example output:   
 *      9 + 0                        postifx =  9 0 +
 *                                   9
 *
 *      (9*9-0+8*7*7)                postfix =  9 9 * 0 - 8 7 * 7 * +
 *                                   473
 *
 *
 *
 */
import java.io.BufferedReader;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.io.InputStreamReader;

/**
 * A class to evaluate postfix and infix expressions
 */
public class InfixToPostfix {

    public static Stack<Integer> stack = new Stack<>();

    /**
     * Evaluate infix expressions
     *
     * @param args
     */
    public static void main(String[] args) {
        InfixToPostfix.start();
    }

    public static void start() {
        System.out.println("Type an infix expression (to quit, type q)");

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String input; // line input by the user
        boolean quit = false;

        do {
            try {
                System.out.print(">"); // prompt
                input = in.readLine();
                if (input.equals("q")) {
                    quit = true;
                } else {
                    String postfix = toPostfix(input);
                    System.out.println("postfix = " + postfix);
                    System.out.println(computePostfix(postfix));
                }
            } catch (Exception e) {
                System.out.println("Invalid expression");
            }
        } while (!quit);
    }

    /**
     * Converts an infix expression to a postfix expression
     *
     * @param infix the string that contains the infix expression
     * @return a string that contains the expression written in postfix notation
     * @throws java.lang.Exception
     *
     */
    public static String toPostfix(String infix) throws Exception {

        try {
            String postfix = "";
            boolean unary = true; // is the current operator unary?
            Stack<String> stack = new Stack<>();
            StringTokenizer st = new StringTokenizer(infix,
                    "()+-/%* ", true);
            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();
                switch (token) {
                    case "":
                        break;
                    case "(":
                        stack.push(token);
                        break;
                    case ")":
                        String op;
                        while (!(op = stack.pop()).equals("(")) {
                            postfix += " " + op;
                        }
                        break;
                    // an operator
                    case "*":
                    case "+":
                    case "-":
                    case "%":
                    case "/":
                        if (unary) {
                            token = "u" + token;
                   // a unary op always goes on
                            // the stack without popping any other op
                            stack.push(token);
                        } else {
                            int p = operatorPrecedence(token);
                            while (!stack.isEmpty() && !stack.peek().equals("(")
                                    && operatorPrecedence(stack.peek()) >= p) {
                                op = stack.pop();
                                postfix += " " + op;
                            }
                            stack.push(token);
                        }
                        unary = true; // if an operator is after this one, it
                        // has to be unary
                        break;
                    default:
                        // an operand
                        Integer.parseInt(token); // just to check that
                        // it is a number
                        // If not a number, an exception is
                        // thrown
                        postfix += " " + token;
                        unary = false; // any operator after an operand is binary
                        break;
                }
            }
            while (!stack.isEmpty()) {
                String op = stack.pop();
                postfix += " " + op;
            }
            return postfix;
        } catch (EmptyStackException | NumberFormatException ese) {
            throw new Exception();
            //throw new ExpressionFormatException();
        }
    }

    /**
     * Evaluates a postfix expression
     *
     * @param postfix the string that contains the postfix expression
     * @return the integer value of the expression
     * @throws java.lang.Exception
     */
    public static int computePostfix(String postfix) throws Exception {

        try {
            // Stack<Integer> stack = new Stack<Integer>();
            StringTokenizer st = new StringTokenizer(postfix);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.equals("*")
                        || // an operator
                        token.equals("+") || token.equals("-")
                        || token.equals("%") || token.equals("/")
                        || token.equals("u+") || token.equals("u-")) {
                    //applyOperator(token, stack);
                    applyOperator(token);
                } else { // an operand
                    stack.push(new Integer(token));
                }
            }
            int result = stack.pop();
            if (!stack.isEmpty()) { // the stack should be empty
                //throw new Exception();
                //throw new ExpressionFormatException();
            }
            return result;
        } catch (Exception ese) {
            throw new Exception();
            //throw new ExpressionFormatException();
        }
    }

    /**
     * Applies the given operator to the top operand or the top two operands on
     * the given stack. Possible operators are unary + and - written as "u+" and
     * "u-", and binary "+", "-", "%", and "/"
     *
     * @param operator the operator to apply
     * @param s the stack of the operands
     * @throws EmptyStackException if the stack is empty
     * @throws IllegalArgumentException if the operator is not /,*,%,+,-,u-,u+
     *
     * post condition: the operator is applied to the top operand or to the top
     * two operands on the stack. The operand(s) is/are popped from the stack.
     * The result is pushed on the stack
     */
// private static void applyOperator(String operator, Stack<Integer> s) {
    private static void applyOperator(String operator) {
        int op1;
        op1 = stack.pop();
        switch (operator) {
            case "u-":
                stack.push(-op1);
                break;
            case "u+":
                stack.push(op1);
                break;
            default:
                // binary operator
                int op2 = stack.pop();
                int result;
                switch (operator) {
                    case "+":
                        result = op2 + op1;
                        break;
                    case "-":
                        result = op2 - op1;
                        break;
                    case "/":
                        result = op2 / op1;
                        break;
                    case "%":
                        result = op2 % op1;
                        break;
                    case "*":
                        result = op2 * op1;
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                stack.push(result);
                break;
        }
    }

    /**
     * Returns an integer indicating the order of precedence of an operator
     * given as a string. Unary + and - return 2, *, / and % return 1 and binary
     * + and - return 0.
     *
     * @param operator the operator given as a string
     * @param binary indicates if the operator is binary (true) or unary (false)
     * @return the precedence order of a given operator
     * @throws ExpressionFormatExpression if the operator is not one of "u+",
     * "u-", "+", "-", "/", "%", and "*"
     */
    private static int operatorPrecedence(String operator) throws Exception {
        switch (operator) {
            case "u-":
            case "u+":
                return 2;
            case "*":
            case "/":
            case "%":
                return 1;
            case "-":
            case "+":
                return 0;
            default:
                throw new Exception();
            //throw new ExpressionFormatException();
        }
    }
}
