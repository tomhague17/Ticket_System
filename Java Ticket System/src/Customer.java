import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a customer.
 * Includes methods for storing and retrieving customer information, adding and removing tickets to their account,
 * calculating the cost of the tickets, applying any discounts and printing out their information to the menu.
 * @author Thomas Hague
 */

public class Customer implements Comparable<Customer> {
    // Fields
    private String firstName;
    private String lastName;
    public static SortedLinkedList<Customer> sortedCustomers = new SortedLinkedList<>();
    private HashMap<Ticket, Integer> ticketsOwnedHashMap = new HashMap<>();

    /**
     * Creates a customer with specified First Name and Last Name.
     * @param firstName customer first name.
     * @param lastName  customer last name.
     */
    Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Getters for accessing private fields.
     */
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public HashMap<Ticket, Integer> getTicketsOwnedHashMap() {
        return ticketsOwnedHashMap;
    }

    /**
     * Create a customers' full name.
     * @return a concatenation of their first name and last name with a space in between.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Find the relevant customer that the user is looking for, if it is a valid customer.
     * @param customerName (as entered by the user)
     * @return the relevant Customer if the user enters a valid customer, otherwise return null.
     */
    public static Customer getRelevantCustomer(String customerName) {
        for (Customer customer : sortedCustomers) {
            if (customer.getFullName().equals(customerName)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Overrides the existing string method from Object class, to specify how we want our customer instances to be displayed.
     * @return Customer name specified by first name and last name.
     */
    @Override
    public String toString() {
        return String.format("First Name: %s Last Name: %s", getFirstName(), getLastName());
    }

    /**
     * Overrides the existing compareTo method from Comparable class, to compare two customers by name lexicographically.
     * The method will first try and compare using last name, and if they were both equal, will compare using first name.
     * @param other the other object to be compared.
     * @return 0 if they are both equal, an int < 0 if the current customer name is lexicographically
     * less than the other, or an int > 0 if the current customer name is lexicographically greater than the other.
     */
    @Override
    public int compareTo(Customer other) {
        int cmpLastName = this.getLastName().compareTo(other.getLastName());
        int cmpFirstName = this.getFirstName().compareTo(other.getFirstName());
        if (cmpLastName == 0) {
            return cmpFirstName;
        } else {
            return cmpLastName;
        }
    }

    /**
     * Determines if a customer is able to buy a new ticket. If the ticket type entered is not one that a customer already owns,
     * it checks if they have <3 different ticket types.
     * @param ticket (as derived from the ticket name inputted by the user).
     * @return True if the customer can buy the ticket, otherwise return false.
     */
    protected boolean canCustomerBuyTicket(Ticket ticket) {
        return !(ticketsOwnedHashMap.size() >= 3 & !ticketsOwnedHashMap.containsKey(ticket));
    }

    /**
     * Adds ticket to a customer account. If customer has the ticket already, we increase the quantity of that ticket in
     * their account by the quantity specified. Otherwise, we add the ticket to their account and with the
     * quantity specified.
     * @param ticket   (customer record for that ticket)
     * @param quantity (as inputted by the user)
     */
    protected void addTicketToCustomerAccount(Ticket ticket, int quantity) {
        if (ticketsOwnedHashMap.containsKey(ticket)) {
            ticketsOwnedHashMap.put(ticket, ticketsOwnedHashMap.get(ticket) + quantity);
        } else {
            ticketsOwnedHashMap.put(ticket, quantity);
        }
    }

    /**
     * Checks if a customer is able to remove a quantity of tickets from their account, by checking if ticket is valid
     * and the ticket quantity they already own is >= to the quantity specified.
     * @param ticket   (customer record for that ticket)
     * @param quantity (as inputted by the user)
     * @return true if customer can remove ticket quantity, otherwise return false.
     */
    protected boolean canCustomerRemoveTicket(Ticket ticket, int quantity) {
            return ticketsOwnedHashMap.get(ticket) >= quantity;
    }

    /**
     * Removes a quantity of tickets from a customers' account
     * If the quantity being removed == the amount of the tickets a customer already owns, the ticket is removed from
     * their account.
     * If the quantity being removed is the less than the quantity a customer already owns, the quantity owned will be
     * reduced by the quantity being removed.
     * @param ticket   (ticket type in a customer account)
     * @param quantity (ticket quantity as inputted by the user, to be removed)
     */
    protected void removeTicketFromCustomerAccount(Ticket ticket, int quantity) {
        if (ticketsOwnedHashMap.get(ticket) - quantity == 0) {
            ticketsOwnedHashMap.remove(ticket);
        } else {
            ticketsOwnedHashMap.put(ticket, ticketsOwnedHashMap.get(ticket) - quantity);
        }
    }

    /**
     * Calculates the cost of the tickets the customer wants to buy, including any available discount. Print statement if unsuccessful.
     * @param ticketName (as entered by the user)
     * @param quantity   (as inputted by the user)
     * @return If ticket is valid, returns the cost of new tickets bought by the customer, including any available
     * discount. Otherwise, returns -1.0.
     */
    protected static double calcPriceOfTicketsPurchased(String ticketName, int quantity) {
        Ticket ticket = Ticket.getUserReleventTicket(ticketName);
        if (ticket == null) {
            System.out.println("Apologies, the ticket entered isn't found in this system");
            return -1.0;
        } else {
            return ticket.getPrice() * quantity;
        }
    }

    /**
     * Calculates the overall quantity of tickets a customer has, across all ticket types.
     * @param ticketsOwnedHashMap (a customers ticket account)
     * @return the total int quantity of tickets a customer has.
     */
    protected int overallCustomerTicketQuantity(HashMap<Ticket, Integer> ticketsOwnedHashMap) {
        int total = 0;
        for (Map.Entry<Ticket, Integer> entry : ticketsOwnedHashMap.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }

    /**
     * Calculates the total price of a customers tickets across all ticket types, before any discounts are applied.
     * @return a double (the total ticket price before discount).
     */
    protected double overallInitialCustomerTicketPrice() {
        double totalPrice = 0;
        for (Map.Entry<Ticket, Integer> entry : ticketsOwnedHashMap.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }

    /**
     * Calculates the total price of a customers tickets across all ticket types after any discounts are applied.
     * @param totalPrice total ticket price before any discount.
     * @return a double (the total ticket price after any discount).
     */
    protected double discountedTotalTicketPrice(double totalPrice) {
        return totalPrice * Ticket.calculateCustomerTicketDiscount(overallCustomerTicketQuantity(ticketsOwnedHashMap));
    }

    /**
     * Prints out the original cost of all the tickets a customer owns, in a specified format to 2dp. Different print
     * statement if no tickets owned.
     * @param ticketsOwnedHashMap (a customers ticket account).
     */
    protected static void printCustomerTicketsBoughtInfo(HashMap<Ticket, Integer> ticketsOwnedHashMap) {
        if (!ticketsOwnedHashMap.isEmpty()) {
            for (Map.Entry<Ticket, Integer> entry : ticketsOwnedHashMap.entrySet()) {
                Ticket ticket = entry.getKey();
                Integer quantity = entry.getValue();
                System.out.println(ticket + ", Quantity: " + quantity);
                double price = quantity * ticket.getPrice();
                System.out.println("Original Cost of the tickets above: £" + String.format("%.2f", price));
            }
        } else {
            System.out.println("No tickets owned.");
        }
    }

    /**
     * Prints out all information on each customer to the command menu, including full name, their ticket purchases,
     * overall cost of their tickets before discounts are applied, and then price paid after discounts are applied.
     */
    protected static void printAllCustomerInfo() {
        for (Customer customer : sortedCustomers) {
            System.out.println(customer.toString());
            printCustomerTicketsBoughtInfo(customer.getTicketsOwnedHashMap());
            if (!customer.ticketsOwnedHashMap.isEmpty()) {
                System.out.println("The original cost of all their tickets: £" + String.format("%.2f", customer.overallInitialCustomerTicketPrice()) + ".");
                System.out.println("The discounted cost of all their tickets: £"
                        + String.format("%.2f", customer.discountedTotalTicketPrice(customer.overallInitialCustomerTicketPrice())) + ".");
            } else {
                System.out.println("No price information.");
            }
        }
    }
}