import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PythonCommentScanner extends AbstractCommentScanner {

    private static String singleCommentStart = "#";
    private static String TODO = "TODO";

    PythonCommentScanner(String path) {
        super(path);
    }

    @Override
    void readInput(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            String line = reader.readLine();
            while (line != null) {
                String trimedString = line.trim();
                if (trimedString.startsWith(singleCommentStart)) {
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
        Pattern SingleCommentPattern = Pattern.compile(".*(\"\")*('')*.*#.*");

        for (Map.Entry<Integer, String> code : codes.entrySet()) {
            String codeContent = code.getValue();
            Matcher singleMatcher = SingleCommentPattern.matcher(codeContent);

            if (singleMatcher.matches()) {
                totalSingleLineComment++;
                if (singleMatcher.group(0).contains(TODO)) {
                    totalTodo++;
                }
            }
        }
    }

    @Override
    void processComment() {
        totalTodo = comments.values()
                .stream()
                .filter(value -> value.contains("TODO"))
                .collect(Collectors.toCollection(ArrayList::new)).size();

        List<Integer> commentLineNumber = new ArrayList<>(comments.keySet());

        int blockStartIndex = 0;
        int curIndex = 0;
        int blockStartLineNumber = commentLineNumber.get(blockStartIndex);
        int curLineNumber = commentLineNumber.get(curIndex);
        int length = commentLineNumber.size();
        while (blockStartIndex < length && curIndex < length) {
            if ((curIndex + 1) < length && (commentLineNumber.get(curIndex + 1) == curLineNumber + 1)){
                curIndex++;
                curLineNumber = commentLineNumber.get(curIndex);
            }
            else {
                if ((curLineNumber - blockStartLineNumber + 1) >= 2) {
                    totalMultipleLineBlock++;
                    totalMultipleLineComment += (curLineNumber - blockStartLineNumber + 1);
                }
                if (curIndex == length - 1) {
                    break;
                }
                blockStartIndex = curIndex + 1;
                blockStartLineNumber = commentLineNumber.get(blockStartIndex);
                curIndex++;
                curLineNumber = commentLineNumber.get(curIndex);

            }
        }
        totalSingleLineComment += (commentLineNumber.size() - totalMultipleLineComment);
    }
}
