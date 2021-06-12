package vn.nhantd.mycareer.model.user;

import java.io.Serializable;

public class Salary implements Serializable {
    private String currency_unit = "";
    private Long from_salary = 0L;
    private Long to_salary = 0L;

    // Standard getters & setters
    public String getCurrency_unit() {
        return currency_unit;
    }

    public void setCurrency_unit(String currency_unit) {
        this.currency_unit = currency_unit;
    }

    public Long getFrom_salary() {
        return from_salary;
    }

    public void setFrom_salary(Long from_salary) {
        this.from_salary = from_salary;
    }

    public Long getTo_salary() {
        return to_salary;
    }

    public void setTo_salary(Long to_salary) {
        this.to_salary = to_salary;
    }

    @Override
    public String toString() {
        if (currency_unit.equals("Thỏa thuận"))
            return currency_unit;
        return from_salary + " - " + to_salary + " " + currency_unit;
    }
}
