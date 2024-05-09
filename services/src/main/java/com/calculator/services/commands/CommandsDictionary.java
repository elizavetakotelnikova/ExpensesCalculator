package com.calculator.services.commands;

import com.calculator.services.services.CategoryService;
import com.calculator.services.services.TransactionService;
import com.calculator.services.services.Validator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.util.Map;
@Component
public class CommandsDictionary {
    private final ApplicationContext applicationContext;
    @Getter
    private Map<String, Command> commandMap;
    @Autowired
    public CommandsDictionary(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initializeMap();
    }
    private void initializeMap() {
        commandMap.put("add category", new AddCategoryCommand(applicationContext.getBean(CategoryService.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("add mcc to category", new UpdateCategoryCommand(applicationContext.getBean(CategoryService.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("add group to category", new IncludeCategoryGroupCommand(applicationContext.getBean(CategoryService.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("remove category", new DeleteCategoryCommand(applicationContext.getBean(CategoryService.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("add transaction", new AddTransactionCommand(applicationContext.getBean(TransactionService.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("remove transacion", new DeleteTransactionCommand(applicationContext.getBean(TransactionService.class),
                applicationContext.getBean(Validator.class)));
        commandMap.put("show categories", new ShowCategoriesCommand(applicationContext.getBean(CategoryService.class)));
    }
}
