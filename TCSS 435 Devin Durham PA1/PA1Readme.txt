Devin Durham
d1durham
TCSS 435
PA1
6/9/2017


board: |5|1|2|4|
       |6|A|3|7|
       |9| |C|8|
       |D|E|B|F|

board2: | |1|3|4|
        |5|2|6|8|
        |9|A|7|B|
        |D|E|F|C|


BFS:
   out: 11, 2490068, 769469, 2248236
   out2: 7, 1202, 373, 830
   Time: O(b^d) where b is the branching factor and d is the max depth
   Note: takes a very long time. not checking for visited nodes

DFS:
   board:
   |1|2|3|4|
   | |5|6|7|
   |9|A|B|8|
   |D|E|F|C|
   out: 6,14,5,9
   Time: O(b^d) where b is the branching factor and d is the max depth

GBFS:
   out(h1): 11, 27, 12, 16
   out(h2): 97, 423, 213, 211

   out2(h1): 7,15,6,10
   out2(h2):

   Time: O(b^d) where b is the branching factor and d is the max depth

A*:
   out(h1): 11, 27, 12, 16
   out(h2): 11, 65, 32, 34

   out2(h1): 7, 15, 6, 10
   out2(h2): 7, 15, 6, 10

   Time: O(b^d) where b is the branching factor and d is the max depth

DLS:
   board:
   |1|2|3|4|
   | |5|6|7|
   |9|A|B|8|
   |D|E|F|C|
   out: 6,14,5,9
   out2: 7, 433, 204, 11
   limit: 6
   Time: O(b^limit) where b is the branching factor and limit is the limited depth of the tree

   limit: 5
   out: Deapth limit reached. all nodes have been expanded

        -1, 0, 0, 0

