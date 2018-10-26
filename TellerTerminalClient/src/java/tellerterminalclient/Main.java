/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tellerterminalclient;

import ejb.session.stateless.ATMCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.DepositAccountSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.AtmCardNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DepositAccountNotFoundException;

/**
 *
 * @author Kai Jing
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB 
    private static DepositAccountSessionBeanRemote depositAccountSessionBeanRemote;
    @EJB
    private static ATMCardSessionBeanRemote atmCardSessionBeanRemote;
    
    public static void main(String[] args) throws CustomerNotFoundException, DepositAccountNotFoundException, AtmCardNotFoundException {
        // TODO code application logic here
        MainApp mainApp = new MainApp(customerSessionBeanRemote, depositAccountSessionBeanRemote, atmCardSessionBeanRemote);
        mainApp.runApp();
    }
    
}
