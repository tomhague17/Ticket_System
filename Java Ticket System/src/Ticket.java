import java.util.HashMap;

/**
 * A class representing a ticket.
 * Includes methods for storing and retrieving ticket information, storing discount information; printing ticket
 * information and calculating discounts.
 * @author Thomas Hague
 */

public class Ticket implements Comparable<Ticket> {
    // Fields
    private String name;
    private double price;
    public static HashMap<String, Double> ticketHashMap = new HashMap<>();
    public static SortedLinkedList<Ticket> sortedTickets = new SortedLinkedList<>();
    public static double discount1;
    public static double discount2;
    public static double discount3;

    /**
     * Creates a Ticket instance with specified name and price.
     * @param name ticket name.
     * @param price ticket price.
     */
    Ticket(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Getters for private fields.
     */

    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }

    /**
     * Find the relevant Ticket that the user is looking, if it is a valid ticket.
     * @param ticketName (as entered by the user)
     * @return the relevant Ticket if the user enters a valid ticket, otherwise return null.
     */
    public static Ticket getUserReleventTicket(String ticketName) {
        for (Ticket ticket : sortedTickets) {
            if (ticketName.equals(ticket.getName())) {
                return ticket;
            }
        }
        return null;
    }

    /**
     * Convert a lexicographically ordered LinkedList of Tickets into a HashMap.
     * @param sortedTickets , a sortedLinkedList of Ticket instances.
     * @return a HashMap of the tickets, with the Ticket Name as the key and the Ticket Price as the value.
     */

    public static HashMap<String, Double> convertSortedLinkedListToHashMap(SortedLinkedList<Ticket> sortedTickets) {
        HashMap<String, Double> ticketHashMap = new HashMap<>();
        for (Ticket ticket : sortedTickets) {
            ticketHashMap.put(ticket.getName(), ticket.getPrice());
        }
        return ticketHashMap;
    }

    /**
     * Overrides the existing string method from Object class, to specify how we like to output our Ticket information.
     * @return Ticket name and Price(2 decimal places and with a £).
     */
    @Override
    public String toString() {
        return String.format("Ticket: %s, Price: £%.2f", getName(), getPrice());
    }

    /**
     * Overrides the existing compareTo method from Comparable class, to compare two ticket instances by name lexicographically.
     * @param other (the other ticket object to be compared).
     * @returns 0 if they are both equal, an int < 0 if the current object name is lexicographically
     * less than the other, or an int > 0 if current object name is lexicographically greater than the other.
     */
    @Override
    public int compareTo(Ticket other) {
        return this.getName().compareTo(other.getName());
    }

    /**
     * Print out available information on all ticket types and their respective prices, ordered lexicographically.
     */
    public static void printAvailableTicketInfo() {
        for (Ticket ticket : sortedTickets) {
            System.out.println(ticket.toString());
        }
    }

    /**
     * A method for calculating the discount that will be applied, based upon a quantity of tickets, if a discount is available.
     * The formula will take the information on how much each discount will be at the respective quantity levels from an input file.
     * @param quantity of tickets that we are checking for any discount.
     * @return If the quantity is less than 6, no discount is applied and 1.0 is returned. If a discount is available; a double
     * (1 - the relevant discount) is returned.
     */
    protected static double calculateCustomerTicketDiscount(Integer quantity) {
        if (quantity >= 6 & quantity <= 10) {
            return 1-discount1; // the smallest discount.
        } else if (quantity >= 11 & quantity <=25) {
            return 1-discount2; // the middle discount.
        } else if (quantity >= 26) {
            return 1-discount3; // the largest discount
        } else {
            return 1.0;
        }
    }
}
