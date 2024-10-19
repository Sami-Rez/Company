package spg.company.components.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spg.company.components.UIFactory;
import spg.company.data.entity.Employee;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmployeeCardList extends Grid<Employee> {

    @PostConstruct
    private void initUI() {
        setSizeFull();
        setHeight("100%");
        setSizeFull();

        this.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        this.addComponentColumn( this::createCard );
        this.setSelectionMode( Grid.SelectionMode.MULTI );
        this.addItemClickListener( this::onItemClick );
    }

    private HorizontalLayout createCard(Employee employee) {
        String imageUrl = employee.getImageUrl();
        String fullName = employee.getLastName() + " " + employee.getFirstName();
        String mail = employee.getEmail();
        String role = employee.getRole().name();

        HorizontalLayout cardLayout = new HorizontalLayout();
        cardLayout.addClassName("card");
        cardLayout.setAlignItems( FlexComponent.Alignment.CENTER );

        Image avatar = UIFactory.loadImage( fullName, imageUrl );

        Span mailContainer = new Span( mail );
        mailContainer.getStyle().set("font-size", "var(--lumo-font-size-s)");
        mailContainer.getStyle().set("color", "var(--lumo-secondary-text-color)");
        Span nameContainer = new Span( fullName );
        nameContainer.getStyle().set("font-weight", "bold");

        Span roleContainer = new Span( role );
        roleContainer.setHeightFull();
        roleContainer.getElement().getThemeList().add("badge success");
        cardLayout.setAlignSelf( FlexComponent.Alignment.CENTER, roleContainer );
        VerticalLayout dataLayout = new VerticalLayout( nameContainer, mailContainer);
        dataLayout.setSpacing(false);
        dataLayout.setPadding(false);
        dataLayout.setMargin(false);
        cardLayout.add( avatar, dataLayout, roleContainer);
        cardLayout.setPadding(false);
        cardLayout.setSpacing(false);
        cardLayout.setMargin(false);

        return cardLayout;
    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------

    private void onItemClick(ItemClickEvent<Employee> event) {
        this.select( event.getItem() );
    }

}
