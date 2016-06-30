package fr.imie.editors;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import fr.imie.entity.Invoice;
import fr.imie.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by tlemaillet on 6/30/16.
 */
@SpringComponent
@UIScope
public class InvoiceEditor extends VerticalLayout{
    private final InvoiceRepository invoiceRepository;

    /**
     * The currently edited invoice
     */
    private Invoice invoice;

    /* Fields to edit properties in Customer entity */
    private DateField date = new DateField("Invoice Date");

    /* Action buttons */
    private Button save = new Button("Save", FontAwesome.SAVE);
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", FontAwesome.TRASH_O);
    private CssLayout actions = new CssLayout(save, cancel, delete);

    @Autowired
    public InvoiceEditor(InvoiceRepository repository) {
        this.invoiceRepository = repository;

        addComponents(date, actions);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> invoiceRepository.save(invoice));
        delete.addClickListener(e -> invoiceRepository.delete(invoice));
        cancel.addClickListener(e -> editInvoice(invoice));
        setVisible(false);
    }

    public interface ChangeHandler {

        void onChange();
    }

    public final void editInvoice(Invoice c) {
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            invoice = invoiceRepository.findOne(c.getId());
        }
        else {
            invoice = c;
        }
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        BeanFieldGroup.bindFieldsUnbuffered(invoice, this);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
    }

    public void setChangeHandler(InvoiceEditor.ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}
