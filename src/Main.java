import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StringBuilder code = new StringBuilder();
        try {
            File myObj = new File("src/input.txt");
            Scanner scanner = new Scanner(myObj);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                code.append(data).append("\n");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred in reading the file!");
            e.printStackTrace();
        }
        Tokenizer tokenizer = new Tokenizer(code.toString());
        tokenizer.runTokenizer();
    }
}