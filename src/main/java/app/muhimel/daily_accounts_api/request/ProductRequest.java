package app.muhimel.daily_accounts_api.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {

    @NotEmpty(message = "Name is Required")
    private String name;

    @NotEmpty(message = "unit is Required")
    private String unit;
}
