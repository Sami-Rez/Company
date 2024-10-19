package spg.company.views.company;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import spg.company.CompanyApplication;
import spg.company.components.UIFactory;
import spg.company.components.grid.EmployeeGrid;
import spg.company.data.entity.Company;
import spg.company.data.entity.Employee;
import spg.company.data.service.CompanyService;
import spg.company.views.MainLayout;
import spg.company.views.employee.EmployeeCreateView;
import spg.company.views.employee.EmployeeEditView;

import static org.vaadin.lineawesome.LineAwesomeIcon.CARET_LEFT_SOLID;
import static org.vaadin.lineawesome.LineAwesomeIcon.USER_PLUS_SOLID;

// DYNAMIC PAGE TITLE -> Interface HasDynamicTitle
@Route(value = "employees/:id", layout = MainLayout.class)
@RoutePrefix(value = "companies")
public class CompanyEmployeesListView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

    private static final Runnable NO_DATA = () -> CompanyApplication.warn("No data found");
    private final Button createEmployee = UIFactory.btnTertiary("Create", USER_PLUS_SOLID.create(), e -> onCreate());
    private final Button back = UIFactory.btnTertiary("Back", CARET_LEFT_SOLID.create(), e -> onBack());

    private final EmployeeGrid grid = new EmployeeGrid( this::onEdit, this::onFire);

    private final CompanyService service;
    private Company company;


    // -- CONSTRUCTOR --------------------------------------------------------------------------------------------------

    public CompanyEmployeesListView(CompanyService service) {
        this.service = service;
    }


    // -- READ PARAMS --------------------------------------------------------------------------------------------------

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().getLong("id").ifPresentOrElse(
                this::setCompanyId,
                NO_DATA
        );
    }


    // -- INIT ---------------------------------------------------------------------------------------------------------

    private void initUI() {
        addClassName("page-view");
        setSizeFull();


        // -- HEADER -----------------
        RouterLink companyLink = new RouterLink( company.getName(), CompanyEditView.class,
                UIFactory.paramOf("id", company.getId() ));
        companyLink.addClassName("router-link");

        Span description = new Span( new Text( "All employees for company "), companyLink );
        Component header = UIFactory.headerActionPanel("Employees", description, back, createEmployee);


        // -- GRID -------------------


        add( header );
        add( grid );
    }

    // -- SET COMPANY --------------------------------------------------------------------------------------------------

    private void setCompanyId(Long companyId) {
        service.findCompanyById( companyId ).ifPresentOrElse(c -> {
            company = c;
            initUI();
            grid.setItems(q -> service.findEmployeesByCompanyId( companyId, q));
        }, NO_DATA);
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
        getUI().ifPresent(ui -> ui.navigate(EmployeeEditView.class, UIFactory.paramOf("id", employee.getId() )));
    }

    private void onFire(Employee employee) {
        try {
            service.fire( employee );
            grid.getDataProvider().refreshAll();
            CompanyApplication.info("Employee " + employee.getLastName() + " fired.");
        } catch (Exception e) {
            CompanyApplication.error( e.getMessage() );
        }
    }

    private void onBack() {
        getUI().ifPresent(ui -> ui.navigate(CompanyEditView.class, UIFactory.paramOf("id", company.getId())));
    }


    // -- DYNAMIC TITLE ------------------------------------------------------------------------------------------------

    @Override
    public String getPageTitle() {
        if(company == null)
            return "Company | Employees";
        return company.getName() + " | Employees";
    }
}
