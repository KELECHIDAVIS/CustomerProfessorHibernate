package org.example;

public class MainDelete {
    public static void main(String[] args) {

        String type = "hibernate";

        if (type.equals("jdbc")) {

            try (Connection connection = ConnectionFactory.getConnection()) {

                int professorId = 1;

                PreparedStatement stmt = connection.prepareStatement(
                        "SELECT p.id AS prof_id, p.office_number, p.research_area, p.customer_id, " +
                                "c.id AS cust_id, c.customer_name, c.address " +
                                "FROM professor p JOIN customer c ON p.customer_id = c.id WHERE p.id = ?");

                stmt.setInt(1, professorId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {

                    Customer customer = new Customer(rs.getString("customer_name"), rs.getString("address"));
                    customer.setId(rs.getInt("cust_id"));

                    Professor professor = new Professor(customer, rs.getInt("office_number"), rs.getInt("research_area"));
                    professor.setId(rs.getInt("prof_id"));

                    connection.setAutoCommit(false);

                    stmt = connection.prepareStatement("DELETE FROM professor WHERE id = ?");
                    stmt.setInt(1, professor.getId());
                    stmt.executeUpdate();

                    stmt = connection.prepareStatement("DELETE FROM customer WHERE id = ?");
                    stmt.setInt(1, customer.getId());
                    stmt.executeUpdate();

                    connection.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type.equals("hibernate")) {

            SessionFactory factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Professor.class)
                    .buildSessionFactory();

            try (factory) {
                Session session = factory.getCurrentSession();
                int professorId = 2;
                session.beginTransaction();
                Professor professor = session.get(Professor.class, professorId);
                if (professor != null) {
                    session.delete(professor);
                }
                session.getTransaction().commit();
            }
        }
    }
}
