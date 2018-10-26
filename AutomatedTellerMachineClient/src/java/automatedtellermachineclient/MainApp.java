/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedtellermachineclient;

import java.util.Scanner;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import ejb.session.stateless.CustomerSessionBeanRemote;
import entity.AtmCardEntity;
import entity.CustomerEntity;
import util.exception.AtmCardNotFoundException;
import ejb.session.stateless.ATMCardSessionBeanRemote;
import entity.DepositAccountEntity;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Kai Jing
 */
public class MainApp {
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private CustomerEntity currentCustomerEntity;
    private ATMCardSessionBeanRemote atmCardSessionBeanRemote;
    
    public MainApp() {
        
    }
    
    public MainApp(CustomerSessionBeanRemote customerSessionBeanRemote, ATMCardSessionBeanRemote atmCardSessionBeanRemote) {
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.atmCardSessionBeanRemote = atmCardSessionBeanRemote;    
    }
    
    public void runApp() throws CustomerNotFoundException, AtmCardNotFoundException {
        Scanner sc = new Scanner (System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Welcome to Retail-Core-Banking-System (RCBS) Automated Teller Machine Client ***\n");
            try {
                doLogin();
                System.out.println("1: Change PIN ");
                System.out.println("2: Enquire Available Balance ");
                System.out.println("3: Exit\n");
                response = 0;
            }
            catch (InvalidLoginCredentialException ex) {
                System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
            }
            
            while (response < 1 || response >3) {
                System.out.print("> ");
                
                response = sc.nextInt();
                
                if (response == 1) {
                    doChangePIN(currentCustomerEntity.getCustomerId());
                } else if (response == 2) {
                    doEnquireAvailBalance();
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
            System.out.println("You are login as: " + currentCustomerEntity.getFirstName());
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void doChangePIN(Long customerId) {
        Scanner sc = new Scanner (System.in);
        System.out.print("Enter atm card number: ");
        String cardNum = sc.next();
        try {
            AtmCardEntity atmCard = atmCardSessionBeanRemote.retrieveCardByCardNum(cardNum);
            Long atmCardId = atmCard.getAtmCardId();
            System.out.print("Enter current pin> ");
            String currPin = sc.next().trim();
            if (currPin.equals(atmCard.getPin())) {
                System.out.print("Enter new pin> ");
                String newpin = sc.next();
                atmCardSessionBeanRemote.ChangePIN(atmCardId, newpin);
                //System.out.println(atmCard.getPin());
            } else {
                System.out.println("Incorrect pin! Please try again");
            }
        } catch (AtmCardNotFoundException ex) {
            System.out.println("Atm Card Card Number " + cardNum + " does not exist!");
        }
    }

    private void doEnquireAvailBalance() throws AtmCardNotFoundException {
        Scanner sc = new Scanner (System.in);
        System.out.print("Enter atm card number: ");
        String cardNum = sc.next();
        try {
            AtmCardEntity atmCard = atmCardSessionBeanRemote.retrieveCardByCardNum(cardNum);
            Long atmCardId = atmCard.getAtmCardId();
            System.out.print("Enter current pin> ");
            String currPin = sc.next().trim();
            if (currPin.equals(atmCard.getPin())) {
                List<DepositAccountEntity> depositAccounts = atmCard.getDepositAccounts();
                if (depositAccounts.size() == 1) {
                    DepositAccountEntity depositAccount = depositAccounts.get(0);
                    String currAccountNum = depositAccount.getAccountNum();
                    BigDecimal availBalance = depositAccount.getAvailBalance();
                    System.out.println("Current deposit account " + currAccountNum + " has " + availBalance);
                } else {
                    //card associated with more than one account
                    //customer to indicate which account to enquire balance from
                }
            }
        } catch (AtmCardNotFoundException ex) {
            System.out.println("Atm Card Card Number " + cardNum + " does not exist!");
        }
    }
}
