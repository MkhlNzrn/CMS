package ru.abolsoft.core.workspace.exceptions;

public class CyclicRecursionDetected extends WorkspaceDomainException {
    public CyclicRecursionDetected() {
        super("cyclic recursion detected");
    }
    public CyclicRecursionDetected(String msg) {
        super(msg);
    }
}


