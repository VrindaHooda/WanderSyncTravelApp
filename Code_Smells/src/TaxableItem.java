public class TaxableItem extends Item {
    private static final double TAX_RATE = 7.0; // hardcoded tax rate, constant and unmodifiable

    public TaxableItem(String name, double price, int quantity, DiscountType discountType, double discountAmount) {
        super(name, price, quantity, discountType, discountAmount);
    }

    public double getTaxRate() {
        return TAX_RATE; // returns the fixed tax rate
    }
}
