package entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int quantity;
    private double subTotalPrice = 0;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private ItemType itemType;

    public OrderLine(int quantity, ItemType itemTypes) {
        this.quantity = quantity;
        this.itemType = itemTypes;
    }

    public OrderLine() {}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubTotalPrice() {
        return subTotalPrice;
    }

    public void setSubTotalPrice(double subTotalPrice) {
        this.subTotalPrice = subTotalPrice;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemTypes) {
        this.itemType = itemTypes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}