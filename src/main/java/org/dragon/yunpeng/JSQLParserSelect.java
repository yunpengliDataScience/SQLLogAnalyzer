package org.dragon.yunpeng;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import java.util.ArrayList;
import java.util.List;

public class JSQLParserSelect {
	public static void main(String[] args) {
		// Example: Parsing a SELECT statement with multiple table joins
		String selectSql = "SELECT c.name, o.order_date FROM customers c " + "JOIN orders o ON c.id = o.customer_id "
				+ "JOIN items i ON o.id = i.order_id " + "WHERE c.age > 25";

		// String selectSql = "select c.id, c.name, a.address from customers c inner join addresses a on c.id = a.customer_id where c.id = 1";
		
		parseSelectStatement(selectSql);
	}

	private static void parseSelectStatement(String sql) {
		try {
			Statement statement = CCJSqlParserUtil.parse(sql);

			if (statement instanceof Select) {
				Select selectStatement = (Select) statement;

				// Extract table names from the SELECT statement
				List<Pair<String, String>> tableAliasAndNameList = extractTableNames(selectStatement.getSelectBody());

				// Extract column names from the SELECT statement
				List<Pair<String, String>> tableAliasColumnNamePairList = extractColumnNames(
						selectStatement.getSelectBody());

				// Print the extracted information
				System.out.println("Table Alias and Names: " + tableAliasAndNameList);
				System.out.println("Table Alias and Column Names: " + tableAliasColumnNamePairList);

				List<Pair<String, List<String>>> tableAndSelectedColumns = getTableAndSelectedColumns(
						tableAliasAndNameList, tableAliasColumnNamePairList);

				System.out.println("Table and its selected columns: " + tableAndSelectedColumns);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<Pair<String, List<String>>> getTableAndSelectedColumns(
			List<Pair<String, String>> tableAliasAndNameList, List<Pair<String, String>> tableAliasColumnNamePairList) {
		List<Pair<String, List<String>>> tableAndSelectedColumns = new ArrayList<Pair<String, List<String>>>();

		for (Pair<String, String> tableAliasAndNamePair : tableAliasAndNameList) {
			String tableAlias = tableAliasAndNamePair.getKey();
			String tableName = tableAliasAndNamePair.getValue();

			List<String> columns = new ArrayList<String>();

			for (Pair<String, String> tableAliasColumnNamePair : tableAliasColumnNamePairList) {
				String tAlias = tableAliasColumnNamePair.getKey();
				String colName = tableAliasColumnNamePair.getValue();

				if (tAlias != null && tAlias.equalsIgnoreCase(tableAlias)) {
					columns.add(colName);
				}
			}

			Pair<String, List<String>> tableColListPair = new Pair<String, List<String>>(tableName, columns);

			tableAndSelectedColumns.add(tableColListPair);
		}

		return tableAndSelectedColumns;
	}

	private static List<Pair<String, String>> extractTableNames(SelectBody selectBody) {
		List<Pair<String, String>> tableAliasAndNameList = new ArrayList<Pair<String, String>>();

		if (selectBody instanceof PlainSelect) {
			PlainSelect plainSelect = (PlainSelect) selectBody;

			FromItem fromItem = plainSelect.getFromItem();

			extractTableNamesRecursively(fromItem, tableAliasAndNameList);

			List<Join> joins = plainSelect.getJoins();
			if (joins != null) {
				for (Join join : joins) {
					fromItem = join.getRightItem();
					extractTableNamesRecursively(fromItem, tableAliasAndNameList);
				}
			}
		}

		return tableAliasAndNameList;
	}

	private static void extractTableNamesRecursively(FromItem fromItem,
			List<Pair<String, String>> tableAliasAndNameList) {
		if (fromItem instanceof Table) {
			Table table = (Table) fromItem;

			Alias alias = fromItem.getAlias();
			String aliasName = alias.getName();
			String tableName = table.getName();

			Pair<String, String> pair = new Pair<String, String>(aliasName, tableName);

			tableAliasAndNameList.add(pair);
		} else if (fromItem instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) fromItem;
			SelectBody subSelectBody = subSelect.getSelectBody();
			extractTableNames(subSelectBody);
		}
	}

	private static List<Pair<String, String>> extractColumnNames(SelectBody selectBody) {
		List<Pair<String, String>> tableAliasColumnNamePairList = new ArrayList<Pair<String, String>>();

		if (selectBody instanceof PlainSelect) {
			PlainSelect plainSelect = (PlainSelect) selectBody;

			List<SelectItem> selectItems = plainSelect.getSelectItems();
			for (SelectItem selectItem : selectItems) {
				if (selectItem instanceof SelectExpressionItem) {
					SelectExpressionItem expressionItem = (SelectExpressionItem) selectItem;

					Alias alias = expressionItem.getAlias();

					String columnExpression = expressionItem.getExpression().toString();
					String[] tableAliasColNamePair = columnExpression.split("\\.");

					String tableAlias = tableAliasColNamePair[0]; // TODO: the case when there is no table alias
					String columnName = tableAliasColNamePair[1];

					Pair<String, String> pair = new Pair<String, String>(tableAlias, columnName);

					tableAliasColumnNamePairList.add(pair);
				}
			}
		}

		return tableAliasColumnNamePairList;
	}
}
