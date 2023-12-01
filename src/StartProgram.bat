rem Enter the file path to your folder on the next line, and then delete this line
rem C:\Users\BDK\IdeaProjects\OSMakeupProj\src
cd "C:\Users\BDK\IdeaProjects\OSMakeupProj\src"
javac *.java
start cmd /k java Slave_A
timeout 3
start cmd /k java Slave_B
timeout 3
start cmd /k java Slave_C
timeout 3
start cmd /k java Slave_D
timeout 3
start cmd /k java Master
timeout 3
FOR /L %%x IN (0 1 3) DO start cmd /k java Client
exit