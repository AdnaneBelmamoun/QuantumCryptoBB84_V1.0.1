package qrypto.qommunication; 


import qrypto.exception.*;



public interface QConnection{

    /**
     * This method allows to send qubits according to some coding.
     * @param classname is the name of the class where the quantum transmission
     *  is implemented. 
     * @return the sequence of qubits transmitted.
     * @exception qrypto.QryptoException if the transmission has not been completed
     * with success.
     */

    public QuBit[] send(String classname) throws QryptoException;




    /**
     * This method allows to receive qubits according to some coding.
     * @param classname  is the name of the class where the quantum transmission
     * is implemented.
     * @return the sequence of qubits received.
     * @exception qrypto.exception.QryptoException is the transmission has not completed
     * with success.
     */

    public QuBit[] read(String classname) throws QryptoException;




}












