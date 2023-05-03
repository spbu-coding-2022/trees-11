<img src=app/src/main/kotlin/app/resources/icon.png alt="logo" width="150" align="right">

# Tree Structure

Trees are a data structures that link nodes in a parent/child relationship, in the sense that there're nodes that depend on or come off other nodes. Each node contains a key and a value.


[![Apache License](https://img.shields.io/badge/license-Apache%202.0-black.svg)](https://www.apache.org/licenses/LICENSE-2.0)

![image](https://user-images.githubusercontent.com/113186929/235809712-99ff32ab-0c26-4095-8914-edbe727d6343.png)


## Build tool

The Gradle build tool is used to manage the project.
You only need to write one line to build a project:
```bash
   ./gradlew build
```
## How to use app

To run app  write one line to build a project:
```
./gradlew run
```

After launching the app, you will see three buttons:
 - `New`  Creates a new tree, you just have to type in a name and choose one of three tree types: Binary search Tree, AVL Tree, Red-Black Tree.
- `Open` Invites you to open a tree of three possible databases: Json, SQLite, Neo4j. Point and click on the database you want, and you will see a list of trees that already exist. Then choose the file you want.
- `Exit` Close the app.

After creating or opening a tree, a window will appear where you can:
- Insert key and value to your tree. If a node with this name already exists, its value will be overwritten with the new one.
- Remove the node with the entered key.
- Find the value of a node using a key.
- Save the tree in three possible databases: Json, SQLite, Neo4j. If you want to save the tree with an existing name, the app will overwrite the old file.




## How to use library
- `insert` - Inserts a node into the tree. It takes `Key` and `Value` and uses them to add. If a node with this name already exists, its value will be overwritten with the new one.
- `remove` - Removes a node from the tree. It accepts the `Key` and uses it to delete the node.
- `get` - Retrieves a given node. Use the `Key` to get the `Value`. If there is no such key in the tree the program will return null.


## How to use Data Bases

- ## Json
If you want to change the save folder of your tree, change `json_save_dir` value in the `trees-11/app/src/main/resources/Json.properties` file.

- ## SQLite
If you want to change the saving path of your tree, change `sqlite_path` value in the `trees-11/app/src/main/resources/SQLite.properties` file.
- ## Neo4j
You should download Neo4j Desktop [here](https://neo4j.com/).

Open app.

<p align="center"><img src=https://user-images.githubusercontent.com/113186929/235798341-28c05ee5-434c-4d63-9087-80ea0e6fe8d0.JPG width="900" align="justify"></p>
<p align="center"><img src=https://user-images.githubusercontent.com/113186929/235798381-fb7929bf-8e83-46d1-8318-f7691443726f.JPG width="900" align="justify"></p>
<p align="center"><img src=https://user-images.githubusercontent.com/113186929/235798400-15953498-ac08-4c79-96c2-c14ca39638b2.JPG width="900" align="justify"></p>
<p align="center"><img src=https://user-images.githubusercontent.com/113186929/235798416-f11c2dd0-2532-425f-942e-121c1622ed75.JPG width="900" align="justify"></p>

If you want to change uri, user name or password of your data Base, change relevant fields in the `trees-11/app/src/main/resources/Neo4j.properties` file.

