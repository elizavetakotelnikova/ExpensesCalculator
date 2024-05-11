package com.calculator.services;

import com.calculator.services.commands.Command;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Request {
    private Command command;
    private List<String> tokenizedLine;
}
