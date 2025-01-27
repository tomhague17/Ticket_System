import java.io.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Main Program that contains the main method, a method for reading in the file, a method for creating the menu, adding tickets
 * and removing tickets.
 */
public class MainProgram {
    /**
     * Main method which produces a command line menu a user can interact with to purchase and remove tickets for customers,
     * that have been inputted from a file.
     * Exceptions are thrown if the user enters data that does not exist in the system or incorrect data types.
     */
    public static void main(String[] args) {

        try {
            // open new input file and assign to a Scanner
            Scanner inputFile = new Scanner(new FileReader("input_data.txt"));
            // open new output file and assign to a Printwriter
            PrintWriter outputFile = new PrintWriter(new FileWriter("letters.txt"));

            readInInputFile(inputFile);

            // convert SortedLinkedList of tickets to a HashMap
            Ticket.ticketHashMap = Ticket.convertSortedLinkedListToHashMap(Ticket.sortedTickets);
            inputFile.close();

            // set-up while loop for the switch
            boolean scannerFinished = false;
            while (!scannerFinished) {

                printMenu(); // print out the menu during each iteration of the loop.
                System.out.print("Please enter one of the above options: ");

                try {
                    Scanner input = new Scanner(System.in); // new scanner input every time the loop runs.
                    String option = input.nextLine(); // place the menu option entered by the user into the switch.

                    switch (option.toLowerCase()) {
                        case "t":
                            Ticket.printAvailableTicketInfo();
                            break;
                        case "c":
                            Customer.printAllCustomerInfo();
                            break;
                        case "a":
                            // update customer data when they buy new tickets.
                            addTicketToCustomer(input, outputFile);
                            break;
                        case "r":
                            // update customer data when they remove tickets.
                            removeTicketFromCustomer(input);
                            break;
                        case "f":
                            scannerFinished = true;
                            System.out.println("Goodbye! See you soon.");
                            break;
                        default:
                            System.out.println("Invalid option! Please select t, c, a ,r or f");
                            break;
                    }
                } catch (InputMismatchException e) { // deal with when user enters something that isn't a letter.
                    System.out.println("Please enter one of the letters specified");
                }
            } outputFile.close(); // close output file
        } catch (FileNotFoundException e) { // deal with situations where input file isn't found.
            System.out.println("Input File not found, check you've added the correct one!");
        } catch (NumberFormatException e) { // deal with situations where input file format doesn't follow the specifications.
            System.out.println("Input File not in the correct format, check the structure.");
        }
        catch (IOException e) { // // deal with situations where output file isn't found.
            System.out.println("Output File not found, check you've added the correct one!");
        }
    }

    /**
     * Designs the menu with 5 options as specified, including one to exit the program.
     */
    private static void printMenu() {
        System.out.println("t: Display all ticket information");
        System.out.println("c: Display all customer information");
        System.out.println("a: Add new tickets to a customer account");
        System.out.println("r: Delete tickets from a customer account");
        System.out.println("f: Exit the system");
        System.out.println("Please note that entering customer and ticket names is case-sensitive.");
    }

