
# Tree Structure

Tree is a widely used abstract data type that represents a hierarchical tree structure with a set of connected nodes.


[![Apache License](https://img.shields.io/badge/license-Apache%202.0-black.svg)](https://www.apache.org/licenses/LICENSE-2.0)



## What's ready now:

 - Implemented the logic of three trees: AVLTree, RBTree, BSTree
 - We have written tests for each tree
 - Tree can be saved and read from neo4j !!!!

 ## What is planned to do:

 - Support saving and restoring trees from Json and SQL
 - Implement a user interface


## Build tool

The Gradle build tool is used to manage the project

The files with the source code must be located in the directory `src/main/kotlin` \
The test files must be located in the directory `src/test/kotlin`

Build code without running tests with command
```bash
./gradlew assemble
```

Build the code and run the tests with command
```bash
./gradlew test
```
## Neo4j

About neo4j read in [here](https://neo4j.com/).
