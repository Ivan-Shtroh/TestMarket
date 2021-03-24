import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Operations {
    /**
     * operation - метод выполняющий основные операции программы: поиск по ключевым параметрам и сбор статистики
     * @param inputPath  - путь по которому забирается файл запроса
     * @param outputPath - путь по которому отдается файл-ответ
     * @param operation  - тип операции - search или stat
     */
    public void operation(String inputPath, String outputPath, String operation) {
        if (operation.equalsIgnoreCase("search")) {
            try {
                search(inputPath, outputPath);
            } catch (FileNotFoundException e) {
                String s = ExceptionHandling.getFileNotFound();
                ExceptionHandling.response(s, outputPath);
            } catch (UnrecognizedPropertyException e) {
                String s = ExceptionHandling.getWrongRequest();
                ExceptionHandling.response(s, outputPath);
            } catch (JsonMappingException e) {
                String s = ExceptionHandling.getWrongMapping();
                ExceptionHandling.response(s, outputPath);
            } catch (NullPointerException e) {
                String s = ExceptionHandling.getEmptyParameter();
                ExceptionHandling.response(s, outputPath);
            } catch
            (IOException e) {
                e.printStackTrace();
            }
        } else if (operation.equalsIgnoreCase("stat")) {
            try {
                stat(inputPath, outputPath);
            } catch (FileNotFoundException e) {
                String s = ExceptionHandling.getFileNotFound();
                ExceptionHandling.response(s, outputPath);
            } catch (InvalidFormatException ex) {
                String s = ExceptionHandling.getInvalidDateFormat();
                ExceptionHandling.response(s, outputPath);
            } catch (UnrecognizedPropertyException e) {
                String s = ExceptionHandling.getWrongRequest();
                ExceptionHandling.response(s, outputPath);
            } catch (JsonMappingException e) {
                String s = ExceptionHandling.getWrongMapping();
                ExceptionHandling.response(s, outputPath);
            } catch (NullPointerException e) {
                String s = ExceptionHandling.getEmptyParameter();
                ExceptionHandling.response(s, outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String s = ExceptionHandling.getOperationTypeException();
            ExceptionHandling.response(s, outputPath);
        }
    }


    public void search(String inputPath, String outputPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Wrapper - объект вспомогательного класса-оболочки
        Wrapper wrapper = mapper.readValue(new File(inputPath), Wrapper.class);
        Criterias[] criteriasArray = wrapper.getCriterias();
        List<Result> generalResults = new ArrayList<>();

        try (Connection connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Test", "postgres", "123")) {

            //поиск по критериям:
            for (Criterias criterias : criteriasArray) {

                //Фамилия — поиск покупателей с этой фамилией:
                if (criterias.getLastName() != null) {
                    PreparedStatement preparedStatement = connect.prepareStatement(
                            "SELECT * FROM Customers WHERE lastName = ?");
                    String lastName = criterias.getLastName();
                    preparedStatement.setString(1, lastName);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    List<Customer> resultList = new ArrayList<>();
                    while (resultSet.next()) {
                        Customer customer = new Customer();
                        customer.setId(resultSet.getInt(1));
                        customer.setLastName(resultSet.getString(2));
                        customer.setFirstName(resultSet.getString(3));
                        resultList.add(customer);
                    }
                    Result result = new Result(criterias, resultList.toArray(new Customer[0]));
                    generalResults.add(result);

                    // Название товара и число раз — поиск покупателей,
                    // купивших этот товар не менее, чем указанное число раз:
                } else if (criterias.getProduct() != null && criterias.getCountBuy() != 0) {
                    PreparedStatement preparedStatement = connect.prepareStatement(
                            "SELECT * \n" +
                                    "FROM Customers\n" +
                                    "where id IN(SELECT customer_id \n" +
                                    "FROM Purchases pur  \n" +
                                    "\n" +
                                    "JOIN Products prod \n" +
                                    "ON (pur.product_id = prod.product_id) \n" +
                                    "WHERE product = ?\n" +
                                    "group by customer_id\n" +
                                    "having  count(product)>=?)");
                    preparedStatement.setString(1, criterias.getProduct());
                    preparedStatement.setInt(2, criterias.getCountBuy());
                    ResultSet resultSet = preparedStatement.executeQuery();

                    List<Customer> resultList = new ArrayList<>();
                    while (resultSet.next()) {
                        Customer customer = new Customer();
                        customer.setId(resultSet.getInt(1));
                        customer.setLastName(resultSet.getString(2));
                        customer.setFirstName(resultSet.getString(3));
                        resultList.add(customer);
                    }
                    Result result = new Result(criterias, resultList.toArray(new Customer[0]));
                    generalResults.add(result);

                    //Минимальная и максимальная стоимость всех покупок — поиск покупателей, у которых
                    //общая стоимость всех покупок за всё время попадает в интервал:
                } else if (criterias.getMinPrice() != null && criterias.getMaxPrice() != null) {
                    PreparedStatement preparedStatement = connect.prepareStatement(
                            "SELECT * FROM customers\n" +
                                    "WHERE id IN (SELECT customer_id\n" +
                                    "FROM Purchases pur\n" +
                                    "JOIN Products prod\n" +
                                    "ON (pur.product_id = prod.product_id)\n" +
                                    "GROUP BY customer_id\n" +
                                    "HAVING sum(price)>? AND sum(price)<?)"
                    );

                    preparedStatement.setBigDecimal(1, criterias.getMinPrice());
                    preparedStatement.setBigDecimal(2, criterias.getMaxPrice());
                    ResultSet resultSet = preparedStatement.executeQuery();

                    List<Customer> resultList = new ArrayList<>();
                    while (resultSet.next()) {
                        Customer customer = new Customer();
                        customer.setId(resultSet.getInt(1));
                        customer.setLastName(resultSet.getString(2));
                        customer.setFirstName(resultSet.getString(3));
                        resultList.add(customer);
                    }
                    Result result = new Result(criterias, resultList.toArray(new Customer[0]));
                    generalResults.add(result);

                    //Число пассивных покупателей — поиск покупателей, купивших меньше всего товаров.
                    //Возвращается не более, чем указанное число покупателей:
                } else if (criterias.getCountPassiveBuyer() != null) {
                    PreparedStatement preparedStatement = connect.prepareStatement(
                            //получим список ID покупателей, отсортированный по числу купленных товаров
                            //(от меньшего числа покупок к большему):
                            "SELECT customer_id, count(product_id)\n" +
                                    "FROM  purchases\n" +
                                    "GROUP BY customer_id\n" +
                                    "ORDER BY count(product_id)"
                    );
                    ResultSet resultSet = preparedStatement.executeQuery();
                    List<Customer> resultList = new ArrayList<>();
                    while (resultSet.next()) {
                        Customer customer = new Customer();
                        customer.setId(resultSet.getInt(1));
                        resultList.add(customer);
                    }

                    int lim = criterias.getCountPassiveBuyer();
                    if (lim < resultList.size()) {
                        resultList = resultList.stream().limit(lim).collect(Collectors.toList());
                    }

                    //получим данные о заданном количестве покупателей:
                    PreparedStatement preparedStatement2 = connect.prepareStatement(
                            "SELECT * FROM  customers WHERE id = ?");

                    for (Customer i : resultList) {
                        int id = i.getId();
                        preparedStatement2.setInt(1, id);
                        ResultSet resultSet2 = preparedStatement2.executeQuery();
                        while (resultSet2.next()) {
                            i.setId(resultSet2.getInt(1));
                            i.setLastName(resultSet2.getString(2));
                            i.setFirstName(resultSet2.getString(3));
                        }
                    }
                    Result result = new Result(criterias, resultList.toArray(new Customer[0]));
                    generalResults.add(result);

                }
            }
            //Запись результата поиска по всем заданным критериям:
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputPath), new writeSearchOutput("search", generalResults.toArray(new Result[0])));
        } catch (SQLException e) {
            String s = ExceptionHandling.getSQLException();
            ExceptionHandling.response(s, outputPath);
        }
    }

    public void stat(String inputPath, String outputPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Stat stat = mapper.readValue(new File(inputPath), Stat.class);
        //определим рабочие дни
        List<LocalDate> workDays = Stat.getWorkDays(stat);


        try (Connection connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Test", "postgres", "123")) {

            PreparedStatement preparedStatement = connect.prepareStatement(
                    "SELECT * FROM Customers");
            ResultSet resultSet = preparedStatement.executeQuery();

            //Список всех покупателей
            List<Customer> allTheCustomerList = new ArrayList<>();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt(1));
                customer.setLastName(resultSet.getString(2));
                customer.setFirstName(resultSet.getString(3));
                allTheCustomerList.add(customer);
            }
            String SQL = "SELECT product, SUM(price)\n" +
                    "FROM products prod\n" +
                    "JOIN purchases p\n" +
                    "ON (prod.product_id = p.product_id)\n" +
                    "where p.customer_id =? AND p.purchase_date IN (?)\n" +
                    "group by prod.product";
            //список дат - рабочих дней в виде строки (с учетом форматирования):
            String sqlIN = workDays.stream()
                    .map(x -> String.valueOf(x))
                    .collect(Collectors.joining("','yyyy-MM-dd'),TO_DATE('", "(TO_DATE('", "','yyyy-MM-dd'))"));

            SQL = SQL.replace("(?)", sqlIN);

            //Лист покупок для каждого покупателя: продукт и общая сумма покупок этого товара за весь период
            for (Customer customer : allTheCustomerList) {
                List<Purchases> purchasesList = new ArrayList<>();

                PreparedStatement preparedStatement2 = connect.prepareStatement(SQL);
                preparedStatement2.setInt(1, customer.getId());
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()) {
                    Purchases pur = new Purchases();
                    pur.setProduct(resultSet2.getString(1));
                    pur.setSumExpensesProduct(resultSet2.getBigDecimal(2));
                    purchasesList.add(pur);
                }
                customer.setPurchases(purchasesList);
            }
            //суммарная и средняя стоимость покупок для всех покупателей за указанный период
            double customerTotalExpenses = 0.0;
            double customerAVGExpenses = 0.0;

            //суммарная стоимость
            for (Customer cust : allTheCustomerList) {
                customerTotalExpenses += cust.getPurchases().stream().mapToDouble(i -> i.getSumExpensesProduct().doubleValue()).sum();
            }
            //средняя стоимость 
            customerAVGExpenses = customerTotalExpenses / allTheCustomerList.size();

            // запись статистики в файл
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputPath), new writeStatOutput("stat", workDays.size(),
                    allTheCustomerList.toArray(new Customer[0]),
                    new BigDecimal(customerTotalExpenses).setScale(2, BigDecimal.ROUND_HALF_UP),
                    new BigDecimal(customerAVGExpenses).setScale(2, BigDecimal.ROUND_HALF_UP)));


        } catch (SQLException e) {
            String s = ExceptionHandling.getSQLException();
            ExceptionHandling.response(s, outputPath);
        }
    }
}




