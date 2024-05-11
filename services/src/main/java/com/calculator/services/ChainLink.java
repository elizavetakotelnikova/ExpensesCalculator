package com.calculator.services;

import com.calculator.services.exceptions.IncorrectArgumentsException;

public interface ChainLink {
    ChainLink addNext(ChainLink link);
    void handle(Request request) throws IncorrectArgumentsException;
}
