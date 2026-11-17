package app.muhimel.daily_accounts_api.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    public interface Create extends Default {
    }

    public interface Update extends Default {
    }

    @Null(groups = Create.class, message = "Id must not be provided when creating a product")
    @NotNull(groups = Update.class, message = "Id is Required")
    private Integer id;

    @NotEmpty(message = "Name is Required")
    private String name;

    @NotEmpty(message = "unit is Required")
    private String unit;
}
