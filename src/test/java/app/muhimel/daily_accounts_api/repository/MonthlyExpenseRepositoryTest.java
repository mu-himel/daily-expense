package app.muhimel.daily_accounts_api.repository;

import app.muhimel.daily_accounts_api.entity.MonthlyExpense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class MonthlyExpenseRepositoryTest {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER= new PostgreSQLContainer("postgres:latest");

    @Autowired
    private MonthlyExpenseRepository monthlyExpenseRepository;

    @Test
    void test_FindByNumberOfMonthAndYearAndReturnWithMonthlyExpenseObject() {
        monthlyExpenseRepository.save(new MonthlyExpense(11,2026,new BigDecimal(10000)));
        MonthlyExpense monthlyExpense = monthlyExpenseRepository.findByNumberOfMonthAndYear(11,2026);
        assertThat(monthlyExpense).isNotNull();
        assertThat(monthlyExpense.getYear()).isEqualTo(2026);
        assertThat(monthlyExpense.getNumberOfMonth()).isEqualTo(11);
        assertThat(monthlyExpense.getBudgetAmount()).isEqualTo(new BigDecimal(10000));
    }

    @Test
    void test_FindByYearAndReturnListOfMonthlyExpense() {
        monthlyExpenseRepository.save(new MonthlyExpense(11,2026,new BigDecimal(10000)));
        List<MonthlyExpense> monthlyExpense = monthlyExpenseRepository.findByYear(2026);
        assertThat(monthlyExpense).isNotNull();
        assertThat(monthlyExpense.size()).isEqualTo(1);

        assertThat(monthlyExpense.get(0).getYear()).isEqualTo(2026);
        assertThat(monthlyExpense.get(0).getNumberOfMonth()).isEqualTo(11);
        assertThat(monthlyExpense.get(0).getBudgetAmount()).isEqualTo(new BigDecimal(10000));
    }
}