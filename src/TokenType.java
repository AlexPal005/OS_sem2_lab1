public enum TokenType {
    Identifier("Identifier"),
    KeyWord("KeyWord"),
    Punctuator("Punctuator"),

    NumberLiteral("NumberLiteral"),
    BigIntLiteral("BigIntLiteral"),
    NullishCoalescing("NullishCoalescing"),


    StringLiteral("StringLiteral"),
    NullLiteral("NullLiteral"),
    BooleanLiteral("BooleanLiteral"),
    UndefinedLiteral("UndefinedLiteral"),

    ArithmeticOperator("ArithmeticOperator"),
    LogicalOperator("LogicalOperator"),
    ComparisonOperator("ComparisonOperator"),
    Assignment("Assignment"),
    AssignmentOperator("AssignmentOperator"),
    BitwiseOperator("BitwiseOperator"),
    IncrDecrOperator("IncrDecrOperator"),

    Arrow("Arrow"),

    SpreadOperator("SpreadOperator"),


    RegularExpressionLiteral("RegularExpressionLiteral"),
    TemplateLiteral("TemplateLiteral"),

    Error("Error");

    private final String tokenValue;

    TokenType(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getTokenValue() {
        return tokenValue;
    }
}
