package fr.imie.views;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import fr.imie.editors.CustomerEditor;
import fr.imie.entity.Customer;
import fr.imie.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Created by tlemaillet on 6/24/16.
 */
public class ProductView extends VerticalLayout implements View{
    public static final String VIEW_NAME = "customer";

    private final CustomerRepository repo;
    private final CustomerEditor editor;
    private final Grid grid;
    private final TextField filter;
    private final Button addNewBtn;

    @Autowired
    public ProductView(CustomerRepository repo, CustomerEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid();
        this.filter = new TextField();
        this.addNewBtn = new Button("New customer", FontAwesome.PLUS);
    }

    @PostConstruct
    void init() {
        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);

        // Configure layouts and components
        actions.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);


        grid.setHeight(300, Sizeable.Unit.PIXELS);
        grid.setWidth(100, Sizeable.Unit.PERCENTAGE);
        //grid.setWidthUndefined();
        //grid.setColumns("id", "firstName", "lastName");
        //grid.setFrozenColumnCount(4);


        filter.setInputPrompt("Filter by Name");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.addTextChangeListener(e -> listCustomers(e.getText()));

        // Connect selected Customer to editor or hide if none is selected
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                editor.setVisible(false);
            }
            else {
                editor.editCustomer((Customer) grid.getSelectedRow());
            }
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(
                new Customer("Mister Bean", "33 rue des potirons", "23065",
                        "Rennes", "mbean@gmail.com", null, "0123456789")
        ));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        // Initialize listing
        listCustomers(null);

        setMargin(true);
        setSpacing(true);
        addComponent(mainLayout);
    }

    // tag::listCustomers[]
    private void listCustomers(String text) {
        if (StringUtils.isEmpty(text)) {
            grid.setContainerDataSource(
                    new BeanItemContainer(Customer.class, repo.findAll()));
        }
        else {
            grid.setContainerDataSource(new BeanItemContainer(Customer.class,
                    repo.findByNameStartsWithIgnoreCase(text)));
        }
    }
    // end::listCustomers[]

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // the view is constructed in the init() method()
    }
}
