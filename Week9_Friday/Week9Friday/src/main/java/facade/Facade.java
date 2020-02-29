package facade;

import entities.Customer;
import entities.ItemType;
import entities.Order;
import entities.OrderLine;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class Facade {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("pu");
    private static EntityManager em;

    public Customer createCustomer(String name, String email) {
        Customer result = new Customer(name, email);
        em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(result);
            em.getTransaction().commit();
            return result;
        } finally {
            em.close();
        }
    }

    public Customer findCustomer(Integer id) {
        em = EMF.createEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public List<Customer> getAllCustomer() {
        em = EMF.createEntityManager();
        try {
            TypedQuery tq = em.createQuery("SELECT c FROM Customer c", Customer.class);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    public ItemType createItemType(String name, String description, double price) {
        ItemType result = new ItemType(name, description, price);
        em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(result);
            em.getTransaction().commit();
            return result;
        } finally {
            em.close();
        }
    }

    public ItemType findItemType(Integer id) {
        em = EMF.createEntityManager();
        try {
            return em.find(ItemType.class, id);
        } finally {
            em.close();
        }
    }

    public Order findOrder(Integer id) {
        em = EMF.createEntityManager();
        try {
            return em.find(Order.class, id);
        } finally {
            em.close();
        }
    }

    public Order createOrder(Integer customerID) {
        em = EMF.createEntityManager();
        try {
            Customer customer = em.find(Customer.class, customerID);
            Order result = new Order(customer);
            customer.addOrder(result);
            em.getTransaction().begin();
            em.persist(result);
            em.getTransaction().commit();
            return result;
        } finally {
            em.close();
        }
    }

    public OrderLine createOrderLine(int quantity, ItemType itemType, Order order) {
        em = EMF.createEntityManager();
        OrderLine result = new OrderLine(quantity, itemType);
        order.addOrderLine(result);
        try {
            em.getTransaction().begin();
            em.persist(result);
            em.getTransaction().commit();
            return result;
        } finally {
            em.close();
        }
    }

    public OrderLine createOrderLine(int quantity, Integer itemTypeID, Integer orderID) {
        Order order = findOrder(orderID);
        ItemType itemType = findItemType(itemTypeID);
        return createOrderLine(quantity, itemType, order);
    }
}