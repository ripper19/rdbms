**Simple In-Memory RDBMS(Relational Database Management System) using Java**

A lightweight, in-memory relational database management system built entirely in Java. This project demonstrates the fundamental concepts and internals of how databases work, with a focus on simplicity and educational value.

**Features**
**Table Schemas** - Defines tables with columns,rows with indexing.
**Basic Crud Operations**:
- CREATE TABLE (Define table structure)
- Insert (Add new Records)
- SELECT (Query data(CURRENTLY Supports 'SELECT ALL'))
- UPDATE (UUpdate Records)
- DELETE (Delete Records)
**Data Integrity**: 
- Primary key Constraints
- Basic type safety
- Unique constraints
**REPL Interface** - Interactive CLI for db interaction
**In-Memory Storage** - Data is stored in memory, not Persistent

**Getting Started**

**Prerequisites**:
- Java 17 or higher
- Basic Java Development Environment

**Installation**
bash
```markdown
git clone https://github.com/ripper19/rdbms.git
cd rdbms/src
```

**Compile**
```markdown
javac Main.java
```
**Run the db**
```markdown
java Main.java
```
**SYNTAX**
```markdown
CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR, email VARCHAR UNIQUE)

INSERT INTO users (name, email, id) VALUES('John Doe', 'john@example.com', 30)

UPDATE users SET(name='John') WHERE(id=1)

DELETE FROM users WHERE (id=1)

SHOW TABLES

SELECT ALL FROM users

These Deviate slightly from normal sql syntax, this was done for ease during parsing. If a statement here doesnt work as intended 
Check Regex pattern in parser or type "help" in REPL for more concrete commands for testing.
```
**File Structure**
.
├── Column.java              - Column Definitions

├── CreateTableStatement.java - CreateTable Statement

├── Database.java             - Database Engine

├── DataType.java             - Defines for Datatypes

├── DeleteStatement.java      - Delete Table Statement

├── Index.java                - Describes primary and unique Indexes

├── InsertStatement.java      - Insert Statement

├── Main.java                 - Contains REPL

├── ParseColumns.java         - Parses columns during creation with constraints

├── Parser.java               - Parser for data

├── Row.java                  - Defines Row and Constraints

├── SelectAllStatement.java   - Select Statement

├── ShowStatement.java        - Show all tables Statement

├── Statement.java            - Abstract class for Statements defining core methods

├── Table.java                - Defines Table behaviour

└── UpdateStatement.java      - Update Statement


**Current Limitations**
- No persistent storage (in-memory only)
- Basic query capabilities
- Limited error handling

**Learning Objectives**
This project serves as an educational tool to understand:
- How databases store and organize data
- The implementation of basic SQL operations
- Type safety and data validation in databases
- Indexing and constraint enforcement
- Database engine architecture basics


