package spg.company.views.company;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import spg.company.CompanyApplication;
import spg.company.components.EmployeeDialog;
import spg.company.components.NavTitle;
import spg.company.components.UIFactory;
import spg.company.data.entity.Company;
import spg.company.data.entity.Employee;
import spg.company.data.service.CompanyService;
import spg.company.views.MainLayout;

import java.util.Set;

import static org.vaadin.lineawesome.LineAwesomeIcon.LIST_SOLID;
import static org.vaadin.lineawesome.LineAwesomeIcon.USER_CHECK_SOLID;

@NavTitle("Companies")
@PageTitle("Companies | Edit")
@Route(value = ":id?", layout = MainLayout.class)
@RoutePrefix(value = "companies")
public class CompanyEditView extends VerticalLayout implements BeforeEnterObserver {

    private final ComboBox<Company> companySelection = new ComboBox<>("Company");
    private final TextField address = new TextField("Address");
    private final TextField name = new TextField("Name");

    private final Section companyFormContainer = new Section();
    private final Button save = UIFactory.btnPrimary("Save", e -> onSave());
    private final Button cancel = UIFactory.btn("Cancel", e -> onCancel());
    private final Button openEmployeeDialog = UIFactory.btnTertiary("Hire", USER_CHECK_SOLID.create(), e -> onDialogShow());
    private final Button showEmployees = UIFactory.btnTertiary("Show Employees", LIST_SOLID.create(), e -> onShowEmployees());

    private final Binder<Company> binder = new BeanValidationBinder<>(Company.class);


    private Company company;

    // injected stuff
    private final CompanyService service;
    private final EmployeeDialog employeeDialog;


    // -- CONSTRUCTOR --------------------------------------------------------------------------------------------------

    public CompanyEditView(CompanyService service, EmployeeDialog employeeDialog) {
        this.service = service;
        this.employeeDialog = employeeDialog;
        initUI();
    }


    // -- OPTIONAL PARAMETER -------------------------------------------------------------------------------------------

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().getLong("id")
                .flatMap( service::findCompanyById )
                .ifPresent( this::setCompany );
    }


    // -- INIT ---------------------------------------------------------------------------------------------------------

    private void initUI() {
        addClassName("page-view");
        setSizeFull();


        // -- COMBO-BOS -----------------------------------

        companySelection.setItems( service::findAllCompanies );
        companySelection.setItemLabelGenerator( Company::getName );
        companySelection.setHelperText("Select a company");
        companySelection.setWidthFull();
        companySelection.addValueChangeListener( e -> onCompanyChanged( e.getValue() ) );


        // -- EDIT FORM -----------------------------------

        FormLayout formLayout = new FormLayout();
        formLayout.add(address, 2);
        formLayout.add(name, 2);

        companyFormContainer.add( UIFactory.headerActionPanel("Edit Company",
                "Edit company, show employees for company or hire new employees.",
                showEmployees, openEmployeeDialog) );
        companyFormContainer.add( formLayout );
        companyFormContainer.add( UIFactory.buttonLayout(save, cancel) );
        companyFormContainer.setVisible( false );
        companyFormContainer.setSizeFull();


        // -- PUT ALL TOGETHER ----------------------------

        add( UIFactory.headerActionPanel("Companies", "Please select a company to edit."));
        add( companySelection );
        add( new Hr() );
        add( companyFormContainer );


        // -- BINDING -------------------------------------

        binder.bindInstanceFields(this);


        employeeDialog.initUI("Unemployed Employees", e -> onDialogHire());
    }


    // -- GETTER/SETTER ------------------------------------------------------------------------------------------------


    private void setCompany(Company company) {
        this.company = company;
        binder.setBean( company );

        boolean companyAvailable = this.company != null;
        companyFormContainer.setVisible( companyAvailable );

        if (companyAvailable && !company.equals( companySelection.getValue() )) {
            companySelection.setValue( company );
        }
    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------

    private void onSave() {
        binder.validate();
        if (binder.isValid()) {
            Company company = binder.getBean();
            service.update(company);
            CompanyApplication.success(company.getName() + " updated");
        } else {
            CompanyApplication.warn("Data not valid. Please check your data.");
        }
    }

    private void onCancel() {
        setCompany(company);
    }


    // -- COMBO-BOX ------------

    private void onCompanyChanged(Company company) {
        setCompany( company );
    }


    // -- DIALOG ---------------

    private void onDialogShow() {
        if (company == null) {
            CompanyApplication.warn("Please select a company first");
            return;
        }

        employeeDialog.setItems( service::findUnemployedEmployees );
        employeeDialog.addKeywordChangeListener( e -> search( e.getValue() ) );
        employeeDialog.onShow();
    }

    private void search(String keyword) {
        employeeDialog.setItems( q -> service.findUnemployedEmployees( keyword, q ) );
    }

    private void onDialogHire() {
        if (company == null) {
            CompanyApplication.warn("No company available!");
            return;
        }
        Set<Employee> selection = employeeDialog.getSelectedItems();
        int size = selection.size();
        int hired = service.hire( company.getId(), selection );
        CompanyApplication.success(size + "/" + hired + (hired == 1 ? " Employee hired" : " Employees hired"));
        getUI().ifPresent(ui -> ui.navigate(
                CompanyEmployeesListView.class,
                UIFactory.paramOf("id", company.getId()))
        );
    }

    private void onShowEmployees() {

        System.out.println("Helloe");

        getUI().ifPresent(ui -> ui.navigate(CompanyEmployeesListView.class,
                UIFactory.paramOf("id", company.getId())));
    }
}
