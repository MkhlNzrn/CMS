package ru.abolsoft.core.common.exceptions;


public class DomainException extends ApplicationException {

    public DomainException(String domainName, String msg) {
        super(domainName + ": " + msg);
    }

    public static class NotImplemented extends NotImplementedException {
        public NotImplemented(String domainName) {
            super(domainName + "\n" + msg);
        }
        public NotImplemented() {
            super();
        }
    }
}


