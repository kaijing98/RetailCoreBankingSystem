/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Kai Jing
 */
public class AssociateCardAndAccountException extends Exception {

    /**
     * Creates a new instance of <code>AssociateCardAndAccountException</code>
     * without detail message.
     */
    public AssociateCardAndAccountException() {
    }

    /**
     * Constructs an instance of <code>AssociateCardAndAccountException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AssociateCardAndAccountException(String msg) {
        super(msg);
    }
}
