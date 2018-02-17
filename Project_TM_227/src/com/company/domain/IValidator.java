package com.company.domain;

public interface IValidator<V> {
    void validate(V value);
}
