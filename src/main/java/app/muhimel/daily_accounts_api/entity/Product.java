package app.muhimel.daily_accounts_api.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@AllArgsConstructor
public class Product extends BaseEntity{


    private String name;
    private String unit;

    Product(Long id, String name, String unit){
        this.setId(id);
        this.name = name;
        this.unit = unit;
    }
}
