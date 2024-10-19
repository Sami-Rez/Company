package spg.company.components.grid;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import spg.company.components.UIFactory;
import spg.company.components.button.TableButton;
import spg.company.data.entity.Employee;
import spg.company.data.service.CompanyService;

import java.util.Arrays;

import static org.vaadin.lineawesome.LineAwesomeIcon.SEARCH_SOLID;

public abstract class AbstractEmployeeGrid extends VerticalLayout {
    private final TextField searchField = new TextField();
    private final Grid<Employee> grid = new Grid<>(Employee.class, false);
    private final CompanyService service;
    private final VerticalLayout headerContainer = new VerticalLayout();
    private boolean dataDirty = true;

    // -- CONSTRUCTOR --------------------------------------------------------------------------------------------------

    protected AbstractEmployeeGrid(CompanyService service) {
        this.service = service;
        init();
    }


    // -- ABSTRACT STUFF -----------------------------------------------------------------------------------------------

    protected abstract CallbackDataProvider.FetchCallback<Employee, Void> reload(
            CompanyService service, String keyword);
    protected abstract CallbackDataProvider.FetchCallback<Employee, Void> load(CompanyService service);

    protected abstract CallbackDataProvider.CountCallback<Employee, Void> count();



    // -- INIT ---------------------------------------------------------------------------------------------------------

    private void init() {
        addClassName("page-view");
        setSizeFull();

        // -- HEADER -----------------
        headerContainer.setVisible( false );
        headerContainer.setPadding(false);
        headerContainer.setMargin(false);

        // -- SEARCH -----------------
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search by first- and lastname");
        searchField.setPrefixComponent(SEARCH_SOLID.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.getStyle().set("margin-bottom", "0");
        searchField.addValueChangeListener( this::onValueChanged );

        // -- GRID ------------------
        grid.addClassName("spg-grid");
        grid.setMaxHeight("80vh");
        grid.setPageSize( 65 );
        UIFactory.tableProfileColumn( grid );
        grid.addColumns( Employee.GRID_COLS );
        grid.addThemeVariants( GridVariant.LUMO_NO_BORDER );

        // initial load data
        load();

        add( headerContainer );
        add( searchField );
        add( grid );
    }

    // -- ADD/GET/SET --------------------------------------------------------------------------------------------------

    protected CompanyService service() {
        return service;
    }

    public void addHeader(Component header) {
        if(header == null)
            return;

        headerContainer.add( header );
        headerContainer.setVisible(true);
    }

    @SafeVarargs
    public final void addTableActions(TableButton<Employee>... actions) {
        if(actions == null)
            return;

        grid.addComponentColumn( employee -> UIFactory.tableColumnAction(
                Arrays.stream(actions)
                        .map( tbtn -> tbtn.toButton( employee ))
                        .toArray( Component[]::new )
        ));
    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------

    private void onValueChanged(AbstractField.ComponentValueChangeEvent<TextField, String> e) {

        String keyword = e.getValue();

        // Muss alles geladen werden - dataDirty == true ? Aber nur wenn keyword null oder blank und < 3
        if( dataDirty && (keyword == null || keyword.isBlank() || keyword.length() < 3 )) {
            load();
            return;
        }

        dataDirty = true;
        grid.setItems( reload( service, keyword ) );
    }

    private void load() {
        if( dataDirty) {
            dataDirty = false;    // avoid multiple reload
            grid.setItems( load( service ) );
        }
    }

    protected void refresh() {
        dataDirty = false;
        grid.getLazyDataView().setItemCountCallback( count() );
        grid.setItems( reload(service, searchField.getValue()) );
    }

}
