/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerEntity;
import util.exception.CustomerNotFoundException;

public interface CustomerSessionBeanRemote {

    public CustomerEntity createCustomerEntity(CustomerEntity newcustomerEntity);

    public CustomerEntity retrieveCustomerByIdentificationNum(String identificationNum) throws CustomerNotFoundException;

    public void associateCustomerAndAccount(Long depositAccountId, Long customerId);

    public void dissociateCustomerAndAccount(Long depositAccountId, Long customerId);


}
