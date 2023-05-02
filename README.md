
# Tree Structure

Trees are a data structures that link nodes in a parent/child relationship, in the sense that there're nodes that depend on or come off other nodes. Each node contains a key and a value.


[![Apache License](https://img.shields.io/badge/license-Apache%202.0-black.svg)](https://www.apache.org/licenses/LICENSE-2.0)



### What's ready now:

 - Implemented the logic of three trees: AVLTree, RBTree, BSTree
 - Tree can be saved and read from neo4j, json, SQLite.

 ### What's planned to do:

 - Implement a user interface
 - Implement a controller



## Build tool

The Gradle build tool is used to manage the project.
You only need to write one line to build a project
```bash
  gradle build
```
## Usage

- `insert` - Inserts a node in tree. It accepts the `Key` and `Value` and uses them to insert
- `remove` - Removes a node from the tree. It accepts the `Key` and uses it to delete the node
- `get` - Retrieves a given node. Use the `Key` to get the `Value`. If there is no such key in the tree the program will return null.

Implemented saving to databases, such as Json, Neo4j, SQLite.\
 Usage guide will be soon. 

## Neo4j

About neo4j read in [here](https://github.com/neo4j/neo4j).
