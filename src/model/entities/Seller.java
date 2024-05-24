package model.entities;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Seller implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String email;
    private Date birthDate;
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private Double baseSalary;
    private Department department;

    public Seller() {}

    public Seller(Integer id, String name, String email, Date birthDate, Double baseSalary, Department department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.baseSalary = baseSalary;
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return Objects.equals(getId(), seller.getId()) &&
                Objects.equals(getName(), seller.getName()) &&
                Objects.equals(getEmail(), seller.getEmail()) &&
                Objects.equals(getBirthDate(), seller.getBirthDate()) &&
                Objects.equals(getBaseSalary(), seller.getBaseSalary()) &&
                Objects.equals(getDepartment(), seller.getDepartment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getBirthDate(), getBaseSalary(), getDepartment());
    }

    @Override
    public String toString() {
        return STR."Seller: ID = \{id}, Name = \{name}, Email = \{email}, Birth Date = \{birthDate}, Base Salary = \{baseSalary} and \{department.toString()}";
    }
}
