package ru.abolsoft.core.common;

public interface UseCase<TResponse , TRequest> {
    public TResponse execute(TRequest req);
}
