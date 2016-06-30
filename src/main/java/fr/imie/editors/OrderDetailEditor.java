package fr.imie.editors;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import fr.imie.entity.Order;
import fr.imie.entity.OrderDetail;
import fr.imie.entity.Product;
import fr.imie.repository.OrderDetailRepository;
import fr.imie.repository.OrderRepository;
import fr.imie.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tlemaillet on 6/30/16.
 */
@SpringComponent
@UIScope
public class OrderDetailEditor extends VerticalLayout {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * The currently edited orderDetail
     */
    private OrderDetail orderDetail;

    /* Fields to edit properties in Order entity */
    private TextField qte = new TextField("Quantite");;
    private ComboBox products;
    private ComboBox orders;


    /* Action buttons */
    private Button save = new Button("Save", FontAwesome.SAVE);
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", FontAwesome.TRASH_O);
    private CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public OrderDetailEditor(OrderDetailRepository repository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderDetailRepository = repository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;

        this.orders = new ComboBox("Order", this.orderRepository.findAll());
        orders.setNullSelectionAllowed(false);
        orders.setImmediate(true);

        this.products = new ComboBox("Product", this.productRepository.findAll());
        products.setNullSelectionAllowed(false);
        products.setImmediate(true);

        addComponents(qte, orders, products, actions);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> {
            orderDetail.setOrder((Order) orders.getValue());
            orderDetail.setProduct((Product) products.getValue());
            orderDetailRepository.save(orderDetail);
        });
        delete.addClickListener(e -> orderDetailRepository.delete(orderDetail));
        cancel.addClickListener(e -> editOrderDetail(orderDetail));
        setVisible(false);
    }

    public interface ChangeHandler {

        void onChange();
    }

    public final void editOrderDetail(OrderDetail c) {
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            orderDetail = orderDetailRepository.findOne(c.getId());
        }
        else {
            orderDetail = c;
        }
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(orderDetail, this);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
    }

    public void setChangeHandler(OrderDetailEditor.ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}
