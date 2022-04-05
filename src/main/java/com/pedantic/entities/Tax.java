package com.pedantic.entities;


import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;


import java.math.BigDecimal;

@Entity
//@Table(name = "IRS_SALARY_TAX_TABLE")
//changes name of id to tax_id id is in parent class AbstractEntity
@AttributeOverride(name = "id", column = @Column(name = "tax_id"))
public class Tax extends AbstractEntity {
	//changes the column name to TAX_RATE
    @Column(name = "TAX_RATE")
    private BigDecimal taxRate;


    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}
