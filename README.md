# ReplyCodeChallenge2020
It's the code We used at the ReplyCodeChallenge 2020.
# How to use it
javac Main.java
java Main "filename" number_of_tests
# Solution
It's based on number of attempts and getting lucky; basically we try a subset of Developers&ProjectManagers and we save the configuration and score, then we shuffle the array and retry, if the score is better we update the configuration, after n attempts we gave the best solution found.
