package fr.imie.views;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import fr.imie.editors.InvoiceEditor;
import fr.imie.entity.Invoice;
import fr.imie.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by tlemaillet on 6/30/16.
 */
@ViewScope
@SpringView(name = InvoiceView.VIEW_NAME)
public class InvoiceView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "invoice";

    private final InvoiceRepository repo;
    private final InvoiceEditor editor;
    private final Grid grid;
    private final TextField filter;
    private final Button addNewBtn;

    @Autowired
    public InvoiceView(InvoiceRepository repo, InvoiceEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid();
        this.filter = new TextField();
        this.addNewBtn = new Button("New invoice", FontAwesome.PLUS);
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


        filter.setInputPrompt("Filter by Reference");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.addTextChangeListener(e -> listInvoice(e.getText()));

        // Connect selected Customer to editor or hide if none is selected
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                editor.setVisible(false);
            }
            else {
                editor.editInvoice((Invoice) grid.getSelectedRow());
            }
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editInvoice(
                new Invoice(new Date(), null)
        ));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listInvoice(filter.getValue());
        });

        // Initialize listing
        listInvoice(null);

        setMargin(true);
        setSpacing(true);
        addComponent(mainLayout);
    }

    // tag::listInvoices[]
    private void listInvoice(String text) {
        if (StringUtils.isEmpty(text)) {
            grid.setContainerDataSource(
                    new BeanItemContainer(Invoice.class, repo.findAll()));
        }
        else {
            grid.setContainerDataSource(new BeanItemContainer(Invoice.class,
                    repo.findByRef(text)));
        }
    }
    // end::listInvoices[]

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // the view is constructed in the init() method()
    }
}
