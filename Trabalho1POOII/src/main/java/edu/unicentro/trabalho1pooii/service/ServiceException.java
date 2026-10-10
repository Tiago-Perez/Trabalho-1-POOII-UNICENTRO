package edu.unicentro.trabalho1pooii.service;

public class ServiceException extends Exception {
    private static final long serialVersionUID = 1L;

    public ServiceException(String mensagem) {
        super(mensagem);
    }

    public ServiceException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}