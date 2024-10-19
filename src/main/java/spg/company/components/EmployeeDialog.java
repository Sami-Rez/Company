package spg.company.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spg.company.components.grid.EmployeeCardList;
import spg.company.data.entity.Employee;

import java.util.Set;

import static org.vaadin.lineawesome.LineAwesomeIcon.SEARCH_SOLID;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmployeeDialog extends Dialog {
    private final TextField searchField = new TextField();

    private final H5 footerInfo = new H5("0 selected");
    private final Button cancel = UIFactory.btn("Cancel", e -> onCancel());
    // private final Button hire = UIFactory.btnPrimary("Hire");
    private final Button hire = UIFactory.btnPrimary("Hire");

    private final EmployeeCardList cardList;

    // -- CONSTRUCTOR --------------------------------------------------------------------------------------------------

    public EmployeeDialog(EmployeeCardList cardList) {
        this.cardList = cardList;
    }


    // -- INIT ---------------------------------------------------------------------------------------------------------

    public void initUI(String title, ComponentEventListener<ClickEvent<Button>> listener) {
        setHeaderTitle( title );
        addThemeVariants( DialogVariant.LUMO_NO_PADDING );
        setHeight("90vh");
        setMaxWidth("1024px");
        setMinWidth("70vw");


        // -- EMPLOYEE CARD LIST --------------------------------
        hire.addClickListener( buttonClickEvent -> {
            if(isOpened())
                close();
        });

        // -- SEARCH --------------------------------------------
        searchField.setVisible( false );    // visible if listener is set
        searchField.setWidth("80%");
        searchField.setPlaceholder("Search by first- and lastname");
        searchField.setPrefixComponent( SEARCH_SOLID.create() );
        searchField.setValueChangeMode( ValueChangeMode.LAZY );
        searchField.getStyle().set("margin-left", "10%");
        searchField.getStyle().set("margin-right", "10%");
        searchField.getStyle().set("padding-bottom", "10px");


        // -- LISTENER ------------------------------------------
        cardList.addSelectionListener( this::onSelect );
        cardList.setHeightFull();

        hire.addClickListener( listener );

        add( searchField );
        add( cardList );
        getFooter().add( UIFactory.footerPanel( footerInfo, hire, cancel ) );
    }


    // -- GETTER/SETTER ------------------------------------------------------------------------------------------------

    public void setItems(CallbackDataProvider.FetchCallback<Employee, Void> fetchCallback) {
        cardList.setItems( fetchCallback );
    }

    public Set<Employee> getSelectedItems() {
        return cardList.getSelectedItems();
    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------

    public void onShow() {
        if(!isOpened())
            open();
    }

    public void onCancel() {
        cardList.deselectAll();
        if(isOpened())
            close();
    }

    private void onSelect(SelectionEvent<Grid<Employee>, Employee> event) {
        int size = event.getAllSelectedItems().size();
        footerInfo.setText( size + " selected" );
    }


    public void addKeywordChangeListener(HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>> listener) {
        searchField.setVisible( true );
        searchField.addValueChangeListener( listener );
    }

}
