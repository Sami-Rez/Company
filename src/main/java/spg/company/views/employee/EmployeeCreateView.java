package spg.company.views.employee;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.hibernate.service.spi.InjectService;
import spg.company.CompanyApplication;
import spg.company.components.NavTitle;
import spg.company.components.UIFactory;
import spg.company.components.form.EmployeeForm;
import spg.company.data.entity.Employee;
import spg.company.data.service.CompanyService;
import spg.company.views.MainLayout;

@NavTitle("Employee")
@PageTitle("Employees | Create")
@Route(value = "create", layout = MainLayout.class)
@RoutePrefix("employee")
public class EmployeeCreateView extends VerticalLayout {

    private final Button cancel = UIFactory.btn("Cancel", e -> onCancel() );
    private final Button save = UIFactory.btnPrimary("Save", e -> onSave() );

    // Injected stuff
    private final EmployeeForm employeeForm;
    private final CompanyService service;


    // -- CONSTRUCTOR --------------------------------------------------------------------------------------------------

    public EmployeeCreateView(CompanyService service, EmployeeForm employeeForm) {
        this.service = service;
        this.employeeForm = employeeForm;
        initUI();
    }


    // -- INIT ---------------------------------------------------------------------------------------------------------

    private void initUI() {
        addClassName("page-view");
        Component header = UIFactory.headerActionPanel("Employee", "Create a new, unemployed employee.");
        employeeForm.setEmployee( new Employee() );

        add( header );
        add( employeeForm );
        add( UIFactory.buttonLayout( save, cancel ) );

    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------

    private void onCancel() {
        employeeForm.refresh();
    }

    private void onSave() {
        if( employeeForm.isValid() ) {
            Employee employee = employeeForm.getEmployee();
            service.update( employee );
            CompanyApplication.info(employee.getLastName() + " created.");
            employeeForm.refresh();
        } else {
            CompanyApplication.warn("Invalid Data. Pleas check form data");
        }
    }
}
