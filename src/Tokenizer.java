import java.util.ArrayList;

public class Tokenizer {

    private final String[] keyWords = {"break", "case", "catch", "class", "const", "continue", "debugger", "default",
            "delete", "do", "else", "export", "extends", "false", "finally", "for", "function", "if", "import",
            "in", "instanceof", "new", "null", "return", "super", "switch", "this", "throw", "true", "try", "typeof",
            "var", "void", "while", "with", "let", "static", "yield", "await", "as", "async", "eval", "from", "get",
            "of", "set", "implements", "package", "protected", "static", "interface", "private", "public"};

    private String code;

    private final ArrayList<Token> tokens;

    public Tokenizer(String code) {
        this.code = code;
        this.tokens = new ArrayList<>();
    }

    public void runTokenizer() {
        for (int i = 0; i < code.length(); i++) {

        }
        System.out.println(checkSingleCharToken(code.charAt(0)));
    }

    private Token checkSingleCharToken(char symbol) {
        if (symbol == '(') {
            return new Token(TokenType.Punctuator, "\"(\"");
        }
        return new Token(TokenType.None, "");
    }


}
