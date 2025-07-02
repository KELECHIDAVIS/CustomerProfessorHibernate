package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import jdbc.ConnectionFactory;

// **Couldn't maven or imports working so just followed the structure from Create Student best I could

public class MainCreate {
    public static void main(String[] args) {
        String type = "hibernate";

        if (type.equals("jdbc")) {

            try (Connection connection = ConnectionFactory.getConnection()) {

                connection.setAutoCommit(false);

                Customer customer = new Customer("Acme Industries", "123 Main St.");
                PreparedStatement custStmt = connection.prepareStatement(
                        "INSERT INTO customer (customer_name, address) VALUES (?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);

                custStmt.setString(1, customer.getName());
                custStmt.setString(2, customer.getAddress());
                custStmt.executeUpdate();

                try (var rs = custStmt.getGeneratedKeys()) {
                    if (rs.next()) customer.setId(rs.getInt(1));
                }

                Professor professor = new Professor(customer, /*office#*/ 101, /*research area*/ 42);
                PreparedStatement profStmt = connection.prepareStatement(
                        "INSERT INTO professor (office_number, research_area, customer_id) VALUES (?, ?, ?)");

                profStmt.setInt(1, professor.getOfficeNumber());
                profStmt.setInt(2, professor.getResearchArea());
                profStmt.setInt(3, customer.getId());

                profStmt.executeUpdate();

                connection.commit();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        else if (type.equals("hibernate")) {


            SessionFactory factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Professor.class)
                    .buildSessionFactory();

            try (factory) {                          // Java‑9 “try‑with‑resource” closes factory automatically
                Session session = factory.getCurrentSession();


                Customer customer  = new Customer("Globex Corp.", "742 Evergreen Terrace");
                Professor professor = new Professor(customer, /*office#*/ 12, /*research area*/ 314);


                session.beginTransaction();


                session.save(professor);


                session.getTransaction().commit();
                System.out.println("Hibernate insert committed!");
            }
        }

        System.out.println("Finished!");

    }
    }
}
