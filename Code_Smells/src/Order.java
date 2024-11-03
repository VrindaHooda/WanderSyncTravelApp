import java.util.List;

public class Order {
    private List<Item> items;
    private String customerName;
    private String customerEmail;

    public Order(List<Item> items, String customerName, String customerEmail) {
        this.items = items;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public double calculateTotalPrice() {
    	double total = 0.0;
        for (Item item : items) {
            double price = accountForDiscount(item);
            total += price * item.getQuantity();
            if (item instanceof TaxableItem) {
                TaxableItem taxableItem = (TaxableItem) item;
                double tax = taxableItem.getTaxRate() / 100.0 * item.getPrice();
                total += tax;
            }
            if (hasGiftCard()) {
                total -= 10.0; // subtract $10 for gift card
            }
            if (total > 100.0) {
                total *= 0.9; // apply 10% discount for orders over $100
            }
        }
        return total;
    }

    public double accountForDiscount (Item item) {
        double price = 0;
        price = item.getPrice();
        switch (item.getDiscountType()) {
            case PERCENTAGE:
                price -= item.getDiscountAmount() * price;
                break;
            case AMOUNT:
                price -= item.getDiscountAmount();
                break;
            default:
                break;
            }

        return price;
    }

    public void sendConfirmationEmail() {
        String message = "Thank you for your order, " + customerName + "!\n\n" +
                "Your order details:\n" + getItemSummary() +
                "Total: " + calculateTotalPrice();
        EmailSender.sendEmail(customerEmail, "Order Confirmation", message);
    }

    private String getItemSummary() {
        StringBuilder summary = new StringBuilder();
        for (Item item : items) {
            summary.append(item.getName()).append(" - ").append(item.getPrice()).append("\n");
        }
        return summary.toString();
    }


    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public boolean hasGiftCard() {
        boolean has_gift_card = false;
        for (Item item : items) {
            if (item instanceof GiftCardItem) {
                has_gift_card = true;
                break;
            }
        }
        return has_gift_card;
    }import java.util.List;

    public class Order {
        private List<Item> items;
        private String customerName;
        private String customerEmail;

        public Order(List<Item> items, String customerName, String customerEmail) {
            this.items = items;
            this.customerName = customerName;
            this.customerEmail = customerEmail;
        }

        public double calculateTotalPrice() {
            double total = 0.0;
            for (Item item : items) {
                double price = accountForDiscount(item);
                total += price * item.getQuantity();
                if (item instanceof TaxableItem) {
                    TaxableItem taxableItem = (TaxableItem) item;
                    double tax = taxableItem.getTaxRate() / 100.0 * item.getPrice();
                    total += tax;
                }
                if (hasGiftCard()) {
                    total -= 10.0; // subtract $10 for gift card
                }
                if (total > 100.0) {
                    total *= 0.9; // apply 10% discount for orders over $100
                }
            }
            return total;
        }

        public double accountForDiscount (Item item) {
            double price = 0;
            price = item.getPrice();
            switch (item.getDiscountType()) {
                case PERCENTAGE:
                    price -= item.getDiscountAmount() * price;
                    break;
                case AMOUNT:
                    price -= item.getDiscountAmount();
                    break;
                default:
                    break;
            }

            return price;
        }

        public void sendConfirmationEmail() {
            String message = "Thank you for your order, " + customerName + "!\n\n" +
                    "Your order details:\n" + getItemSummary() +
                    "Total: " + calculateTotalPrice();
            EmailSender.sendEmail(customerEmail, "Order Confirmation", message);
        }

        private String getItemSummary() {
            StringBuilder summary = new StringBuilder();
            for (Item item : items) {
                summary.append(item.getName()).append(" - ").append(item.getPrice()).append("\n");
            }
            return summary.toString();
        }


        public void addItem(Item item) {
            items.add(item);
        }

        public void removeItem(Item item) {
            items.remove(item);
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }

        public boolean hasGiftCard() {
            boolean has_gift_card = false;
            for (Item item : items) {
                if (item instanceof GiftCardItem) {
                    has_gift_card = true;
                    break;
                }
            }
            return has_gift_card;
        }

        public void printOrder() {
            System.out.println("Order Details:");
            System.out.println(getItemSummary());
        }

        public void addItemsFromAnotherOrder(Order otherOrder) {
            for (Item item : otherOrder.getItems()) {
                items.add(item);
            }
        }

    }



    public void printOrder() {
        System.out.println("Order Details:");
       System.out.println(getItemSummary());
   }

   public void addItemsFromAnotherOrder(Order otherOrder) {
        for (Item item : otherOrder.getItems()) {
            items.add(item);
        }
   }

}

