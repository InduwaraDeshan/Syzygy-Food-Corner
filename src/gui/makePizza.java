/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

/**
 *
 * @author Deshan
 */
import static gui.Home.tPrice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// Flyweight Pattern
class IngredientFactory {

    private IngredientFactory() {
        
    }

    private static final HashMap<String, IngredientFactory> ingredientsPOOL = new HashMap<>();

    public static IngredientFactory createIngredient(String ingredient) {

        IngredientFactory instance = ingredientsPOOL.get(ingredient);

        if (instance == null) {
            instance = new IngredientFactory();
            instance.ingredient = ingredient;
            ingredientsPOOL.put(ingredient, instance);
        }
        return instance;
    }
    private String ingredient;

    public String getIngredient() {
        return ingredient;
    }

    public IngredientFactory setIngredient(String ingredient) {
        return IngredientFactory.createIngredient(ingredient);
    }

}

// Interpreter Pattern
interface Command {

    void execute(Pizza pizza);
}

class DefaultPizzaCommand implements Command {

    private String topping;

    public DefaultPizzaCommand(String topping) {
        this.topping = topping;
    }

    @Override
    public void execute(Pizza pizza) {
        pizza = new Pizza.Builder()
                .setType(pizza.getType())
                .setSize(pizza.getSize())
                .setBasePrice(pizza.getBasePrice())
                .addAllToppings(pizza.getToppings()) 
                .build();
        pizza.display(); 
    }
}

class CustomizePizzaCommand implements Command {

    private String type;
    private String size;
    private List<String> toppings;

    public CustomizePizzaCommand(String type, String size, List<String> toppings) {
        this.type = type;
        this.size = size;
        this.toppings = toppings;
    }

    @Override
    public void execute(Pizza pizza) {
        pizza = new Pizza.Builder()
                .setType(type)
                .setSize(size)
                .setBasePrice(pizza.getBasePrice()) 
                .addAllToppings(toppings) 
                .build();
        pizza.display(); 
    }
}

// Mediator Pattern
class OrderMediator {

    private final Manager manager;
    private final Customer customer;

    public OrderMediator(Manager manager, Customer customer) {
        this.manager = manager;
        this.customer = customer;
    }

    public void findOrder() {
        JOptionPane.showMessageDialog(null, "Order Manager: Find Order", "Information", JOptionPane.INFORMATION_MESSAGE);
        manager.orderConfirmation();
    }

}

//component
abstract class User {

    protected OrderMediator orderMediator;

    public final void setOrderMediator(OrderMediator taxiCenterApp) {
        this.orderMediator = taxiCenterApp;
    }
}

class Manager extends User {

    public void orderConfirmation() {

        Invoice invoice = new Invoice();
        DefaultTableModel dtm = (DefaultTableModel) invoice.jTable2.getModel();

        String p_name, p_size, p_toppings, p_qty, p_price;

        for (int i = 0; i < Home.homeJTable2.getRowCount(); i++) {

            p_name = Home.homeJTable2.getValueAt(i, 0).toString();
            p_size = Home.homeJTable2.getValueAt(i, 1).toString();
            p_toppings = Home.homeJTable2.getValueAt(i, 2).toString();
            p_qty = Home.homeJTable2.getValueAt(i, 3).toString();
            p_price = Home.homeJTable2.getValueAt(i, 4).toString();

            Vector vector = new Vector();
            
            vector.add(p_name + " " + p_size + " " + p_qty);
            vector.add(p_toppings);
            vector.add(p_price);
            dtm.addRow(vector);

        }
        invoice.jLabel21.setText(tPrice);
        invoice.setVisible(true);
        Home.jButton3.setVisible(true);

    }
}

class Customer extends User {

    public void searchOrder() {
        JOptionPane.showMessageDialog(null, Home.userName + " : Pizza Orded", "Information", JOptionPane.INFORMATION_MESSAGE);
        orderMediator.findOrder();
    }
}

// Chain of Responsibility Pattern
abstract class OrderStep {

    protected OrderStep nextStep;

    public void setNextStep(OrderStep nextStep) {
        this.nextStep = nextStep;
    }

    public abstract void processStep(cookedPizza pizza);
}

class cookedPizza {

    String pizza;

    public void setPizza(String pizza) {
        this.pizza = pizza;
    }

    public String getPizza() {
        return pizza;
    }
    

}

class AcceptingStep extends OrderStep {

    @Override
    public void processStep(cookedPizza pizza) {
        if (pizza.getPizza()=="acceptPizza") {
            JOptionPane.showMessageDialog(null, "Order is accepted.", "Information", JOptionPane.INFORMATION_MESSAGE);
            pizza.setPizza("pizzaCooked");
            nextStep.processStep(pizza);
        }
    }
}

class CookingStep extends OrderStep {

    @Override
    public void processStep(cookedPizza pizza) {
        if (pizza.getPizza()==("pizzaCooked")) {
            JOptionPane.showMessageDialog(null, "Pizza is being cooked.", "Information", JOptionPane.INFORMATION_MESSAGE);
            pizza.setPizza("packedPizza");
            nextStep.processStep(pizza);
        }
    }
}

class PackingStep extends OrderStep {

    @Override
    public void processStep(cookedPizza pizza) {
        if (pizza.getPizza()==("packedPizza")) {
            JOptionPane.showMessageDialog(null, "Pizza is being packed.", "Information", JOptionPane.INFORMATION_MESSAGE);
            pizza.setPizza("deliverPizza");
            nextStep.processStep(pizza);
        }
    }
}

class HandoverStep extends OrderStep {

    @Override
    public void processStep(cookedPizza pizza) {
        if (pizza.getPizza()==("deliverPizza")) {
            JOptionPane.showMessageDialog(null, "Pizza is handed over to the driver for delivery.", "Information", JOptionPane.INFORMATION_MESSAGE);

        }
    }
}

// Build Pattern
class Pizza {

    private final String type;
    private final String size;
    private final double basePrice;
    private final List<String> toppings;
    
    public  static  double totalPrice;
    
    
    
    private Pizza(Builder builder) {
        this.type = builder.type;
        this.size = builder.size;
        this.basePrice = builder.basePrice;
        this.toppings = builder.toppings;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public List<String> getToppings() {
        return toppings;
    }

    public static double getTotalPrice() {
        return totalPrice;
    }
    
    

    public static class Builder {

        private String type;
        private String size;
        private double basePrice;
        private List<String> toppings = new ArrayList<>();

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setSize(String size) {
            this.size = size;
            return this;
        }

        public Builder setBasePrice(double basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public Builder addAllToppings(List<String> toppings) {
            this.toppings.addAll(toppings);
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }

    public void display() {

        double toppingsPrice = toppings.size() * 150;
        totalPrice = basePrice + toppingsPrice;

        System.out.println("Pizza: " + type + ", Size: " + size + ", Toppings: " + String.join(", ", toppings) + " \nTotal Price : Rs " + getTotalPrice());

    }


}



