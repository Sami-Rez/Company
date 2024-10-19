package spg.company;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import spg.company.components.UIFactory;
import spg.company.data.repository.CompanyRepository;

import javax.sql.DataSource;

@SpringBootApplication
@NpmPackage(value = "@fontsource/roboto", version = "4.5.0")
@Theme(value = "spg-enterprises")

public class CompanyApplication implements AppShellConfigurator {
	//    @Value("${default.avatar.src}")
	public static final String DEFAULT_AVATAR_SRC = "images/default.png";
	public static final String APP_TITLE = "SPG-Enterprises";
	public static final String UPLOAD_PATH = "data/images/";

	public static void main(String[] args) {
		SpringApplication.run(CompanyApplication.class, args);
	}

	@Profile("default")
	@Bean
	SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
																			   SqlInitializationProperties properties, CompanyRepository repository) {

		return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties) {
			@Override
			public boolean initializeDatabase() {
				if (repository.count() == 0L)
					return super.initializeDatabase();
				return false;
			}
		};
	}

	public static void info(String text) {
		UIFactory.notify("Info", text, UIFactory.NotifyType.INFO).open();
	}

	public static void success(String text) {
		success("Success", text);
	}

	public static void success(String header, String text) {
		UIFactory.notify(header, text, UIFactory.NotifyType.SUCCESS).open();
	}

	public static void warn(String text) {
		UIFactory.notify("Warn", text, UIFactory.NotifyType.WARN).open();
	}

	public static void error(String text) {
		UIFactory.notify("Error", text, UIFactory.NotifyType.ERROR).open();

	}
}