    /**
     * Method for reading in the input file and process the data for tickets and customers to the relevant places.
     */
    public static void readInInputFile(Scanner inputFile) {

        try {
            // create an instance of a customer by reading overall number of customers and then taking customers names
            // off the following lines, until the number of customers added equals the number of customers specified.
            int numberOfCustomers = inputFile.nextInt();
            inputFile.nextLine();

            int customersAddedtoSystem = 0;
            while (inputFile.hasNextLine() & customersAddedtoSystem < numberOfCustomers) {
                String[] customerAddedFullName = inputFile.nextLine().split(" ");
                try {
                    if (customerAddedFullName.length == 2) {
                        String firstName = customerAddedFullName[0];
                        String lastName = customerAddedFullName[1];
                        Customer newCustomer = new Customer(firstName, lastName);
                        Customer.sortedCustomers.insert(newCustomer);
                        customersAddedtoSystem++;
                    }
                } catch (
                        ArrayIndexOutOfBoundsException e) { // Deal with situations where customer name is in the wrong format.
                    System.out.println("Invalid input, could you please try again ensuring the customer has a first name and last name");
                }
            }
        } catch (InputMismatchException e) { // Deal with situations number of customers isn't properly specified.
            System.out.println("Invalid input, input file doesn't specify the number of customers as a whole number.");
        }

        try {
            // create an instance of a ticket by reading overall number of tickets and then taking ticket names and
            // prices off the following lines, until the number of tickets added equals the number of tickets specified.
            int numberOfTickets = inputFile.nextInt();
            inputFile.nextLine();

            int ticketsAddedtoSystem = 0;
            while (inputFile.hasNextLine() & ticketsAddedtoSystem < numberOfTickets) {
                String name = inputFile.nextLine();
                double price = inputFile.nextDouble();
                Ticket newTicket = new Ticket(name, price);
                Ticket.sortedTickets.insert(newTicket);
                ticketsAddedtoSystem++;
                inputFile.nextLine();
            }
        } catch (InputMismatchException e) { // Deal with situations number of customers isn't properly specified.
            System.out.println("Invalid input, input file doesn't specify the number of tickets as a whole number.");
        }

        // Read in the discount values and store them in the Ticket class.
        try {
            if (inputFile.hasNextDouble()) {
                Ticket.discount1 = inputFile.nextDouble();
                inputFile.nextLine();
            }
        } catch (
                NoSuchElementException e) { // Deal with situations where no discount values are added to the input file.
            System.out.println("No discounts are available");
        }
        try {
            if (inputFile.hasNextDouble()) {
                Ticket.discount2 = inputFile.nextDouble();
                inputFile.nextLine();
            }
        } catch (
                NoSuchElementException e) { // Deal with situations where only 1 discount value is added to the input file.
            System.out.println("No further discounts are available");
        }
        try {
            if (inputFile.hasNextDouble()) {
                Ticket.discount3 = inputFile.nextDouble();
            }
        } catch (
                NoSuchElementException e) { // Deal with situations where only 2 discount values are added to the input file.
            System.out.println("No further discounts are available");
        }
        System.out.println("File has been successfully read in.");
    }

    /**
     * Method that takes input from the user and uses it add tickets to the relevant accounts, calculate price and apply any discount.
     * Print statements and exceptions if unsuccessful.
     * @param input (Scanner)
     * @param outputFile (Printwriter)
     */
    public static void addTicketToCustomer(Scanner input, PrintWriter outputFile) {
        System.out.println("Please enter the first name and surname of the customer wishing to buy a ticket: ");
        String customerName = input.nextLine();
        // check customer name entered is valid customer
        if (Customer.getRelevantCustomer(customerName) != null) {
            Customer customer = Customer.getRelevantCustomer(customerName);
            System.out.println("Please enter the name of the ticket you would like to buy: ");
            String ticketChosenName = input.nextLine();
            Ticket relevantTicketWeNeed;
            if (Ticket.ticketHashMap.containsKey(ticketChosenName)) { // check ticket name entered is valid ticket.
                relevantTicketWeNeed = Ticket.getUserReleventTicket(ticketChosenName);
            } else {
                System.out.println("Apologies, the ticket entered isn't found in our program.");
                return;
            }
            try { // confirm customer is valid, add ticket to their account and calculate price. Print statements to confirm success or not.
                assert customer != null;
                if (customer.canCustomerBuyTicket(relevantTicketWeNeed)) {
                    System.out.println("Please enter the quantity of tickets you wish to buy:");
                    try {
                        int quantityCustomerWants = input.nextInt();
                        if (quantityCustomerWants > 0) {
                            customer.addTicketToCustomerAccount(relevantTicketWeNeed, quantityCustomerWants);
                            System.out.println("You have added a quantity of: " + quantityCustomerWants + ", of " + relevantTicketWeNeed + " each.");
                            double ticketPrice = Customer.calcPriceOfTicketsPurchased(ticketChosenName, quantityCustomerWants);
                            System.out.println("The original cost of purchasing these tickets: £" + String.format("%.2f", ticketPrice));
                            // Print letter to outfile if discount not available.
                            int totalCustomerTicketQuantity = customer.overallCustomerTicketQuantity(customer.getTicketsOwnedHashMap());
                            if (totalCustomerTicketQuantity < 6) {
                                int ticketsLeftToBuy = 6 - totalCustomerTicketQuantity;
                                outputFile.println(("No discount is available yet, please purchase " + ticketsLeftToBuy
                                        + " more tickets for a discount. You aren't far away!"));
                                outputFile.flush();
                            } else { // Calculate discount available and print out the result to menu.
                                double discountedPrice = ticketPrice * Ticket.calculateCustomerTicketDiscount(totalCustomerTicketQuantity);
                                if (discountedPrice != ticketPrice) {
                                    System.out.println("Discounted Price for you to pay: £" + String.format("%.2f", discountedPrice));
                                } else {
                                    System.out.println("No discount applied yet.");
                                }
                                System.out.println("Congratulations, you have received a discount of " +
                                        (100 - Ticket.calculateCustomerTicketDiscount(totalCustomerTicketQuantity) * 100) + "% for your tickets.");
                            }
                        } else { // deal with invalid ticket quantity entry of <=0
                            System.out.println("Invalid ticket quantity, please try again.");
                        }
                    } catch (InputMismatchException e) { // deal with incorrect ticket quantity data type entry
                        System.out.println("Please enter a whole number for the quantity.");
                    }
                } else {
                    System.out.println("Customer already holds the max 3 different tickets.");
                }
            } catch (NullPointerException e) { // deal with invalid customer entry returning null.
                System.out.println("Customer does not exist in our program.");
            }
        } else {
            System.out.println("Sorry, the customer you entered isn't found in this system. You're welcome to try again.");
        }
    }

