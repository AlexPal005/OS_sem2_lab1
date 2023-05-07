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
    private final String code;
    //current index
    private int i;

    private final ArrayList<Token> tokens;

    public Tokenizer(String code) {
        this.code = code;
        this.tokens = new ArrayList<>();
    }

    public void runTokenizer() {
        for (i = 0; i < code.length(); i++ ) {
            // skip spaces
            if (code.charAt(i) == ' ') {
                continue;
            }
            //skip comments
            if (code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
                while (code.charAt(i) != '\n') {
                    i++;
                }
                i++;
            }
            if (code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                i = i + 2;
                while (code.charAt(i) != '*' && code.charAt(i + 1) != '/') {
                    i++;
                }
                i = i + 2;
            }
            //skip \n
            if (code.charAt(i) == '\n') {
                continue;
            }

            // check single char token
            Token token = checkSingleCharToken(code.charAt(i));
            if (token != null) {
                tokens.add(token);
                continue;
            }
            checkSpecialTokens();

            //check string literal
            if (code.charAt(i) == '"') {
                checkStringLiteral('"');
                i--;
                continue;
            } else if (code.charAt(i) == '\'') {
                checkStringLiteral('\'');
                i--;
                continue;
            }
            //check numbers
            if (Character.isDigit(code.charAt(i))) {
                checkNumber();
                i--;
                continue;
            }
            // check keywords
            Token token1 = checkKeyWord();
            if (token1 != null) {
                tokens.add(token1);
            }
        }
        tokens.forEach(System.out::println);

    }

    private Token checkSingleCharToken(char symbol) {
        if (punctuators.contains(symbol)) {
            return new Token(TokenType.Punctuator, "\"" + symbol + "\"");
        } else {
            return null;
        }
    }

    private void checkStringLiteral(char mark) {
        StringBuilder stringValue = new StringBuilder();
        do {
            stringValue.append(code.charAt(i));
            i++;
        } while (code.charAt(i) != mark);

        stringValue.append(code.charAt(i));
        i++;

        tokens.add(new Token(TokenType.StringLiteral, stringValue.toString()));

    }

    private Token checkKeyWord() {
        StringBuilder currWord = new StringBuilder();
        while (code.charAt(i) != '=' && code.charAt(i) != '+' && code.charAt(i) != '-' && code.charAt(i) != ' '
                && code.charAt(i) != '\n' && code.charAt(i) != '\t' && !punctuators.contains(code.charAt(i))) {
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
        while (code.charAt(i) != '=' && code.charAt(i) != '+'
                && code.charAt(i) != '-' && code.charAt(i) != ' ' && !punctuators.contains(code.charAt(i))) {

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
        if (arithmeticOperators.contains(code.charAt(i))) {
            tokens.add(new Token(TokenType.ArithmeticOperator, "\"" + code.charAt(i) + "\""));
            i++;
            return;
        }
        StringBuilder operator = new StringBuilder();
        int currIndex = i;
        while (!Character.isLetter(code.charAt(currIndex)) && code.charAt(currIndex) != '\n'
                && code.charAt(currIndex) != ' ' && code.charAt(currIndex) != '\t') {
            operator.append(code.charAt(currIndex));
            currIndex++;
        }
        if (operator.length() != 0 && comparisonOperators.contains(operator.toString())) {
            tokens.add(new Token(TokenType.ComparisonOperator, "\"" + operator + "\""));
            i = currIndex;
        }
    }


}
