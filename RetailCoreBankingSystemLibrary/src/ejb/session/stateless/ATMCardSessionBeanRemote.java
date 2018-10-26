/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AtmCardEntity;
import entity.DepositAccountEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AtmCardNotFoundException;
import util.exception.CreateNewAtmCardException;
import util.exception.DepositAccountNotFoundException;


public interface ATMCardSessionBeanRemote {

    public void setCustomer(AtmCardEntity newAtmCard, Long customerId);

    public AtmCardEntity createNewAtmCard(AtmCardEntity newAtmCard, Long customerId) throws CreateNewAtmCardException;

    public void associateCardAndCustomer(Long AtmCardId, Long customerId);
    
    public void associateCardAndAccount(Long atmCardId);

    public AtmCardEntity retrieveCardByCardNum(String cardNum) throws AtmCardNotFoundException;

    public void disableAtmCard(Long atmCardId);

    public void ChangePIN(Long atmCardId, String newpin);

    
    
}
