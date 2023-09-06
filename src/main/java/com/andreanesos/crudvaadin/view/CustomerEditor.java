package com.andreanesos.crudvaadin.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.andreanesos.crudvaadin.Customer;
import com.andreanesos.crudvaadin.CustomerRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout implements KeyNotifier{
   
    private static final long serialVersionUID = 1L;

    private final CustomerRepository customerRepository;

    private Customer customer;
    
    //fields to edit propreitrs in customer entity
    //per binding automatico lasciare con stesso nome del customer class
    TextField firstName;
    TextField lastName;
    
    //action buttons
    Button saveButton;
    Button cancelButton;
    Button deleteButton;
    HorizontalLayout actionsButtons;
    
    Binder<Customer> binder;
    private ChangeHandler changeHandler;
    
    @Autowired
    public CustomerEditor(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.firstName = new TextField("First name");
        this.lastName = new TextField("Last name");
        
        //action buttons
        this.saveButton = new Button("Save", VaadinIcon.CHECK.create());
        this.cancelButton = new Button("Cancel");
        this.deleteButton = new Button("Delete", VaadinIcon.TRASH.create());
        this.actionsButtons = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        
        this.binder = new Binder<>(Customer.class);
        
        //setup layout
        add(firstName, lastName, actionsButtons);
        
        //automatic bind using namingconvections
        binder.bindInstanceFields(this);
        
     // Configure and style components
        setSpacing(true);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        addKeyPressListener(Key.ENTER, e -> save());
        
     // wire action buttons to save, delete and reset
        saveButton.addClickListener(e -> save());
        deleteButton.addClickListener(e -> delete());
        cancelButton.addClickListener(e -> editCustomer(customer));
        
        setVisible(false);
    }
    
    
    void delete() {
        customerRepository.delete(customer);
        changeHandler.onChange();
    }
    
    void save() {
        customerRepository.save(customer);
        changeHandler.onChange();
    }
    
    
    public final void editCustomer(Customer customer) {
        if (customer == null) {
            setVisible(false);
            return;
        }
        
        final boolean persisted = customer.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            // In a more complex app, you might want to load
            // the entity/DTO with lazy loaded relations for editing
            this.customer = customerRepository.findById(customer.getId()).get();
        }
        else {
            this.customer = customer;
        }
        cancelButton.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(customer);

        setVisible(true);

        // Focus first name initially
        firstName.focus();        
    }
    
    //change handler beetween mainview and customer editor
    public interface ChangeHandler {
        void onChange();
    }
    
    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }
}
