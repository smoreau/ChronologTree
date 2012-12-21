import java.io.*;

/**
 * This class represents the chronolog tree.
 * User: Stephane Moreau <smoreau@logikdev.com>
 */
public class ChronologTree {
    private static final String IO_IN = "IN";
    private static final String IO_OUT = "OUT";

    private String correlationId;
    private ChronologElement current;

    /**
     * Creates a <code>ChronologTree</code>.
     * @param correlationId the correlation ID
     */
    public ChronologTree(String correlationId) {
        this.correlationId = correlationId;
        this.current = ChronologElement.createAnonymousElement();
    }

    private void scanLine(String line) {
        String[] elements = line.split("\\|");
        String inOut = elements[11];
        String name = elements[13];

        if (IO_IN.equals(inOut)) {
            ChronologElement newElement = new ChronologElement(name, current);
            current.addChild(newElement);
            current = newElement;
        } else if (IO_OUT.equals(elements[11])) {
            current = current.getParent(true);
        } else {
            throw new RuntimeException("This column should contains IN or OUT!");
        }
    }

    private void scanFile(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            if (strLine.indexOf(this.correlationId) != -1) {
                scanLine(strLine);
            }
        }
        in.close();
    }

    private void print() {
        if (current.getChildren().isEmpty()) {
            System.err.println("Correlation '" + this.correlationId + "' not found");
        } else {
            ChronologElement first = current.getChildren().iterator().next();
            System.out.println(first.toString());
        }
    }

    /**
     * The main method of this application.
     * @param args the arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Invalid arguments");
            return;
        }

        ChronologTree chronologTree = new ChronologTree(args[0]);

        ChronologFiles chronologFiles = new ChronologFiles();
        for (int i = 1; i < args.length; ++i) {
            chronologFiles.add(args[i]);
        }

        for (File file : chronologFiles.get()) {
            System.out.println("Reading the file " + file.getPath());
            try {
                chronologTree.scanFile(file);
            } catch (IOException e) {
                System.err.println("Unable to read the file " + file.getPath());
            }
        }

        chronologTree.print();
    }
}
