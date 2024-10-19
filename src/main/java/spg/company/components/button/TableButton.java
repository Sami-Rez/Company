package spg.company.components.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import io.micrometer.common.util.StringUtils;
import org.vaadin.lineawesome.LineAwesomeIcon;

public class TableButton<T> {
    private String text;
    private LineAwesomeIcon icon;
    private final spg.company.components.button.TableEventListener<T> listener;

    public TableButton(LineAwesomeIcon icon, spg.company.components.button.TableEventListener<T> listener) {
        this.icon = icon;
        this.listener = listener;
    }

    public TableButton(String text, spg.company.components.button.TableEventListener<T> listener) {
        this.text = text;
        this.listener = listener;
    }

    public String getText() {
        return text;
    }

    public LineAwesomeIcon getIcon() {
        return icon;
    }

    public Button toButton(T entity) {
        // Muss für jedes Entity erzeugt werden! Jede row hat eigenen Button!

        Button button = new Button();
        if(!StringUtils.isBlank( text ))
            button.setText( text );

        if(icon != null)
            button.setIcon( icon.create() );

        button.addClickListener( e -> fireEvent( e, entity )  );
        button.setClassName("table-button");
        button.addThemeVariants( ButtonVariant.LUMO_TERTIARY );
        return button;
    }


    private void fireEvent(ClickEvent<Button> event, T entity) {
        listener.onTableEvent( event, entity );
    }

}
