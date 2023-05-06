import java.util.ArrayList;
import java.util.Arrays;

public class Tokenizer {

    private final ArrayList<String> keyWords =
            new ArrayList<>(Arrays.asList("break", "case", "catch", "class", "const", "continue", "debugger", "default",
                    "delete", "do", "else", "export", "extends", "false", "finally", "for", "function", "if", "import",
                    "in", "instanceof", "new", "null", "return", "super", "switch", "this", "throw", "true", "try", "typeof",
                    "var", "void", "while", "with", "let", "static", "yield", "await", "as", "async", "eval", "from", "get",
                    "of", "set", "implements", "package", "protected", "static", "interface", "private", "public"));

    private final ArrayList<Character> punctuators = new ArrayList<>(
            Arrays.asList('(', ')', '{', '}', '[', ']', ':', ';', ',', '?')
    );
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

            //check string literal
            if (code.charAt(i) == '"') {
                checkStringLiteral('"');
                continue;
            } else if (code.charAt(i) == '\'') {
                checkStringLiteral('\'');
                continue;
            }

            // check keywords
            checkKeyWord();


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

    private void checkKeyWord() {
        StringBuilder currWord = new StringBuilder();
        while (code.charAt(i) != '=' && code.charAt(i) != '+' && code.charAt(i) != '-' && code.charAt(i) != ' '
                && !punctuators.contains(code.charAt(i))) {

            currWord.append(code.charAt(i));
            if (keyWords.contains(currWord.toString())) {
                if (Character.isLetter(code.charAt(i + 1))) {
                    i++;
                    continue;
                }
                tokens.add(new Token(TokenType.KeyWord, "\"" + currWord + "\""));
                currWord.setLength(0);
            }
            i++;
        }
        if (currWord.length() != 0) {
            tokens.add(new Token(TokenType.Identifier, "\"" + currWord + "\""));
        }
    }

    private Token checkNumber() {
        return null;
    }
}
