import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCommentScanner extends AbstractCommentScanner {
    private static String multiCommentBlockStart = "/*";
    private static String singleCommentStart = "//";
    private static String multiLineCommentStart = "*";
    private static String TODO = "TODO";

    JavaCommentScanner(String path) {
        super(path);
    }

    @Override
    void readInput(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            String line = reader.readLine();
            while (line != null) {
                String trimedString = line.trim();
                if (trimedString.startsWith("/") || trimedString.startsWith("*")) {
                    comments.put(totalLineNumber, trimedString);
                }
                else if (trimedString.length() > 0){
                    codes.put(totalLineNumber, trimedString);
                }
                line = reader.readLine();
                totalLineNumber++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void processCode() {
        Pattern SingleCommentPattern = Pattern.compile(".*(\"\")*.*//.*");
        Pattern MultipleCommentPattern = Pattern.compile(".*(\"\")*.*/\\*.*");

        for (Map.Entry<Integer, String> code : codes.entrySet()) {
            String codeContent = code.getValue();
            if (codeContent.endsWith(";")) {
                continue;
            }
            Matcher singleMatcher = SingleCommentPattern.matcher(codeContent);
            Matcher multiMatcher = MultipleCommentPattern.matcher(codeContent);
            if (singleMatcher.matches()) {
                totalSingleLineComment++;
                if (singleMatcher.group(0).contains("TODO")) {
                    totalTodo++;
                }
            }
            else if (multiMatcher.matches()) {
                totalMultipleLineComment++;
                totalMultipleLineBlock++;
                if (multiMatcher.group(0).contains("TODO")) {
                    totalTodo++;
                }
            }

        }
    }

    @Override
    void processComment() {
        for (Map.Entry<Integer, String> comment : comments.entrySet()) {
            if (comment.getValue().length() >= 2) {
                if (comment.getValue().startsWith(singleCommentStart)) {
                    totalSingleLineComment++;
                }
                else if (comment.getValue().startsWith(multiCommentBlockStart)) {
                    totalMultipleLineBlock++;
                    totalMultipleLineComment++;
                }
                else if (comment.getValue().startsWith(multiLineCommentStart)) {
                    totalMultipleLineComment++;
                }

                if (comment.getValue().contains(TODO)) {
                    totalTodo++;
                }
            }
            else {
                // line contains single char *
                totalMultipleLineComment++;
            }
        }
    }
}
