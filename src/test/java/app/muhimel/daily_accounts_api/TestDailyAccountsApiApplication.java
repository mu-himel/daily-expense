package app.muhimel.daily_accounts_api;

import org.springframework.boot.SpringApplication;

public class TestDailyAccountsApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(DailyAccountsApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
