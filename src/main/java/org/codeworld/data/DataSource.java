package org.codeworld.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codeworld.model.StyleTreeData;
import org.codeworld.model.StyleTreeRow;

import com.google.common.collect.Lists;

public class DataSource {
    private static List<Object[]> rootNodes = new ArrayList<>();

    private static Map<Object, List<Object[]>> children = new HashMap<>();

    private static Set<Object[]> leaves = new HashSet<>();
    private static Long counter = 1L;

    static {
        DataSource.populateWithRandomHierarchicalData();
    }

    public static List<Object[]> getRoot() {
        return DataSource.rootNodes;
    }

    public static List<Object[]> getChildren(final Object parent) {
        return DataSource.children.get(parent);
    }

    public static boolean isLeaf(final Object itemId) {
        return DataSource.leaves.contains(itemId);
    }

    private static void populateWithRandomHierarchicalData() {
        final StyleTreeData styleData = DataSource.createTestData();
        final StyleTreeRow treeRow = styleData.getTreeRow();
        final Object[] parentRow = new Object[] { treeRow.getNumber(), treeRow.getStatus(), treeRow.getLastModified(), treeRow.getDeactivated() };
        for (final StyleTreeRow childTreeRow : styleData.getChildrens()) {
            final Object[] childRow = new Object[] { childTreeRow.getNumber(), childTreeRow.getStatus(), childTreeRow.getLastModified(),
                    childTreeRow.getDeactivated() };
            DataSource.leaves.add(childRow);
            DataSource.addChild(parentRow, childRow);
        }

        DataSource.rootNodes.add(parentRow);
    }

    private static void addChild(final Object parent, final Object[] child) {
        if (!DataSource.children.containsKey(parent)) {
            DataSource.children.put(parent, new ArrayList<Object[]>());
        }
        DataSource.children.get(parent)
                .add(child);
    }

    public static StyleTreeData createTestData() {

        final StyleTreeRow parent = StyleTreeRow.builder()
                .id(counter++)
                .number("S001")
                .lastModified(new Date())
                .status("STYLE_CREATED")
                .deactivated(true)
                .build();

        final StyleTreeRow child1 = StyleTreeRow.builder()
                .id(counter++)
                .number("I001")
                .lastModified(new Date())
                .status("ITEM_CREATED")
                .deactivated(true)
                .build();

        final StyleTreeRow child2 = StyleTreeRow.builder()
                .id(counter++)
                .number("I002")
                .lastModified(new Date())
                .status("ITEM_OK")
                .deactivated(true)
                .build();

        return StyleTreeData.builder()
                .id(counter++)
                .treeRow(parent)
                .childrens(Lists.newArrayList(child1, child2))
                .build();
    }
}
