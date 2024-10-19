package spg.company.views.employee;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import spg.company.CompanyApplication;
import spg.company.components.NavTitle;
import spg.company.components.UIFactory;
import spg.company.components.grid.EmployeeGrid;
import spg.company.data.entity.Employee;
import spg.company.data.service.CompanyService;
import spg.company.views.MainLayout;

import static org.vaadin.lineawesome.LineAwesomeIcon.SEARCH_SOLID;
import static org.vaadin.lineawesome.LineAwesomeIcon.USER_PLUS_SOLID;

@NavTitle("Employees")
@PageTitle(value = "Employees | Unemployed")
@Route(value = "employees", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class EmployeesUnemployedListView extends VerticalLayout {

    private final Button addEmployee = UIFactory.btnTertiary("Create", USER_PLUS_SOLID.create(), e -> onCreate());

    private final TextField searchField = new TextField();
    private final EmployeeGrid grid = new EmployeeGrid(this::onEdit, this::onDelete);
    private final CompanyService service;
    private final Text employeeCounter = new Text("");


    // -- CONSTRUCTOR --------------------------------------------------------------------------------------------------

    public EmployeesUnemployedListView(CompanyService service) {
        this.service = service;
        initUI();
    }


    // -- INIT ---------------------------------------------------------------------------------------------------------

    private void initUI() {
        addClassName("page-view");
        setSizeFull();


        // -- HEADER -----------------
        int employeesTotal = service.countUnemployedEmployees();
        employeeCounter.setText(employeesTotal + " unemployed employees.");
        Component header = UIFactory.headerActionPanel("Employees", employeeCounter, addEmployee);
        header.getStyle().set("with", "100%");

        // -- SEARCH -----------------
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search by first- and lastname");
        searchField.setPrefixComponent(SEARCH_SOLID.create());
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(e -> reload(e.getValue()));

        // -- GRID -------------------
        grid.setItems( service::findUnemployedEmployees );

        add( header );
        add( searchField );
        add( grid );
    }

    private void reload(String keyword) {
        grid.setItems(q -> service.findUnemployedEmployees(keyword, q));
    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------

    private void onCreate() {
        getUI().ifPresent(ui -> ui.navigate(EmployeeCreateView.class));
    }

    private void onEdit(Employee employee) {
        if (employee == null) {
            CompanyApplication.error("Employee must not be null!");
            return;
        }
        final Long employeeId = employee.getId();
        getUI().ifPresent(ui -> ui.navigate(EmployeeEditView.class, UIFactory.paramOf("id", employeeId)));
    }

    private void onDelete(Employee employee) {
        try {
            service.delete(employee);
            grid.getDataProvider().refreshAll();

            int employeesTotal = service.countUnemployedEmployees();
            employeeCounter.setText(employeesTotal + " unemployed employees.");

            CompanyApplication.info("Employee " + employee.getLastName() + " deleted.");
        } catch (Exception e) {
            CompanyApplication.error(e.getMessage());
        }
    }
}
