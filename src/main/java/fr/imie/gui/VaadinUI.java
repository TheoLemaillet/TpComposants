package fr.imie.gui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import fr.imie.editors.CustomerEditor;
import fr.imie.entity.Customer;
import fr.imie.entity.Order;
import fr.imie.repository.CustomerRepository;
import fr.imie.views.CustomerView;
import fr.imie.views.DefaultView;
import fr.imie.views.UIScopedView;
import fr.imie.views.ViewScopedView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlemaillet on 6/23/16.
 */

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

    private final CustomerRepository repo;
    private final CustomerEditor editor;
    private final Grid grid;
    private final TextField filter;
    private final Button addNewBtn;

    // we can use either constructor autowiring or field autowiring
    @Autowired
    private SpringViewProvider viewProvider;


    @Autowired
    public VaadinUI(CustomerRepository repo, CustomerEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid();
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", FontAwesome.PLUS);
    }

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponent(createNavigationButton("Home",
                DefaultView.VIEW_NAME));
        navigationBar.addComponent(createNavigationButton("Customers",
                CustomerView.VIEW_NAME));
        root.addComponent(navigationBar);

        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0f);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);
    }

    private Button createNavigationButton(String caption, final String viewName) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        // If you didn't choose Java 8 when creating the project, convert this to an anonymous listener class
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }


}
