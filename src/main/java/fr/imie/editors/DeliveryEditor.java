package fr.imie.editors;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import fr.imie.entity.Delivery;
import fr.imie.entity.Invoice;
import fr.imie.entity.Order;
import fr.imie.repository.DeliveryRepository;
import fr.imie.repository.InvoiceRepository;
import fr.imie.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tlemaillet on 6/30/16.
 */
@SpringComponent
@UIScope
public class DeliveryEditor extends VerticalLayout {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;

    /**
     * The currently edited delivery
     */
    private Delivery delivery;

    /* Fields to edit properties in Order entity */
    private DateField date = new DateField("Delivery Date");;
    private ComboBox invoices;
    private ComboBox orders;


    /* Action buttons */
    private Button save = new Button("Save", FontAwesome.SAVE);
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", FontAwesome.TRASH_O);
    private CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public DeliveryEditor(DeliveryRepository repository, OrderRepository orderRepository, InvoiceRepository invoiceRepository) {
        this.deliveryRepository = repository;
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;

        this.orders = new ComboBox("Order", this.orderRepository.findAll());
        orders.setNullSelectionAllowed(false);
        orders.setImmediate(true);

        this.invoices = new ComboBox("Invoice", this.invoiceRepository.findAll());
        invoices.setNullSelectionAllowed(false);
        invoices.setImmediate(true);

        addComponents(date, orders, invoices, actions);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> {
            delivery.setOrder((Order) orders.getValue());
            delivery.setInvoice((Invoice) invoices.getValue());
            deliveryRepository.save(delivery);
        });
        delete.addClickListener(e -> deliveryRepository.delete(delivery));
        cancel.addClickListener(e -> editDelivery(delivery));
        setVisible(false);
    }

    public interface ChangeHandler {

        void onChange();
    }

    public final void editDelivery(Delivery c) {
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            delivery = deliveryRepository.findOne(c.getId());
        }
        else {
            delivery = c;
        }
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(delivery, this);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
    }

    public void setChangeHandler(DeliveryEditor.ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}