    /**
     * Method that takes input from a scanner, and uses to remove tickets from a customer account as specified.
     * Print statements and exceptions if unsuccessful.
     * @param input (scanner)
     */
    public static void removeTicketFromCustomer(Scanner input) {
        System.out.println("Please enter the first name and surname of the customer wishing to remove a ticket: ");
        String customerChosenFullName = input.nextLine();
        // check customer name entered is valid customer
        if (Customer.getRelevantCustomer(customerChosenFullName) != null) {
            Customer customer = Customer.getRelevantCustomer(customerChosenFullName);
            System.out.println("Please enter the name of the ticket you want to remove: ");
            String ticketName = input.nextLine();
            // check ticket name entered is valid ticket.
            Ticket relevantTicketWeNeed;
            if (!Ticket.ticketHashMap.containsKey(ticketName)) {
                System.out.println("Apologies, the ticket entered isn't found in our program");
                return;
            } else {
                relevantTicketWeNeed = Ticket.getUserReleventTicket(ticketName);
            }
            try { // confirm customer is valid, remove ticket from their account and calculate price. Print statements to confirm success or not.
                assert customer != null;
                if (!customer.getTicketsOwnedHashMap().containsKey(relevantTicketWeNeed)) {
                    System.out.println("Customer hasn't purchased this ticket yet, feel free to try again.");
                    return;
                }
                System.out.println("Please enter the quantity of tickets you want to remove: ");
                try {
                    int quantityCustomerWantsToRemove = input.nextInt();
                    // check customer has enough tickets in their account to remove the quantity user inputted.
                    if (quantityCustomerWantsToRemove > 0) {
                        if (customer.canCustomerRemoveTicket(relevantTicketWeNeed, quantityCustomerWantsToRemove)) {
                            customer.removeTicketFromCustomerAccount(relevantTicketWeNeed, quantityCustomerWantsToRemove);
                            System.out.println("You have removed " + quantityCustomerWantsToRemove + " tickets of the: " + relevantTicketWeNeed);
                        } else {
                            System.out.println("Customer unfortunately doesn't own enough tickets to remove that quantity, try a lower amount.");
                            System.out.println("They own " + customer.getTicketsOwnedHashMap().get(relevantTicketWeNeed)
                                    + " of the " + relevantTicketWeNeed);
                        }
                    } else { // deal with invalid ticket quantity entry of <=0
                        System.out.println("Invalid ticket quantity, please try again.");
                    }
                } catch (InputMismatchException e) { // deal with incorrect ticket quantity data type entry
                    System.out.println("Please enter a whole number for the quantity.");
                }
            } catch (NumberFormatException e) { // deal with if customer ticket account is empty.
                System.out.println("Customer ticket account is empty, please add some tickets.");
            }
        } else {
            System.out.println("Sorry, the customer you entered isn't found in this system. You're welcome to try again.");
        }
    }
}