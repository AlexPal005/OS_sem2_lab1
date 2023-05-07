public enum TokenType {
    Identifier("Identifier"),
    KeyWord("KeyWord"),
    Punctuator("Punctuator"),

    NumberLiteral("NumberLiteral"),
    BigIntLiteral("BigIntLiteral"),


    StringLiteral("StringLiteral"),
    NullLiteral("NullLiteral"),
    BooleanLiteral("BooleanLiteral"),

    ArithmeticOperator("ArithmeticOperator"),
    LogicalOperator("LogicalOperator"),
    ComparisonOperator("ComparisonOperator"),
    Assignment("Assignment"),
    BitwiseOperator("BitwiseOperator"),


    RegularExpressionLiteral("RegularExpressionLiteral"),
    TemplateLiteral("TemplateLiteral"),

    None("None");

    private final String tokenValue;
    TokenType(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getTokenValue() {
        return tokenValue;
    }
}
