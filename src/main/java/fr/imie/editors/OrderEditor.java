package fr.imie.editors;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import fr.imie.entity.Customer;
import fr.imie.entity.Order;
import fr.imie.repository.CustomerRepository;
import fr.imie.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tlemaillet on 6/29/16.
 */
@SpringComponent
@UIScope
public class OrderEditor extends VerticalLayout {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    /**
     * The currently edited order
     */
    private Order order;

    /* Fields to edit properties in Order entity */
    private ComboBox customers;


    /* Action buttons */
    private Button save = new Button("Save", FontAwesome.SAVE);
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", FontAwesome.TRASH_O);
    private CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public OrderEditor(OrderRepository repository, CustomerRepository customerRepository) {
        this.orderRepository = repository;
        this.customerRepository = customerRepository;

        this.customers = new ComboBox("Customer", this.customerRepository.findAll());
        customers.setNullSelectionAllowed(false);
        customers.setImmediate(true);

        addComponents(customers, actions);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> {

            order.setCustomer((Customer) customers.getValue());
            orderRepository.save(order);
        });
        delete.addClickListener(e -> orderRepository.delete(order));
        cancel.addClickListener(e -> editOrder(order));
        setVisible(false);
    }

    public interface ChangeHandler {

        void onChange();
    }

    public final void editOrder(Order c) {
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            order = orderRepository.findOne(c.getId());
        }
        else {
            order = c;
        }
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(order, this);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
    }

    public void setChangeHandler(OrderEditor.ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}
