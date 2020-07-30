package org.codeworld;

import javax.servlet.annotation.WebServlet;

import org.codeworld.treegrid.TreeGridContainer;
import org.vaadin.treegrid.TreeGrid;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be overridden to add component to the user interface and initialize
 * non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        final TreeGrid grid = new TreeGrid();
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        TreeGridContainer mainContainer = new TreeGridContainer();
        grid.setContainerDataSource(mainContainer);
        grid.setHierarchyColumn(TreeGridContainer.NUMBER);
        grid.setColumnReorderingAllowed(true);

        layout.addComponents(grid);
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*",
            name = "MyUIServlet",
            asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class,
            productionMode = false,
            widgetset = "org.vaadin.treegrid.demo.DemoWidgetSet")
    public static class MyUIServlet extends VaadinServlet {
    }
}
