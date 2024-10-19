package spg.company.views;



import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.vaadin.lineawesome.LineAwesomeIcon;
import spg.company.CompanyApplication;
import spg.company.components.UIFactory;
import spg.company.views.company.CompanyEditView;
import spg.company.views.employee.EmployeeCreateView;
import spg.company.views.employee.EmployeesUnemployedListView;
import spg.company.views.example.EmployeeListJpqlConcat;
import spg.company.views.example.EmployeeListSpringRepo;

public class MainLayout extends AppLayout {

    private final Div viewTitleContainer = new Div();


    // -- CONSTRUCTOR --------------------------------------------------------------------------------------------------

    public MainLayout() {
        initUI();
    }


    // -- INIT ---------------------------------------------------------------------------------------------------------

    private void initUI() {
        setPrimarySection(Section.DRAWER);


        // -- DRAWER HEADER -----------------------------

        H1 appName = new H1(CompanyApplication.APP_TITLE);
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);


        // -- DRAWER NAVBAR ------------------------------

        Div nav = UIFactory.appNav();
        nav.add(
                UIFactory.createNavItem(CompanyEditView.class, LineAwesomeIcon.HOME_SOLID.create()),
                UIFactory.createNavItem(EmployeeCreateView.class, LineAwesomeIcon.USER_PLUS_SOLID.create()),
                UIFactory.createNavItem(EmployeesUnemployedListView.class, LineAwesomeIcon.LIST_SOLID.create()),
                new Hr(),
                UIFactory.createNavItem( EmployeeListSpringRepo.class, LineAwesomeIcon.LIST_SOLID.create()),
                UIFactory.createNavItem( EmployeeListJpqlConcat.class, LineAwesomeIcon.LIST_SOLID.create())
        );


        // -- MENU TOGGLE  -------------------------------

        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");


        // -- PUT IT TOGETHER ----------------------------

        addToDrawer(header);
        addToDrawer(new Scroller(nav));
        addToDrawer(new Footer());
        addToNavbar(true, toggle, viewTitleContainer);
    }


    // -- READ BREADCRUMB ----------------------------------------------------------------------------------------------

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        String[] elements = getCurrentPageTitle().split("\\|");
        viewTitleContainer.removeAll();
        int length = elements.length;

        // Create Breadcrumb e.g: Employee > Edit - set css arrow between elements
        for (int i = 0; i < length; i++) {
            viewTitleContainer.add(elements[i]);
            if (i < length - 1) {
                Span arrow = new Span();
                arrow.addClassNames("arrow", "arrow-right");
                viewTitleContainer.add(arrow);
            }
        }
    }


    // -- SET TITLE ----------------------------------------------------------------------------------------------------

    private String getCurrentPageTitle() {

        // extract dynamic titles
        Component content = getContent();
        if (content instanceof HasDynamicTitle dynamicTitle) {
            return dynamicTitle.getPageTitle();
        }

        // extract annotated titles
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
