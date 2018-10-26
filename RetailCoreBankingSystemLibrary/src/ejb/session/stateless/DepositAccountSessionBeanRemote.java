/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import entity.DepositAccountEntity;
import javax.ejb.Remote;
import util.exception.CreateNewDepositAccountException;
import util.exception.CustomerNotFoundException;
import util.exception.DepositAccountNotFoundException;
import util.exception.InvalidLoginCredentialException;

public interface DepositAccountSessionBeanRemote {

    public DepositAccountEntity retrieveDepositAccountByAccountNum(String accountNum) throws DepositAccountNotFoundException;

    public void dissociateAccountAndCustomer(Long depositAccountId, Long customerId);

    public DepositAccountEntity createDepositAccountEntity(DepositAccountEntity newdepositAccountEntity, Long customerId) throws CreateNewDepositAccountException;

    public void associateAccountAndCustomer(Long depositAccountId, Long customerId);

    public void setCustomer(DepositAccountEntity newdepositAccountEntity, Long customerId);

    
}
