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
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author Kai Jing
 */
@Stateless
@Local(CustomerSessionBeanLocal.class)
@Remote(CustomerSessionBeanRemote.class)
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "RetailCoreBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public CustomerEntity createCustomerEntity(CustomerEntity newcustomerEntity) {
        em.persist(newcustomerEntity);
        em.flush();
        
        return newcustomerEntity;
    }
    
    @Override
    //associates customer and deposit account entities
    public void associateCustomerAndAccount(Long depositAccountId, Long customerId) {
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        DepositAccountEntity depositAccount = em.find(DepositAccountEntity.class, depositAccountId);
    }
    
    @Override
    public void dissociateCustomerAndAccount (Long depositAccountId, Long customerId) {
        DepositAccountEntity depositAccount = em.find(DepositAccountEntity.class, depositAccountId);
        CustomerEntity customer = em.find(CustomerEntity.class, customerId);
        
        em.remove(customer);
        customer.getDepositAccounts().remove(depositAccount);
    }
    
    @Override
    public CustomerEntity retrieveCustomerByIdentificationNum(String identificationNum) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.identificationNum = :inIdentificationNum");
        query.setParameter("inIdentificationNum", identificationNum);
        
        try {
            return (CustomerEntity)query.getSingleResult();
            
        }
        catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Identification Number " + identificationNum + " does not exist!");
        }
    }
}
