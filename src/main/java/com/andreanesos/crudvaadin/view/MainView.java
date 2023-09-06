package com.andreanesos.crudvaadin.view;

import org.springframework.util.StringUtils;

import com.andreanesos.crudvaadin.Customer;
import com.andreanesos.crudvaadin.CustomerRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout{
    
    private static final long serialVersionUID = 1L;

    private final CustomerRepository customerRepository;
    
    private final TextField filterTextField;
    private final Button newCustomerButton;
    
    private final Grid<Customer> grid;
    private final CustomerEditor customerEditor;
    private final HorizontalLayout actionsButtons;
    
    //che injecti arriva dal framework
    //però l'ho dichiarato sopra una variabile per usarla in altri metodi
    public MainView(CustomerRepository customerRepository, CustomerEditor customerEditor) {
        this.customerRepository = customerRepository;
        this.customerEditor = customerEditor;
        this.grid = new Grid<>(Customer.class);
        this.filterTextField = new TextField();
        this.newCustomerButton = new Button("New customer", VaadinIcon.PLUS.create());
        this.actionsButtons = new HorizontalLayout(filterTextField, newCustomerButton);
        
        setupLayout();
    }
    
    
    private void setupLayout() {
        
        // Replace listing with filtered content when user changes filter
        filterTextField.setPlaceholder("Filter by last name");
        filterTextField.setValueChangeMode(ValueChangeMode.LAZY);
        filterTextField.addValueChangeListener(e -> setCustomersToGrid(e.getValue()));
        
        newCustomerButton.addClickListener(e -> customerEditor.editCustomer(new Customer("", "")));
        
        //Connect selected Customer to editor or hide if none is selected
        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "lastName");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        grid.asSingleSelect().addValueChangeListener(e -> {
                                    customerEditor.editCustomer(e.getValue());
                                                    });
        setCustomersToGrid("");    // Initialize list customer

        add(actionsButtons, grid, customerEditor);
        
        // Listen changes made by the editor and refresh data from backend
        customerEditor.setChangeHandler(() -> {
            customerEditor.setVisible(false);
            setCustomersToGrid(filterTextField.getValue());
        });
        
        
        
    }


    private void setCustomersToGrid(String filterText) {
        if(StringUtils.hasText(filterText)) {
            grid.setItems(customerRepository.findByLastNameStartsWithIgnoreCase(filterText));
        }else {
            grid.setItems(customerRepository.findAll());
        }
    }
        
        /*
        //customers grid
        this.grid = new Grid<Customer>(Customer.class);
        setCustomersToGrid("");
        grid.setColumns("id", "firstName", "lastName");
        add(grid);
        
        //filter text field
        TextField filter = new TextField();
        filter.setPlaceholder("Insert last name to filter");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> setCustomersToGrid(e.getValue()));
        add(filter, grid);
        */
    
    
    
    
}
