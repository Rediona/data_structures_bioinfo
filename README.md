# Description
This is an app that accepts input from a user and answers if two nodes in a network  are connected (e.g if they are friends), and if so, it calculates their distance.

The network is supplied by the user in the form of a text file, and it is then represented through a Compressed Sparse Row, Graph Representation. The algorithms implemented are BFS and biderectional BFS.
## Technical points

### Executing the application through the command line

If you're interested in executing this application in your command line, you'll need to have an instance of the Java JDK installed in your computer. Given that your Java installation is up and running you can procced as follows:

* uzip and save the Main.java and Pair.java files
* the tab separated text files included,wiki-Vote and soc-Epinions contain the
nodes and edges of the networks
* or else, download the same files from :
https://snap.stanford.edu/data/wiki-Vote.html
https://snap.stanford.edu/data/soc-Epinions1.html
* open cmd on Windows, or terminal on Linux

Then, to compile the java classes you can run:

```
javac Main.java Pair.java
```

To execute the code and try your search, you must add the name of the text file with your preferred network. You can run:

```
java Main wiki-Vote.txt  
or
java Main socEpinions.txt 
```
You can then follow the instructions given and try your search.

## Contributors
Petros Tamvakis

Rediona Kane
