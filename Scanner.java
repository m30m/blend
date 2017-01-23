
import java.io.*;

/**
 * This is the template of class 'scanner'. You should place your own 'scanner class here and
 * your scanner should match this interface.
 */
public class Scanner {
    public String CV;
    int lineNumber = 1;
    private final java.util.Scanner scanner;

    public String getCurrent() {
        return current;
    }

    private String current;

    Scanner(String filename) throws Exception {
        File f = new File(filename);
        if (!f.exists())
            throw new Exception("File does not exist: " + f);
        if (!f.isFile())
            throw new Exception("Should not be a directory: " + f);
        if (!f.canRead())
            throw new Exception("Can not read input file: " + f);
        java.io.FileInputStream fis = new java.io.FileInputStream(f);
        scanner = new java.util.Scanner(fis);

    }

    public String NextToken() throws Exception {
        if (!scanner.hasNext())
            return "$";
        current = scanner.next();
        System.out.println(Integer.parseInt(current));

        System.out.println("next = " + current);
        return current;
    }

}
