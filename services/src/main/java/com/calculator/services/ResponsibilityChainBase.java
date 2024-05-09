package com.calculator.services;

import com.calculator.services.exceptions.IncorrectArgumentsException;
import lombok.Getter;

@Getter
public abstract class ResponsibilityChainBase implements ChainLink
{
    protected ChainLink next;
    public ChainLink addNext(ChainLink link)
    {
        if (next == null) next = link;
        else next.addNext(link);
        return this;
    }

    public abstract void handle(Request request) throws IncorrectArgumentsException;
}
