package org.dragon.yunpeng;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.ArrayList;
import java.util.List;

//TODO
public class JSQLParser {
    public static void main(String[] args) {
        // Example 1: Parsing an INSERT statement
        String insertSql = "INSERT INTO customers (id, name) VALUES (1, 'John Doe')";
        parseInsertStatement(insertSql);

        // Example 2: Parsing an UPDATE statement
        String updateSql = "UPDATE customers SET name = 'Jane Doe' WHERE id = 1";
        parseUpdateStatement(updateSql);

        // Example 3: Parsing a DELETE statement
        String deleteSql = "DELETE FROM customers WHERE id = 1";
        parseDeleteStatement(deleteSql);

        // Example 4: Parsing a SELECT statement
        String selectSql = "SELECT * FROM customers WHERE age > 25";
        parseSelectStatement(selectSql);
    }

    private static void parseInsertStatement(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (statement instanceof Insert) {
                Insert insertStatement = (Insert) statement;

                // Extract table name from the INSERT statement
                String tableName = insertStatement.getTable().getName();

                // Extract column names from the INSERT statement
                List<String> columnNames = new ArrayList<String>();
                List<Column> columns = insertStatement.getColumns();
                
                for(Column col: columns) {
                	columnNames.add(col.getColumnName());
                }

                // Print the extracted information
                System.out.println("Table Name: " + tableName);
                System.out.println("Column Names: " + columnNames);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseUpdateStatement(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (statement instanceof Update) {
                Update updateStatement = (Update) statement;

                TablesNamesFinder tablesNamesFinder=new TablesNamesFinder();
                // Extract table names from the UPDATE statement
                List<String> tableNames = tablesNamesFinder.getTableList(updateStatement);

                // Print the extracted table names
                System.out.println("Table Names: " + tableNames);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseDeleteStatement(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (statement instanceof Delete) {
                Delete deleteStatement = (Delete) statement;

                // Extract table name from the DELETE statement
                String tableName = deleteStatement.getTable().getName();

                // Print the extracted table name
                System.out.println("Table Name: " + tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseSelectStatement(String sql) {
        try {
            Statement statement = CCJSqlParserUtil.parse(sql);

            if (statement instanceof Select) {
                Select selectStatement = (Select) statement;

                // Extract table names from the SELECT statement
                List<String> tableNames;
                TablesNamesFinder tablesNamesFinder=new TablesNamesFinder();
				try {
					tableNames = tablesNamesFinder.getTableList(selectStatement);
					
					
					
					// Print the extracted table names
	                System.out.println("Table Names: " + tableNames);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

