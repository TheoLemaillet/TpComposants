package fr.imie.views;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import fr.imie.editors.OrderDetailEditor;
import fr.imie.entity.OrderDetail;
import fr.imie.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by tlemaillet on 6/30/16.
 */
@ViewScope
@SpringView(name = OrderDetailView.VIEW_NAME)
public class OrderDetailView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "orderdetail";

    private final OrderDetailRepository repo;
    private final OrderDetailEditor editor;
    private final Grid grid;
    private final TextField filter;
    private final Button addNewBtn;

    @Autowired
    public OrderDetailView(OrderDetailRepository repo, OrderDetailEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid();
        this.filter = new TextField();
        this.addNewBtn = new Button("New order", FontAwesome.PLUS);
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


        filter.setInputPrompt("Filter by Ref");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.addTextChangeListener(e -> listOrderDetail(e.getText()));

        // Connect selected Customer to editor or hide if none is selected
        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                editor.setVisible(false);
            }
            else {
                editor.editOrderDetail((OrderDetail) grid.getSelectedRow());
            }
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editOrderDetail(new OrderDetail(0, null, null)));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listOrderDetail(filter.getValue());
        });

        // Initialize listing
        listOrderDetail(null);

        setMargin(true);
        setSpacing(true);
        addComponent(mainLayout);
    }

    // tag::listOrders[]
    private void listOrderDetail(String text) {
        if (StringUtils.isEmpty(text)) {
            grid.setContainerDataSource(
                    new BeanItemContainer(OrderDetail.class, repo.findAll()));
        }
        else {
            grid.setContainerDataSource(new BeanItemContainer(OrderDetail.class,
                    repo.findByRef(text)));
        }
    }
    // end::listOrders[]

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // the view is constructed in the init() method()
    }
}
