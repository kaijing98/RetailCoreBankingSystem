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
public class CreateNewAtmCardException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewAtmCardException</code> without
     * detail message.
     */
    public CreateNewAtmCardException() {
    }

    /**
     * Constructs an instance of <code>CreateNewAtmCardException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewAtmCardException(String msg) {
        super(msg);
    }
}
