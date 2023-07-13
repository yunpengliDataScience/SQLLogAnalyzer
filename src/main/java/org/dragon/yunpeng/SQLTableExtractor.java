package org.dragon.yunpeng;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLTableExtractor {

    public static void main(String[] args) {
        String sqlQuery1 = "SELECT c.id, c.name, a.address FROM customers c INNER JOIN addresses a ON c.id = a.customer_id";
        //String sqlQuery1 = "SELECT * FROM employees, departments, others WHERE employees.department_id = departments.department_id";
    	//String sqlQuery1 = "SELECT * FROM employees JOIN departments ON employees.department_id = departments.department_id JOIN salaries ON employees.employee_id = salaries.employee_id";
    	
    	
        String sqlQuery2 = "INSERT INTO orders (customer_id, product_id, quantity) VALUES (1, 2, 10)";
        String sqlQuery3 = "UPDATE customers c, orders o SET c.name = 'John', o.quantity = 20 WHERE c.id = o.customer_id";
        String sqlQuery4 = "DELETE FROM orders WHERE id = 10";

        List<String> tableNames1 = extractTableNames(sqlQuery1);
        List<String> tableNames2 = extractTableNames(sqlQuery2);
        List<String> tableNames3 = extractTableNames(sqlQuery3);
        List<String> tableNames4 = extractTableNames(sqlQuery4);

        System.out.println("Table Names (SELECT): " + tableNames1);
        System.out.println("Table Names (INSERT): " + tableNames2);
        System.out.println("Table Names (UPDATE): " + tableNames3);
        System.out.println("Table Names (DELETE): " + tableNames4);
        
        //String sql = "SELECT * FROM employees, departments, others WHERE employees.department_id = departments.department_id";
        
    }

    /**
     *  Regular Expression:
     *  (?i)(?:FROM|JOIN|UPDATE|INSERT\\s+INTO)\\s+(\\w+(?:\\s*,\\s*\\w+)*)

		- `(?i)`: This is a flag that makes the regular expression case-insensitive. It allows the pattern to match keywords such as "FROM," "JOIN," "UPDATE," or "INSERT INTO" regardless of their case.

		- `(?:FROM|JOIN|UPDATE|INSERT\\s+INTO)`: This is a non-capturing group `(?:...)` that matches any of the specified keywords: "FROM," "JOIN," "UPDATE," or "INSERT INTO." The `|` character acts as a logical OR operator, allowing any of the specified keywords to match. The "INSERT INTO" keyword is included in this part.

		- `\\s+`: This matches one or more whitespace characters (spaces, tabs, etc.) after the keywords. It ensures that there can be multiple spaces between the keyword and the table names.

		- `(\\w+(?:\\s*,\\s*\\w+)*)`: This is a capturing group `(...)` that matches the table names. Let's break it down further:
  			- `\\w+`: This matches one or more word characters (alphanumeric characters or underscores). It captures the first table name.
  			- `(?:\\s*,\\s*\\w+)*`: This is a non-capturing group `(?:...)` followed by `*`, which allows for zero or more occurrences of the pattern inside it. It matches additional table names separated by commas and optional whitespace on both sides (`\\s*`). This allows multiple tables to be matched, capturing their names as well.

		Overall, the regular expression pattern `"(?i)(?:FROM|JOIN|UPDATE|INSERT\\s+INTO)\\s+(\\w+(?:\\s*,\\s*\\w+)*)"` captures the table names after the keywords "FROM," "JOIN," "UPDATE," or "INSERT INTO" in an SQL query. It handles case insensitivity, multiple spaces, and multiple tables separated by commas.
     
     * @param sqlQuery
     * @return List of table names
     */
    public static List<String> extractTableNames(String sqlQuery) {
        List<String> tableNames = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?i)(?:FROM|JOIN|UPDATE|INSERT\\s+INTO)\\s+(\\w+(?:\\s*,\\s*\\w+)*)");
        Matcher matcher = pattern.matcher(sqlQuery);

        while (matcher.find()) {
            String tables = matcher.group(1);
            String[] tableArray = tables.split("\\s*,\\s*");
            for (String table : tableArray) {
                if(!"Dual".equalsIgnoreCase(table)) {
                	tableNames.add(table);
                }
            }
        }

        return tableNames;
    }
}

