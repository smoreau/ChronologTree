import java.io.File;
import java.util.*;

/**
 * This class manages the list of files to read.
 * User: Stephane Moreau <smoreau@logikdev.com>
 */
public class ChronologFiles {
    private static final String PREFIX = "chronolog";

    private List<File> files;

    /**
     * Creates a <code>ChronologFiles</code>.
     */
    public ChronologFiles() {
        this.files = new ArrayList<File>();
    }

    /**
     * Adds files to the list.
     * @param files the files to add
     */
    public void add(File[] files) {
        for (File file: files) {
            add(file);
        }
    }

    /**
     * Adds the given file to the list. If the file is a directory, adds all his children recursively.
     * @param file the file to add
     */
    public void add(File file) {
        if (file.isDirectory()) {
            add(file.listFiles());
        } else {
            if (file.getName().startsWith(PREFIX)) {
                this.files.add(file);
            }
        }
    }

    /**
     * Adds the file represented by the given path to the list.
     * @param filePath the file path
     */
    public void add(String filePath) {
        add(new File(filePath));
    }

    private void sort() {
        Collections.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            }
        });
    }

    /**
     * Gets the list of files sorted by last modified date.
     * @return the list of files
     */
    public List<File> get() {
        this.sort();
        return files;
    }

}
