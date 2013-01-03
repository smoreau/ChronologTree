import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents an element of the chronolog tree.
 * User: Stephane Moreau <smoreau@logikdev.com>
 */
public class ChronologElement {
    private static final String ANONYMOUS_NAME = "?UNKNOWN?";
    private static final String PREFIX_SUB    = "+- ";
    private static final String PREFIX_LAST   = "\\- ";
    private static final String PREFIX_SUBSUB = "|  ";

    private String name;
    private ChronologElement parent;
    private List<ChronologElement> children;
    private Time time;

    /**
     * Creates a <code>ChronologElement</code>.
     * @param name the name of the element
     */
    public ChronologElement(String name) {
        this(name, null);
    }

    /**
     * Creates a <code>ChronologElement</code>.
     * @param name the name of the element
     * @param parent the parent of the element
     */
    public ChronologElement(String name, ChronologElement parent) {
        this.name = name;
        this.parent = parent;
        this.children = new ArrayList<ChronologElement>();
        this.time = new Time();
    }

    /**
     * Gets the parent of the element. If the parent doesn't exist, creates an anonymous one.
     * @param create if <code>true</code>, creates an anonymous parent
     * @return the parent
     */
    public ChronologElement getParent(boolean create) {
        if (parent == null && create) {
            parent = createAnonymousElement();
            parent.addChild(this);
        }
        return parent;
    }

    /**
     * Gets the children of the element.
     * @return the children
     */
    public List<ChronologElement> getChildren() {
        return children;
    }

    /**
     * Adds a child to the element.
     * @param child the child to be added
     * @return <code>true</code> if successfully added; <code>false</code> otherwise
     */
    public boolean addChild(ChronologElement child) {
        return children.add(child);
    }

    /**
     * Sets the time of the element.
     * @param time the time in millis
     */
    public void setTime(long time) {
        this.time.setTime(time);
    }

    private String toString(String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix);
        builder.append(name);
        builder.append(time);
        builder.append("\n");
        prefix = prefix.substring(0, prefix.length() - 3) + PREFIX_SUBSUB;
        for (Iterator<ChronologElement> it = children.iterator(); it.hasNext(); ) {
            ChronologElement child = it.next();
            builder.append(child.toString(prefix + (it.hasNext() ? PREFIX_SUB : PREFIX_LAST)));
        }
        return builder.toString();
    }

    /**
     * Prints the element and his children recursively.
     * @return the <code>String</code> representing the element
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(time);
        builder.append("\n");
        for (Iterator<ChronologElement> it = children.iterator(); it.hasNext(); ) {
            ChronologElement child = it.next();
            builder.append(child.toString(it.hasNext() ? PREFIX_SUB : PREFIX_LAST));
        }
        return builder.toString();
    }

    /**
     * Creates an anonymous element.
     * @return an anonymous element
     */
    public static ChronologElement createAnonymousElement() {
        return new ChronologElement(ANONYMOUS_NAME);
    }


    /**
     * This class represents the time in millis of the element.
     */
    private final class Time {
        private static final String TIME_PREFIX = "  [";
        private static final String TIME_SUFFIX = "]";

        private Long time;

        /**
         * Sets the time of the element.
         * @param time the time in millis
         */
        public void setTime(long time) {
            this.time = time;
        }

        /**
         * Prints the time.
         * @return the <code>String</code> representing the time
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (time != null) {
                builder.append(TIME_PREFIX);
                builder.append(time);
                builder.append(TIME_SUFFIX);
            }
            return builder.toString();
        }
    }
}
