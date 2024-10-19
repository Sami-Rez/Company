package spg.company.views.employee;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;
import spg.company.CompanyApplication;
import spg.company.components.UIFactory;
import spg.company.components.form.EmployeeForm;
import spg.company.data.entity.Employee;
import spg.company.data.service.CompanyService;
import spg.company.views.MainLayout;

@PageTitle(value = "Employees | edit")
@Route(value = ":id", layout = MainLayout.class)
@RoutePrefix(value = "employees")
public class EmployeeEditView extends Div implements BeforeEnterObserver {

    private final Button cancel = UIFactory.btn("Cancel", e -> onCancel() );
    private final Button save = UIFactory.btnPrimary("Save", e -> onSave() );

    // injected stuff
    private final CompanyService service;
    private final EmployeeForm employeeForm;


    // -- CONSTRUCTOR --------------------------------------------------------------------------------------------------

    public EmployeeEditView(CompanyService service, EmployeeForm employeeForm) {
        this.service = service;
        this.employeeForm = employeeForm;
        initUI();
    }


    // -- PARAMS -------------------------------------------------------------------------------------------------------

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().getLong( "id").ifPresent( this::setParameter );
    }

    private void setParameter(Long employeeId) {
        service.findEmployeeById( employeeId ).ifPresentOrElse(
                employeeForm::setEmployee,
                () -> { CompanyApplication.error( "No employee found." ); onCancel(); }
        );
    }


    // -- INIT ---------------------------------------------------------------------------------------------------------

    private void initUI() {
        addClassName("page-view");
        setSizeFull();
        Component header = UIFactory.headerActionPanel( "Employee", "Edit current employee." );
        add( header );
        add( employeeForm );
        add( UIFactory.buttonLayout( save, cancel ) );
    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------

    private void onCancel() {
        getUI().ifPresent( ui -> ui.getPage().getHistory().back() );
    }

    private void onSave() {
        if( employeeForm.isValid() ) {
            Employee employee = employeeForm.getEmployee();
            service.update( employee );
            CompanyApplication.info(employee.getLastName() + " updated.");
            getUI().ifPresent( ui -> ui.navigate( EmployeesUnemployedListView.class ) );
        } else {
            CompanyApplication.warn("Invalid Data. Pleas check form data");
        }
    }
}
