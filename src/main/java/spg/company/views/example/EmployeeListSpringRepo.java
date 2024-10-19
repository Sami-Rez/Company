package spg.company.views.example;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import spg.company.components.NavTitle;
import spg.company.components.UIFactory;
import spg.company.components.button.TableButton;
import spg.company.components.grid.AbstractEmployeeGrid;
import spg.company.data.entity.Employee;
import spg.company.data.service.CompanyService;
import spg.company.views.MainLayout;
import spg.company.views.employee.EmployeeEditView;

@PageTitle("List Spring")
@NavTitle("Example Spring")
@Route(value = "list/spring-repo", layout = MainLayout.class)
public class EmployeeListSpringRepo extends AbstractEmployeeGrid {

    public EmployeeListSpringRepo(CompanyService service) {
        super(service);
    }

    @PostConstruct
    private void initUI() {
        addHeader(
                UIFactory.headerActionPanel("Example Spring", "Search with spring-repository",
                        new Button("Test", e -> doSomething()))
        );
        addTableActions(
                new TableButton<>("Edit", this::onEdit),
                new TableButton<>("Delete", this::onDelete)
        );
    }

    private void onEdit(ClickEvent<Button> event, Employee employee) {
        getUI().ifPresent(ui -> ui.navigate(EmployeeEditView.class, UIFactory.paramOf("id", employee.getId())));
    }

    private void onDelete(ClickEvent<Button> event, Employee employee) {
        service().delete(employee);
        refresh();
    }

    @Override
    protected CallbackDataProvider.FetchCallback<Employee, Void> reload(CompanyService service, String keyword) {
        return q -> service.searchSpringRepoMethod(keyword, q);
    }

    //    @Override
    protected CallbackDataProvider.FetchCallback<Employee, Void> load(CompanyService service) {
        return query -> service().findUnemployedEmployees(query);
    }

    @Override
    protected CallbackDataProvider.CountCallback<Employee, Void> count() {
        return q -> service().countUnemployedEmployees();
    }

    private void doSomething() {
        System.out.println("Something done");
    }

}
