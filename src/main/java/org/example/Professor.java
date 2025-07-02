package org.example;

import jakarta.persistence.*;

@Entity
@Table(name="professor")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id ;

    @Column(name="office_number")
    private int officeNumber;
    @Column(name="research_area")
    private int researchArea;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn (name = "customer_id")
    private Customer customer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(int officeNumber) {
        this.officeNumber = officeNumber;
    }

    public int getResearchArea() {
        return researchArea;
    }

    public void setResearchArea(int researchArea) {
        this.researchArea = researchArea;
    }
}
