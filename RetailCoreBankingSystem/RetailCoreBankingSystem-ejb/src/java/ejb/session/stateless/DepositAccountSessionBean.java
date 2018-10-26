/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.DepositAccountEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreateNewDepositAccountException;
import util.exception.CustomerNotFoundException;
import util.exception.DepositAccountNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Kai Jing
 */
@Stateless
@Local(DepositAccountSessionBeanLocal.class)
@Remote(DepositAccountSessionBeanRemote.class)
public class DepositAccountSessionBean implements DepositAccountSessionBeanRemote, DepositAccountSessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void setCustomer(DepositAccountEntity newdepositAccountEntity, Long customerId) {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        newdepositAccountEntity.setCustomer(customer);
    }

    @Override
    public DepositAccountEntity createDepositAccountEntity(DepositAccountEntity newdepositAccountEntity, Long customerId) throws CreateNewDepositAccountException {
        try {
            em.persist(newdepositAccountEntity);
            //customer.getDepositAccounts().add(newdepositAccountEntity);
            em.flush();
            return newdepositAccountEntity;
        } 
        catch (PersistenceException ex) {
            System.err.println("********* 1: " + ex.getClass().getSimpleName());
            if (ex.getCause() != null &&
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                    {
                        throw new CreateNewDepositAccountException("Deposit Account with same ID already exist");
                    }
            else {
                throw new CreateNewDepositAccountException("An unexpected error has occurred: " + ex.getMessage());
            }
        } catch (Exception ex) {
            System.err.println("********** 3: " + ex.getClass().getSimpleName());
            throw new CreateNewDepositAccountException("An unexpected error has occurred: " + ex.getMessage());
        }
    }
    
    @Override
    //associates customer and deposit account entities
    public void associateAccountAndCustomer(Long depositAccountId, Long customerId) {
        DepositAccountEntity depositAccount = em.find(DepositAccountEntity.class, depositAccountId);
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        depositAccount.setCustomer(customer);
        customer.getDepositAccounts().add(depositAccount);
    }
    
    @Override
    public void dissociateAccountAndCustomer (Long depositAccountId, Long customerId) {
        DepositAccountEntity depositAccount = em.find(DepositAccountEntity.class, depositAccountId);
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        
        em.remove(customer);
        customer.getDepositAccounts().remove(depositAccount);
    }
    
    @Override
    public DepositAccountEntity retrieveDepositAccountByAccountNum (String accountNum) throws DepositAccountNotFoundException {
        Query query = em.createQuery("SELECT da FROM DepositAccountEntity da WHERE da.accountNum = :inAccountNum");
        query.setParameter("inAccountNum", accountNum);
        
        try {
            return (DepositAccountEntity)query.getSingleResult();
        }
        catch (NoResultException | NonUniqueResultException ex) {
            throw new DepositAccountNotFoundException("Deposit Account Account Number " + accountNum + " does not exist!");
        }
    }
    
}
