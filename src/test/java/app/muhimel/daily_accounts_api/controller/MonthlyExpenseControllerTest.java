package app.muhimel.daily_accounts_api.controller;

import app.muhimel.daily_accounts_api.dto.MonthlyExpenseResponse;
import app.muhimel.daily_accounts_api.request.MonthlyExpenseRequest;
import app.muhimel.daily_accounts_api.service.MonthlyExpenseService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MonthlyExpenseController.class)
class MonthlyExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MonthlyExpenseService monthlyExpenseService;

    @Test
    void test_AddMonthlyExpenseReturnsCreatedAndDelegatesToService() throws Exception {
        mockMvc.perform(post("/api/v1/monthly-expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"month":9,"year":2026,"budgetAmount":5000.50}
                        """)
        ).andExpect(status().isCreated());

        ArgumentCaptor<MonthlyExpenseRequest> captor = ArgumentCaptor.forClass(MonthlyExpenseRequest.class);
        verify(monthlyExpenseService).createMonthlyExpense(captor.capture());

        assertThat(captor.getValue().getMonth()).isEqualTo(9);
        assertThat(captor.getValue().getYear()).isEqualTo(2026);
        assertThat(captor.getValue().getBudgetAmount()).isEqualByComparingTo("5000.50");
        verifyNoMoreInteractions(monthlyExpenseService);
    }

    @Test
    void test_AddMonthlyExpenseReturnsBadRequestWhenMonthIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/monthly-expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"year":2026,"budgetAmount":5000.50}
                        """)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(monthlyExpenseService);
    }

    @Test
    void test_AddMonthlyExpenseReturnsBadRequestWhenYearIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/monthly-expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"month":9,"budgetAmount":5000.50}
                        """)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(monthlyExpenseService);
    }

    @Test
    void test_AddMonthlyExpenseReturnsBadRequestWhenBudgetAmountIsMissing() throws Exception {
        mockMvc.perform(post("/api/v1/monthly-expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"month":9,"year":2026}
                        """)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(monthlyExpenseService);
    }

    @Test
    void test_GetMonthlyExpensesReturnsExpensesForGivenYear() throws Exception {
        List<MonthlyExpenseResponse> expenses = List.of(
                new MonthlyExpenseResponse(1, 2026, new BigDecimal("15000.00")),
                new MonthlyExpenseResponse(2, 2026, new BigDecimal("12000.00"))
        );
        when(monthlyExpenseService.getAllMonthlyExpensesByYear(2026)).thenReturn(expenses);

        mockMvc.perform(get("/api/v1/monthly-expenses")
                .param("year", "2026")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].numberOfMonth").value(1))
        .andExpect(jsonPath("$[0].year").value(2026))
        .andExpect(jsonPath("$[0].budgetAmount").value(15000.00))
        .andExpect(jsonPath("$[1].numberOfMonth").value(2))
        .andExpect(jsonPath("$[1].year").value(2026))
        .andExpect(jsonPath("$[1].budgetAmount").value(12000.00));

        verify(monthlyExpenseService).getAllMonthlyExpensesByYear(2026);
        verifyNoMoreInteractions(monthlyExpenseService);
    }

    @Test
    void test_GetMonthlyExpensesReturnsBadRequestWhenYearIsMissing() throws Exception {
        mockMvc.perform(get("/api/v1/monthly-expenses"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(monthlyExpenseService);
    }
}
