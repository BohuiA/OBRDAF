# OBRDAF - Query Object Pattern
Object Based Rational Database Application Framework - Query Object Pattern

ABSTRACT:
An object that represents a database query.
For a full description see <pre> Patterns of Enterprise Application Architecture </pre> by Martin Fowler.

DESCRIPTION:
SQL can be an involved language, and many developers aren't particularly familiar with it. Furthermore, you need to know what the database schema looks like to form queries. You can avoid this by creating specialized finder methods that hide the SQL inside parameterized methods, but that makes it difficult to form more ad hoc queries. It also leads to duplication in the SQL statements should the database schema change.

A Query Object is an interpreter [Gang of Four], that is, a structure of objects that can form itself into a SQL query. You can create this query by refer-ring to classes and fields rather than tables and columns. In this way those who write the queries can do so independently of the database schema and changes to the schema can be localized in a single place.

NOTE: 
All outputs from Query Object Pattern are streamed out by java.util.logging.Logger library. 
In order to get all logging messages in console, users need to enable LOGGER level to at least Level.INFO level.

Example: 
In the root application class, adding following statements to include logger class and setting global logger handlers'
levels.

<pre>
Logger rootLogger = LogManager.getLogManager().getLogger("");
rootLogger.setLevel(Level.INFO);
for (Handler h : rootLogger.getHandlers()) {
    h.setLevel(Level.INFO);
}
</pre>