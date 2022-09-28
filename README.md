# Java Shell
Implement a sh/bash/cmd application, set launch and command output redirection.

## Functional
* Console I/O
* Running programs with arguments, for example: ```java -cp . Hello.java```.
* Redirection from/to a file, between programs, for example: ```input > sort -r | uniq -c > output```.
* Conditional Program Execution, for example: ```test-f file && truncate -s 0 || touch file```.