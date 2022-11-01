# Jump61B
A two-person board game where the goal is to fill every square on the board with your color. The rules are as follows: every turn a player adds a dot to a square that is filled with their color or not filled with any color. Then if it is overfull (contains more dots than it has neighbors), it distributes these dots to its neighbors, and the overfull rules then apply to its neighbors, and so on. In addition to the board game itself, I also implemented an AI using alpha beta pruning with heuristic and minimax algorithms in order to beat the human player in a set amount of moves.

An in-depth explanation at the project spec: https://inst.eecs.berkeley.edu/~cs61b/fa21/materials/proj/proj2/index.html
