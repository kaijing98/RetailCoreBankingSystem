/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tellerterminalclient;

import ejb.session.stateless.ATMCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import entity.AtmCardEntity;
import entity.CustomerEntity;
import entity.DepositAccountEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.DepositAccountType;
import util.exception.AtmCardNotFoundException;
import util.exception.CreateNewAtmCardException;
import util.exception.CreateNewDepositAccountException;
import util.exception.CustomerNotFoundException;
import util.exception.DepositAccountNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Kai Jing
 */
public class MainApp {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CustomerEntity currentCustomerEntity;
    private DepositAccountSessionBeanRemote depositAccountSessionBeanRemote;
    private ATMCardSessionBeanRemote atmCardSessionBeanRemote;
    
    public MainApp() {
        
    }
    
    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, DepositAccountSessionBeanRemote depositAccountSessionBeanRemote, ATMCardSessionBeanRemote atmCardSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.depositAccountSessionBeanRemote = depositAccountSessionBeanRemote;
        this.atmCardSessionBeanRemote = atmCardSessionBeanRemote;
    }
    
    public void runApp() throws CustomerNotFoundException, DepositAccountNotFoundException, AtmCardNotFoundException {
        Scanner sc = new Scanner (System.in);
        Integer response =0;
        
        while (true) {
            System.out.println("*** Welcome to Retail-Core-Banking-System (RCBS) Teller Terminal Client ***\n");
            System.out.println("1: Check existing customer ");
            System.out.println("2: Create new Customer");
            System.out.println("3: Exit\n");
            response = 0;
            
            while (response < 1 || response >3) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Customer Exists!\n");
                        menuMain();
                    }
                    catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    doCreateNewCustomer();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 3) {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException, CustomerNotFoundException {
        Scanner sc = new Scanner (System.in);
        String identificationNum = "";
        
        System.out.println("*** RCBS :: Check Existing Customer ***\n");
        System.out.print("Enter customer identification number> ");
        identificationNum = sc.nextLine().trim();
        
        if (identificationNum.length() > 0) {
            currentCustomerEntity = customerSessionBeanRemote.retrieveCustomerByIdentificationNum(identificationNum);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void doCreateNewCustomer() {
        Scanner sc = new Scanner(System.in);
        CustomerEntity newCustomerEntity = new CustomerEntity();
        
        System.out.println("RCBS :: Teller Terminal :: Create New Customer ***\n");
        System.out.print("Enter First Name> ");
        newCustomerEntity.setFirstName(sc.nextLine().trim());
        System.out.print("Enter Last Name> ");
        newCustomerEntity.setLastName(sc.nextLine().trim());
        System.out.print("Enter Identification Number> ");
        newCustomerEntity.setIdentificationNum(sc.nextLine().trim());
        System.out.print("Enter Contact Number> ");
        newCustomerEntity.setContactNum(sc.nextLine().trim());
        System.out.print("Enter Address Line 1> ");
        newCustomerEntity.setAddressLine1(sc.nextLine().trim());
        System.out.print("Enter Address Line 2> ");
        newCustomerEntity.setAddressLine2(sc.nextLine().trim());
        System.out.print("Enter Postal Code> ");
        newCustomerEntity.setPostalCode(sc.nextLine().trim());

        newCustomerEntity = customerSessionBeanRemote.createCustomerEntity(newCustomerEntity);
        System.out.println("New customer created successfully!: " + newCustomerEntity.getCustomerId() + "\n");
    }

    private void menuMain() throws DepositAccountNotFoundException, AtmCardNotFoundException {
        Scanner sc = new Scanner (System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** RCBS ***\n");
            System.out.println("1: Open Deposit Account");
            System.out.println("2: Issue ATM Card");
            System.out.println("3: Logout\n");
            response = 0;
            
            while (response <1 || response >3) {
                System.out.print("> ");
                response = sc.nextInt();
                if (response == 1) {
                    doOpenDepositAccount(currentCustomerEntity.getCustomerId());
                } else if (response == 2) {
                    System.out.println("*** RCBS ***\n");
                    System.out.println("1: Create new ATM Card");
                    System.out.println("2: Issue replacement ATM Card");
                    System.out.println("3: Back\n");
                    System.out.print("> ");
                    int choice = sc.nextInt();
                    if (choice == 1) {
                        doCreateNewCard(currentCustomerEntity.getCustomerId());
                    } else if (choice == 2) {
                        doIssueReplacementCard(currentCustomerEntity.getCustomerId());
                    } else if (choice == 3) {
                        menuMain();
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 3) {
                break;
            }
        }
    }

    private void doOpenDepositAccount(Long customerId) {
        Scanner sc = new Scanner(System.in);
        DepositAccountEntity newDepositAccountEntity = new DepositAccountEntity();
        System.out.println("RCBS :: Teller Terminal :: Create New Deposit Account ***\n");
        System.out.print("Enter Account Number> ");
        newDepositAccountEntity.setAccountNum(sc.nextLine().trim());
        System.out.print("Select Deposit Account Type (1: Savings, 2: Current)> ");
        Integer depositAccountType = sc.nextInt();
        
        if (depositAccountType == 1 || depositAccountType == 2) {
            newDepositAccountEntity.setAccountType(DepositAccountType.values()[depositAccountType-1]);
        } else {
            System.out.println("Invalid option, please try again!\n");
        }
        sc.nextLine();
        System.out.print("Enter available balance> ");
        newDepositAccountEntity.setAvailBalance(sc.nextBigDecimal());
        System.out.print("Enter hold balance> ");
        newDepositAccountEntity.setHoldBalance(sc.nextBigDecimal());
        System.out.print("Enter ledger balance> ");
        newDepositAccountEntity.setLedgerBalance(sc.nextBigDecimal());
        boolean enabled = true;
        newDepositAccountEntity.setEnabled(enabled);
        depositAccountSessionBeanRemote.setCustomer(newDepositAccountEntity, customerId);
        try {
            newDepositAccountEntity = depositAccountSessionBeanRemote.createDepositAccountEntity(newDepositAccountEntity, customerId);
            depositAccountSessionBeanRemote.associateAccountAndCustomer(newDepositAccountEntity.getDepositAccountId(), customerId);
            System.out.println("New deposit account created successfully!: " + newDepositAccountEntity.getDepositAccountId() + "\n");
        }
        catch(CreateNewDepositAccountException ex) {
            System.out.println("An error has occurred while creating the new deposit account: " + ex.getMessage() + "\n");
        }
        
    }

    private void doCreateNewCard(Long customerId) throws DepositAccountNotFoundException {
        Scanner sc = new Scanner(System.in);
        AtmCardEntity newATMCard = new AtmCardEntity();
        
        System.out.println("RCBS :: Teller Terminal :: Create New ATM Card ***\n");
        System.out.print("Enter Card Number> ");
        newATMCard.setCardNumber(sc.nextLine().trim());
        System.out.print("Enter name on card> ");
        newATMCard.setNameOnCard(sc.nextLine().trim());
        newATMCard.setEnabled(true);
        System.out.print("Enter pin> ");
        newATMCard.setPin(sc.nextLine().trim());
        atmCardSessionBeanRemote.setCustomer(newATMCard, customerId);
        System.out.print("Enter number of accounts to associate with> ");
            int numAccounts = sc.nextInt();
            List<DepositAccountEntity> depositAccounts = new ArrayList<DepositAccountEntity>();
            for (int i=0; i< numAccounts; i++) {
                System.out.print("Enter deposit account number> ");
                String currAccountNum = sc.next();
                try {
                    DepositAccountEntity depositAccount = depositAccountSessionBeanRemote.retrieveDepositAccountByAccountNum(currAccountNum);
                    depositAccounts.add(depositAccount);
                } catch (DepositAccountNotFoundException ex) {
                    System.out.println("Error in setting deposit account for atm card");
                    break;
                }
            }
            newATMCard.setDepositAccounts(depositAccounts);
            try {
                newATMCard = atmCardSessionBeanRemote.createNewAtmCard(newATMCard, customerId);
                atmCardSessionBeanRemote.associateCardAndAccount(newATMCard.getAtmCardId());
                atmCardSessionBeanRemote.associateCardAndCustomer(newATMCard.getAtmCardId(), customerId);
                System.out.println("New atm card created successfully!: " + newATMCard.getAtmCardId() + "\n");
            }
            catch(CreateNewAtmCardException ex) {
                System.out.println("An error has occurred while creating the new deposit account: " + ex.getMessage() + "\n");
            }
    }

    private void doIssueReplacementCard(Long customerId) throws AtmCardNotFoundException, DepositAccountNotFoundException {
        Scanner sc = new Scanner (System.in);
        System.out.println("RCBS :: Teller Terminal :: Issue replacement ATM Card ***\n");
        System.out.print("Enter Current Card Number> ");
        String cardNum = sc.next();
        try {
            AtmCardEntity atmCard = atmCardSessionBeanRemote.retrieveCardByCardNum(cardNum);
            Long atmCardId = atmCard.getAtmCardId();
            atmCardSessionBeanRemote.disableAtmCard(atmCardId);
        } catch (AtmCardNotFoundException ex) {
            System.out.println("Atm Card Card Number " + cardNum + " does not exist!");
        }
        doCreateNewCard(customerId);
    }
}
