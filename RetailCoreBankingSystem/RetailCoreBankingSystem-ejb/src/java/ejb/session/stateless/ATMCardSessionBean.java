/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCardEntity;
import entity.CustomerEntity;
import entity.DepositAccountEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CreateNewAtmCardException;
import util.exception.DepositAccountNotFoundException;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import java.util.ArrayList;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import util.exception.AssociateCardAndAccountException;
import util.exception.AtmCardNotFoundException;

/**
 *
 * @author Kai Jing
 */
@Stateless
@Local(ATMCardSessionBeanLocal.class)
@Remote(ATMCardSessionBeanRemote.class)

public class ATMCardSessionBean implements ATMCardSessionBeanRemote, ATMCardSessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void setCustomer(AtmCardEntity newAtmCard, Long customerId) {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        newAtmCard.setCustomer(customer);
    }
    
    @Override
    public AtmCardEntity createNewAtmCard(AtmCardEntity newAtmCard, Long customerId) throws CreateNewAtmCardException {
        try {
            em.persist(newAtmCard);
            //customer.getDepositAccounts().add(newdepositAccountEntity);
            em.flush();
            return newAtmCard;
        } 
        catch (PersistenceException ex) {
            System.err.println("********* 1: " + ex.getClass().getSimpleName());
            if (ex.getCause() != null &&
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewAtmCardException("Deposit Account with same ID already exist");
                    }
            else {
                throw new CreateNewAtmCardException("An unexpected error has occurred: " + ex.getMessage());
            }
        } catch (Exception ex) {
            System.err.println("********** 3: " + ex.getClass().getSimpleName());
            throw new CreateNewAtmCardException("An unexpected error has occurred: " + ex.getMessage());
        }
    }
    
    @Override
    //associates customer and atm card entities
    public void associateCardAndCustomer(Long AtmCardId, Long customerId) {
        AtmCardEntity atmCard = em.find(AtmCardEntity.class, AtmCardId);
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        atmCard.setCustomer(customer);
        customer.setAtmCard(atmCard);
    }
    
    @Override
    //associates atm card and deposit account entities
    public void associateCardAndAccount(Long atmCardId) {
        //iterate through the list of deposit accounts, find the first one which is
        //not associated with the atm card and link them
        AtmCardEntity atmCard = em.find(AtmCardEntity.class, atmCardId);
        List<DepositAccountEntity> depositAccounts = atmCard.getDepositAccounts();
        for (DepositAccountEntity depositAccount : depositAccounts) {
            depositAccount.setAtmCard(atmCard);
        }  
    }
    
    @Override
    public AtmCardEntity retrieveCardByCardNum (String cardNum) throws AtmCardNotFoundException {
        Query query = em.createQuery("SELECT c FROM AtmCardEntity c WHERE c.cardNumber = :inCardNumber");
        query.setParameter("inCardNumber", cardNum);
        try {
            return (AtmCardEntity)query.getSingleResult();
        }
        catch (NoResultException | NonUniqueResultException ex) {
            throw new AtmCardNotFoundException("Atm Card Card Number " + cardNum + " does not exist!");
        }
    }
    
    @Override
    public void ChangePIN (Long atmCardId, String newpin) {
        AtmCardEntity atmCard = em.find(AtmCardEntity.class, atmCardId);
        atmCard.setPin(newpin);
        
    }
    
    @Override
    public void disableAtmCard (Long atmCardId) {
        AtmCardEntity atmCard = em.find(AtmCardEntity.class, atmCardId);
        atmCard.setEnabled(false);
    }
}
