// SaldoInsuficienteException.java
package com.banco.api.exception;  
  
public class SaldoInsuficienteException extends RuntimeException {  
    public SaldoInsuficienteException(String message) {  
        super(message);  
    }  
}