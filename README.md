# junit
Зачётное задание: 
 
Изготовить аналог библиотеки JUnit.
Необходимо реализовать следующее API - аннотация @Test с опциональным атрибутом ожидаемого исключения, аннотации @Before, @After, свои методы проверки условий (ассёрты). 
 
Система должна работать на множестве потоков. Каждый поток должен после запуска в бесконечном цикле: 
1) ожидать поступления в некоторой общей очереди нового имени класса с тестами 
2) выполнять эти тесты 
3) возвращать отчёт о прошедших/упавших тестах 
 
Запуск тестов должен осуществляться следующим командным интерфейсом: 

java -cp {your-junit-jar};{tested-classes} {your-main-class} N class-name [class-name]* 

N - количество потоков, в которых должны запускаться тесты. Остальные аргументы (произвольное количество) - полные имена классов, в которых находятся тесты.
