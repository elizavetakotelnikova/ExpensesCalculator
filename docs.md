Запуск приложения:
- сначала надо запустить docker-compose.yml;
- дальше перейти к точке запуска (входа) в приложение: ExpensesCalculatorApplication.java;
- тесты: ExpensesCalculatorApplicationTests.java && TransactionTests.java 

### Команды:
**Добавить категорию трат:**
add category <category name> <mcc> [mcc2] [mcc3] ...

**Добавить mcc код к категории:**
add mcc to <category name> <mcc> [mcc2] [mcc3] ...

**Включить одну категорию в другую:**
add group to category <category mame> <category to add> [category to add] ...

**Удалить категорию:**
remove <category name>

**Показать все категории:**
show categories

**Добавить транзакцию:**
add transaction <name> <value> <month> [mccCode]

**Удалить транзакцию:**
remove transaction <name> <value> <month> (удаляет первую из найденных)

**Показать все траты в выбранном месяце**
show expenses in <month>

**Показать траты в выбранной категории по месяцам**
show monthly expenses in <category name>

**Примечания**
- Имя категории уникально