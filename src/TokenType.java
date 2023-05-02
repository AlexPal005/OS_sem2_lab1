public enum TokenType {
    Identifier("Identifier"),
    KeyWord("KeyWord"),
    Punctuator("Punctuator"),

    //Numeric literal
    DecimalLiteral("DecimalLiteral"),
    HexIntegerLiteral("HexIntegerLiteral"),
    OctalIntegerLiteral("OctalIntegerLiteral"),
    BinaryLiteral("BinaryLiteral"),
    BigIntLiteral("BigIntLiteral"),


    StringLiteral("StringLiteral"),
    NullLiteral("NullLiteral"),
    BooleanLiteral("BooleanLiteral"),

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
