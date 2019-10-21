import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<AbstractCommentScanner> scanners = new ArrayList<>();
        for (String filePath : args) {
            Path path = Paths.get(filePath);
            String fileName = path.getFileName().toString();
            if (fileName.startsWith(".")) {
                continue;
            }
            if (fileName.endsWith(".py")) {
                scanners.add(new PythonCommentScanner(filePath));
            }
            else if (fileName.endsWith(".java") ||fileName.endsWith(".js")) {
                scanners.add(new JavaCommentScanner(filePath));
            }
        }
        for (AbstractCommentScanner scanner : scanners) {
            scanner.run();
        }
    }
}
