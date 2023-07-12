This is a utility project that can parse SQL log file and extract what tables involved in SQLs.

To create jar:
mvn clean package -DskipTests

To Run:
java -jar SQLLogAnalyzer.jar C:\Projects\SQLLogAnalyzer\src\main\resources\sql.log