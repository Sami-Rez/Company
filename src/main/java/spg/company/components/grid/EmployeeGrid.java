package spg.company.components.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import spg.company.components.UIFactory;
import spg.company.data.entity.Employee;

import java.util.function.Consumer;

import static org.vaadin.lineawesome.LineAwesomeIcon.USER_EDIT_SOLID;
import static org.vaadin.lineawesome.LineAwesomeIcon.USER_SLASH_SOLID;

public class EmployeeGrid extends Grid<Employee> {

    public EmployeeGrid (Consumer<Employee> onEdit, Consumer<Employee> onDelete) {
        super(Employee.class, false);
        initUI(onEdit, onDelete);
    }

    public void initUI(Consumer<Employee> onEdit, Consumer<Employee> onDelete) {
        setMaxHeight("80vh");
        this.setPageSize(100);
        this.addClassName("spg-grid");
        UIFactory.tableProfileColumn( this );
        this.addColumns( Employee.GRID_COLS );
        this.addThemeVariants( GridVariant.LUMO_NO_BORDER );
        UIFactory.tableActionColumn(this, employee -> {
            Button add = UIFactory.tableBtn( USER_EDIT_SOLID.create(), e -> onEdit.accept( employee ) );
            Button remove = UIFactory.tableBtn( USER_SLASH_SOLID.create(), e -> onDelete.accept( employee ) );
            return UIFactory.tableColumnAction( add, remove );
        });
    }

}
