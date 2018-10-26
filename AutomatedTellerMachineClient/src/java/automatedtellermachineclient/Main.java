/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedtellermachineclient;

import ejb.session.stateless.ATMCardSessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.CustomerSessionBeanRemote;
import util.exception.AtmCardNotFoundException;
import util.exception.CustomerNotFoundException;
/**
 *
 * @author Kai Jing
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
    private static ATMCardSessionBeanRemote atmCardSessionBeanRemote;
    
    public static void main(String[] args) throws CustomerNotFoundException, AtmCardNotFoundException {
        MainApp mainApp = new MainApp (customerSessionBeanRemote, atmCardSessionBeanRemote);
        mainApp.runApp();
    }
    
}
