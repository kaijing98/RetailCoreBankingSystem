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
public class CreateNewDepositAccountException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewDepositAccountException</code>
     * without detail message.
     */
    public CreateNewDepositAccountException() {
    }

    /**
     * Constructs an instance of <code>CreateNewDepositAccountException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewDepositAccountException(String msg) {
        super(msg);
    }
}
