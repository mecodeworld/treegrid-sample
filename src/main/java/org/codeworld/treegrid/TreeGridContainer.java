package org.codeworld.treegrid;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codeworld.data.DataSource;
import org.vaadin.treegrid.container.Measurable;

import com.vaadin.data.Collapsible;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

public class TreeGridContainer extends HierarchicalContainer implements Collapsible, Measurable {

    private static final long serialVersionUID = -2532564318839706679L;
    public static final String DEACTIVATED = "deactivated";
    public static final String LAST_MODIFIED = "lastModified";
    public static final String STATUS = "status";
    public static final String NUMBER = "number";

    public TreeGridContainer() {
        addContainerProperty(TreeGridContainer.NUMBER, String.class, "");
        addContainerProperty(TreeGridContainer.STATUS, String.class, "");
        addContainerProperty(TreeGridContainer.LAST_MODIFIED, Date.class, new Date());
        addContainerProperty(TreeGridContainer.DEACTIVATED, Boolean.class, Boolean.TRUE);

        for (final Object[] r : DataSource.getRoot()) {
            addItem(r);
        }
    }

    private Object addItem(final Object[] values) {
        final Item item = addItem((Object) values);
        setProperties(item, values);
        return values;
    }

    private Object addChild(final Object[] values, final Object parentId) {
        final Item item = addItemAfter(parentId, values);
        setProperties(item, values);
        setParent(values, parentId);
        return values;
    }

    @SuppressWarnings("unchecked")
    private void setProperties(final Item item, final Object[] values) {
        item.getItemProperty(TreeGridContainer.NUMBER)
                .setValue(values[0].toString());
        item.getItemProperty(TreeGridContainer.STATUS)
                .setValue(values[1]);
        item.getItemProperty(TreeGridContainer.LAST_MODIFIED)
                .setValue(values[2]);
        item.getItemProperty(TreeGridContainer.DEACTIVATED)
                .setValue(values[3]);
    }

    @Override
    public int getDepth(Object itemId) {
        int depth = 0;
        while (!isRoot(itemId)) {
            depth++;
            itemId = getParent(itemId);
        }
        return depth;
    }

    @Override
    public void setCollapsed(final Object itemId, final boolean collapsed) {
        this.expandedNodes.put(itemId, !collapsed);

        if (collapsed) {
            // remove children
            removeChildrenRecursively(itemId);
        }
        else {
            // lazy load children
            addChildren(itemId);
        }
    }

    private void addChildren(final Object itemId) {
        for (final Object[] child : DataSource.getChildren(itemId)) {
            final Object childId = addChild(child, itemId);
            if (Boolean.TRUE.equals(this.expandedNodes.get(childId))) {
                addChildren(childId);
            }
        }
    }

    private boolean removeChildrenRecursively(final Object itemId) {
        boolean success = true;
        final Collection<?> children2 = getChildren(itemId);
        if (children2 != null) {
            final Object[] array = children2.toArray();
            for (final Object element : array) {
                final boolean removeItemRecursively = HierarchicalContainer.removeItemRecursively(this, element);
                if (!removeItemRecursively) {
                    success = false;
                }
            }
        }
        return success;

    }

    @Override
    public boolean hasChildren(final Object itemId) {
        return !DataSource.isLeaf(itemId);
    }

    private final Map<Object, Boolean> expandedNodes = new HashMap<>();

    @Override
    public boolean isCollapsed(final Object itemId) {
        return !Boolean.TRUE.equals(this.expandedNodes.get(itemId));
    }

}
