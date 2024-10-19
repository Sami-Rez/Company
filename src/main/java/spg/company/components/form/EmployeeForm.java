package spg.company.components.form;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import spg.company.CompanyApplication;
import spg.company.components.UIFactory;
import spg.company.data.entity.Employee;
import spg.company.data.entity.Role;

import java.io.File;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmployeeForm extends Div {

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final EmailField email =  new EmailField("Email address");
    private final TextField phone =  new TextField("Phone Number");
    private final DatePicker dateOfBirth =  new DatePicker("Birthday");
    private final ComboBox<Role> role = new ComboBox<>("Role");
    private final TextField salary = new TextField("Salary");
    private final Checkbox subcontractor = new Checkbox("Subcontractor");
    private final TextField imageUrl = new TextField("Image");
    private final Image image = new Image();

    private final Binder<Employee> binder = new BeanValidationBinder<>( Employee.class );

    private Upload upload;



    // -- INIT ---------------------------------------------------------------------------------------------------------
    @PostConstruct  // example postConstruct - wird automatisch nach dem Aufruf des Konstruktors aufgerufen
    private void init() {
        role.setItems( Role.values() );

        // -- UPLOAD --------------------------------------

        FileBuffer fileBuffer = new FileBuffer(fileName -> new File(CompanyApplication.UPLOAD_PATH, fileName).getCanonicalFile());
        upload = new Upload( fileBuffer );
        upload.addSucceededListener( this::onUploadSuccess );
        upload.setWidthFull();

        image.setMaxHeight("100px");
        image.getStyle().set("margin-right", "10px");

        HorizontalLayout profileLayout = new HorizontalLayout( image, upload );
        profileLayout.setAlignItems( FlexComponent.Alignment.CENTER );
        profileLayout.setVerticalComponentAlignment( FlexComponent.Alignment.CENTER );
        imageUrl.setEnabled( false );


        // -- FORM ----------------------------------------

        FormLayout formLayout = new FormLayout();
        formLayout.add(
                firstName, lastName,
                email, phone,
                dateOfBirth, role,
                salary, imageUrl
        );
        formLayout.setColspan(imageUrl, 2);
        formLayout.add(subcontractor, imageUrl);


        // -- ADD -----------------------------------------

        add( profileLayout );
        add( new Hr() );
        add( formLayout );


        // -- BINDER --------------------------------------

        binder.bindInstanceFields(this);
    }


    // -- GETTER/SETTER ------------------------------------------------------------------------------------------------

    public void setEmployee(Employee employee) {
        if(employee == null) {
            CompanyApplication.error("Employee must not be null!");
            return;
        }
        binder.setBean( employee );
        setImage( employee.getLastName(), employee.getImageUrl() );
    }

    private void setImage(String name, String imageSrc) {
        UIFactory.loadImageSrc( name, imageSrc, image );
        imageUrl.setValue( imageSrc );
    }

    public Employee getEmployee() {
        return binder.getBean();
    }

    public boolean isValid() {
        binder.validate();
        return binder.isValid();
    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------

    private void onUploadSuccess(SucceededEvent event) {
        String fileName = event.getFileName();
        String imageSrc = CompanyApplication.UPLOAD_PATH + fileName;
        String name = lastName.getValue();
        setImage( name, imageSrc );
    }

    public void refresh() {
        binder.readBean( new Employee() );
        upload.clearFileList();
        image.setSrc( CompanyApplication.DEFAULT_AVATAR_SRC );
    }


}
