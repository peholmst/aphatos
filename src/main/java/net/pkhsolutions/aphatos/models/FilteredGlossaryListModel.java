package net.pkhsolutions.aphatos.models;

import net.pkhsolutions.aphatos.domain.Glossary;

/**
 * Extended version of {@link GlossaryListModel} that supports filtering, i.e.
 * only those words that start with a specific filter string are shown. The
 * filtering algorithm is a combination of binary search and sequential scan.
 *
 * @author Petter Holmström
 */
public class FilteredGlossaryListModel extends GlossaryListModel {

    private static final long serialVersionUID = 123L;

    private String filter = "";

    private int startIndex;

    private int endIndex;

    /**
     * Checks whether the model is currently filtered or displaying all words.
     *
     * @return <code>true</code> if the model is filtered, <code>false</code>
     * otherwise.
     */
    public boolean isFiltered() {
        return !filter.isEmpty();
    }

    /**
     * Calculates the interval of the main glossary to show.
     */
    private void calculteFilterInterval() {
        Glossary glossary = doGetGlossary();
        if (!filter.isEmpty() && glossary.getSize() > 0) {
            int pivot;
            int cmp;
            int begin = 0;
            int end = glossary.getSize() - 1;

            while (begin < end) {
                pivot = begin + ((end - begin) / 2);
                cmp = glossary.get(pivot).compareTo(filter);
                if (cmp >= 0) { // Filter is before 'pivot'
                    end = pivot - 1;
                } else { // Filter is after 'pivot'
                    begin = pivot + 1;
                }
            }
            // Depending on the glossary, we are either at
            // the starting position of the filter or at the
            // position before
            if (glossary.get(begin).startsWith(filter)
                    || begin < glossary.getSize() - 1
                    && glossary.get(++begin).startsWith(filter)) {
                // Start a scan to find the rest of the items
                startIndex = begin;
                int i = startIndex + 1;
                while (i < glossary.getSize()
                        && glossary.get(i).startsWith(filter))
                    i++;
                endIndex = i - 1;
            } else {
                // No items found
                startIndex = 0;
                endIndex = -1;
            }
        } else {
            if (logger.isDebugEnabled())
                logger.debug("Filtering is turned off");
            startIndex = 0;
            endIndex = -1;
        }
        fireContentsChanged(this, 0, 0);
    }

    /**
     * Gets the current filter string. If the string is empty, filtering is not
     * activated.
     *
     * @return the filter string.
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Sets the filter to use. An empty string turns filtering off. Filtering is
     * case sensitive.
     *
     * @param filter the filter string to use.
     */
    public void setFilter(String filter) {
        if (filter == null)
            filter = "";

        if (logger.isDebugEnabled())
            logger.debug("Setting filter to: " + filter);

        if (!this.filter.equals(filter)) {
            this.filter = filter;
            calculteFilterInterval();
        }
    }

    @Override
    public int getSize() {
        if (isFiltered())
            return endIndex - startIndex + 1;
        else
            return super.getSize();
    }

    @Override
    public String getElementAt(int index) {
        if (isFiltered()) {
            if (startIndex + index > endIndex)
                throw new IndexOutOfBoundsException();
            return super.getElementAt(startIndex + index);
        } else
            return super.getElementAt(index);
    }

    @Override
    public void wordAdded(Glossary sender, String word, int position) {
        calculteFilterInterval();
    }

    @Override
    public void wordDeleted(Glossary sender, String word, int position) {
        calculteFilterInterval();
    }

    @Override
    public void glossaryRefreshed(Glossary sender) {
        calculteFilterInterval();
    }
}
