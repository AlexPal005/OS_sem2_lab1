import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Tokenizer {

    private final ArrayList<String> keyWords =
            new ArrayList<>(Arrays.asList("break", "case", "catch", "class", "const", "continue", "debugger", "default",
                    "delete", "do", "else", "export", "extends", "finally", "for", "function", "if", "import",
                    "in", "instanceof", "new", "return", "super", "switch", "this", "throw", "try", "typeof",
                    "var", "void", "while", "with", "let", "static", "yield", "await", "as", "async", "eval", "from", "get",
                    "of", "set", "implements", "package", "protected", "static", "interface", "private", "public"));

    private final ArrayList<Character> punctuators = new ArrayList<>(
            Arrays.asList('(', ')', '{', '}', '[', ']', ':', ';', ',', '?'));

    private final ArrayList<Character> arithmeticOperators = new ArrayList<>(
            Arrays.asList('+', '-', '*', '/', '%'));

    private final ArrayList<String> comparisonOperators = new ArrayList<>(
            Arrays.asList("==", "===", ">", ">=", "<", "<=", "!=", "!=="));

    private final ArrayList<String> logicalOperators = new ArrayList<>(
            Arrays.asList("||", "&&", "!"));

    private final ArrayList<String> bitwiseOperators = new ArrayList<>(
            Arrays.asList("<<", ">>", ">>>", "&", "|", "^", "~"));

    private final ArrayList<String> assignmentOperators = new ArrayList<>(
            Arrays.asList("+=", "-=", "*=", "/=", "%=", "**=", ">>=", ">>>=", "&=", "^=", "|=", "&&=", "||=", "??="));
    private final String code;
    //current index
    private int i;

    private final ArrayList<Token> tokens;

    public Tokenizer(String code) {
        this.code = code;
        this.tokens = new ArrayList<>();
    }

    public void runTokenizer() {
        for (i = 0; i < code.length(); i++) {
            // skip spaces
            if (code.charAt(i) == ' ') {
                continue;
            }
            if (!skipComments()) {
                writeInFile();
                return;
            }

            //skip \n
            if (code.charAt(i) == '\n') {
                continue;
            }

            // check single char token
            Token token = checkSingleCharToken();
            if (token != null) {
                tokens.add(token);
                continue;
            }

            //check numbers
            if (Character.isDigit(code.charAt(i)) ||
                    (code.charAt(i) == '-' && tokens.get(tokens.size() - 1).getTokenType() != TokenType.NumberLiteral)) {

                checkNumber();
                i--;
                continue;
            }

            //check string literal
            if (code.charAt(i) == '"') {
                checkStringLiteral('"');
                i--;
                continue;
            } else if (code.charAt(i) == '\'') {
                checkStringLiteral('\'');
                i--;
                continue;
            } else if (code.charAt(i) == '`') {
                checkStringLiteral('`');
                i--;
                continue;
            }

            if (code.charAt(i) == '/' && Character.isLetter(code.charAt(i + 1))) {
                checkRegularExpression();
                i--;
                continue;
            }
            checkSpecialTokens();


            // check keywords
            Token token1 = checkKeyWord();
            if (token1 != null) {
                tokens.add(token1);
            }
        }
        writeInFile();
    }


    private void writeInFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.txt"));
            tokens.forEach(token -> {
                try {
                    writer.write(token.toString() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkRegularExpression() {
        StringBuilder regex = new StringBuilder();
        do {
            regex.append(code.charAt(i));
            i++;
        } while (code.charAt(i) != '/');

        regex.append(code.charAt(i));
        i++;

        tokens.add(new Token(TokenType.RegularExpressionLiteral, regex.toString()));
    }

    private boolean skipComments() {
        //skip comments
        if (code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
            while (code.charAt(i) != '\n') {
                i++;
            }
            i++;
        }
        if (code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
            i = i + 2;
            while (code.charAt(i) != '*' || code.charAt(i + 1) != '/') {
                i++;
                if (i == code.length()) {
                    tokens.add(new Token(TokenType.Error, "Unfinished comment"));
                    return false;
                }
            }
            i = i + 2;
        }
        return true;
    }

    private Token checkSingleCharToken() {
        if (punctuators.contains(code.charAt(i))) {
            return new Token(TokenType.Punctuator, "\"" + code.charAt(i) + "\"");
        } else if (code.charAt(i) == '.') {
            if (code.charAt(i + 1) == '.' && code.charAt(i + 2) == '.') {
                i = i + 2;
                return new Token(TokenType.SpreadOperator, "\"" + "..." + "\"");
            } else {
                return new Token(TokenType.Punctuator, "\"" + code.charAt(i) + "\"");
            }
        } else return null;
    }

    private void checkStringLiteral(char mark) {
        StringBuilder stringValue = new StringBuilder();
        do {
            if (mark == '`' && code.charAt(i) == '$' && code.charAt(i + 1) == '{') {

                StringBuilder template = new StringBuilder();
                template.append(code.charAt(i));
                do {
                    i++;
                    template.append(code.charAt(i));
                } while (code.charAt(i) != '}');
                tokens.add(new Token(TokenType.TemplateLiteral, template.toString()));
                i++;
                if (i == code.length()) {
                    tokens.add(new Token(TokenType.Error, "Unfinished string literal"));
                    return;
                }
            }
            stringValue.append(code.charAt(i));
            i++;
            if (i == code.length()) {
                tokens.add(new Token(TokenType.Error, "Unfinished string literal"));
                return;
            }
        } while (code.charAt(i) != mark);

        stringValue.append(code.charAt(i));
        i++;

        tokens.add(new Token(TokenType.StringLiteral, stringValue.toString()));

    }

    private Token checkKeyWord() {
        StringBuilder currWord = new StringBuilder();
        while (code.charAt(i) != '=' && code.charAt(i) != '+' && code.charAt(i) != '-' && code.charAt(i) != ' '
                && code.charAt(i) != '\n' && code.charAt(i) != '\t' && !punctuators.contains(code.charAt(i))
                && !logicalOperators.contains(code.charAt(i) + "") && !bitwiseOperators.contains(code.charAt(i) + "")
                && code.charAt(i) != '.') {
            currWord.append(code.charAt(i));
            if (keyWords.contains(currWord.toString())) {
                if (Character.isLetter(code.charAt(i + 1))) {
                    i++;
                    continue;
                }
                return new Token(TokenType.KeyWord, "\"" + currWord + "\"");
            }
            i++;
        }
        //check boolean literal
        if (currWord.toString().equals("true") || currWord.toString().equals("false")) {
            i--;
            return new Token(TokenType.BooleanLiteral, "\"" + currWord + "\"");
        } else if (currWord.toString().equals("null")) {  // check null
            i--;
            return new Token(TokenType.NullLiteral, "\"" + currWord + "\"");
        } else if (currWord.toString().equals("undefined")) {   // check undefined
            i--;
            return new Token(TokenType.UndefinedLiteral, "\"" + currWord + "\"");
        } else if (currWord.length() != 0) {    // check identifier
            i--;
            return new Token(TokenType.Identifier, "\"" + currWord + "\"");
        }
        return null;
    }

    private void checkNumber() {
        StringBuilder number = new StringBuilder();
        if (code.charAt(i) == '-') {
            number.append(code.charAt(i));
            i++;
            while (code.charAt(i) == ' ') {
                i++;
            }
        }
        while (code.charAt(i) != '=' && !arithmeticOperators.contains(code.charAt(i))
                && code.charAt(i) != ' '
                && !punctuators.contains(code.charAt(i))) {

            number.append(code.charAt(i));
            i++;
        }
        if (code.charAt(i) == 'n') {
            tokens.add(new Token(TokenType.BigIntLiteral, "\"" + number + "\""));
        } else {
            tokens.add(new Token(TokenType.NumberLiteral, "\"" + number + "\""));
        }
    }

    private void checkSpecialTokens() {

        StringBuilder operator = new StringBuilder();
        int currIndex = i;
        while (!Character.isLetter(code.charAt(currIndex))
                && !Character.isDigit(code.charAt(currIndex))
                && code.charAt(currIndex) != '\n'
                && code.charAt(currIndex) != ' '
                && code.charAt(currIndex) != '\t'
                && !punctuators.contains(code.charAt(currIndex))) {

            operator.append(code.charAt(currIndex));
            currIndex++;
        }

        if (operator.length() != 0 && comparisonOperators.contains(operator.toString())) {

            tokens.add(new Token(TokenType.ComparisonOperator, "\"" + operator + "\""));
            i = currIndex;

        } else if (operator.length() != 0 && operator.toString().equals("=")) {

            tokens.add(new Token(TokenType.Assignment, "\"" + operator + "\""));
            i = currIndex;

        } else if (operator.length() != 0 && logicalOperators.contains(operator.toString())) {

            tokens.add(new Token(TokenType.LogicalOperator, "\"" + operator + "\""));
            i = currIndex;

        } else if (operator.length() != 0 && bitwiseOperators.contains(operator.toString())) {

            tokens.add(new Token(TokenType.BitwiseOperator, "\"" + operator + "\""));
            i = currIndex;

        } else if (operator.length() != 0 && operator.toString().equals("=>")) {

            tokens.add(new Token(TokenType.Arrow, "\"" + operator + "\""));
            i = currIndex;

        } else if (operator.length() == 1 && arithmeticOperators.contains(operator.charAt(0))) {

            tokens.add(new Token(TokenType.ArithmeticOperator, "\"" + operator + "\""));
            i = currIndex;

        } else if (operator.length() != 0 && assignmentOperators.contains(operator.toString())) {

            tokens.add(new Token(TokenType.AssignmentOperator, "\"" + operator + "\""));
            i = currIndex;

        } else if (operator.length() != 0 && (operator.toString().equals("++") || operator.toString().equals("--"))) {

            tokens.add(new Token(TokenType.IncrDecrOperator, "\"" + operator + "\""));
            i = currIndex - 1;

        }
    }

}
