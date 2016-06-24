package fr.imie.gui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import fr.imie.entity.Customer;
import fr.imie.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Created by tlemaillet on 6/23/16.
 */

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

    private CustomerRepository repo;
    private Grid grid;

    @Autowired
    public VaadinUI(CustomerRepository repo) {
        this.repo = repo;
        this.grid = new Grid();
    }

    @Override
    protected void init(VaadinRequest request) {
        TextField filter = new TextField();
        filter.setInputPrompt("Filter by name");
        filter.addTextChangeListener(e -> listCustomers(e.getText()));
        VerticalLayout mainLayout = new VerticalLayout(filter, grid);
        setContent(mainLayout);
    }

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
}
