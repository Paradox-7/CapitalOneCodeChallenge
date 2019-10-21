import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractCommentScanner {
    protected Map<Integer, String> comments = new LinkedHashMap<>();
    protected Map<Integer, String> codes = new LinkedHashMap<>();

    protected String inputFilePath;
    protected int totalLineNumber = 0;

    protected int totalCommentLine = 0;
    protected int totalSingleLineComment = 0;
    protected int totalMultipleLineComment = 0;
    protected int totalMultipleLineBlock = 0;
    protected int totalTodo = 0;

    AbstractCommentScanner(String path) {
        inputFilePath = path;
    }

    abstract void readInput(String path);
    abstract void processCode();
    abstract void processComment();

    private void printResults() {
        System.out.println(String.format("Processed file: %s", inputFilePath));
        System.out.println(String.format("Total # of lines: %d", totalLineNumber));
        System.out.println(String.format("Total # of comment lines: %d", totalSingleLineComment + totalMultipleLineComment));
        System.out.println(String.format("Total # of single line comments: %d", totalSingleLineComment));
        System.out.println(String.format("Total # of comment lines within block comments: %d", totalMultipleLineComment));
        System.out.println(String.format("Total # of block line comments: %d", totalMultipleLineBlock));
        System.out.println(String.format("Total # of TODO’s: %d\n", totalTodo));
    }

    public void run() {
        readInput(inputFilePath);
        processCode();
        processComment();
        printResults();
    }
}
