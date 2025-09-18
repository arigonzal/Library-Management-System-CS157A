# Library-Management-System-CS157A
- Project Overview:
This project is a system to help a library manage its collection of books and members, enable members to check out and return books in the system, and track current book checkouts and any fines acquired by a member.

- Setting up and running the project:
1. Create a regular Java project and replace the src folder with the program's src folder
2. Setting up the JDBC driver (instructions are based on Eclipse IDE):
    - Navigate to https://dev.mysql.com/downloads/connector/j/
    - Select 'Platform Independent' in the drop down
    - Download the ZIP archive option
    - Extract the file in your location of choice
    - Open your file explorer in Eclipse IDE
    - Right click on the project folder
    - Find and hover over 'Build Path'
    - Click on 'Configure Build Path...'
    - Select the tab labeled 'Libraries'
    - Select either 'Modulepath' or 'Classpath'
    - Look to the right and click 'Add External JARs...'
    - Find the extracted file mentioned previously open it and double click file named 'mysql-connector-j-9.3.0.jar' (the version may differ from the one listed here)
    - Click apply and then Apply and Close
    - The JDBC driver is now installed for the project
3. Setting up the database (instructions based on Apple Silicon macOS):
    - Prerequisite: have Homebrew installed, this allows for easy installation MySQL
    - Open the terminal
    - Execute the command 'brew install mysql' and let it finish installing mysql
    - Navigate to https://dev.mysql.com/downloads/workbench/
    - Ensure that the macOS operating system is selected
    - Download the ARM, 64-bit DMG Archive
    - Install MySQL Workbench using the dmg
    - Open the terminal
    - Execute the command 'brew services start mysql', this starts mysql
    - Open MySQL Workbench and select the default and should be only connections listed labeled 'Local instance 3306'
    - Next go under File at the top of the window and click 'Open SQL Script...'
    - Do this for both the 'LibrarySystemSchem.sql' and 'StartinData.sql' files
    - Execute each file by clicking on the first lightning bolt right under the file names in MySQL Workbench
    - The queries should execute without fail and the schema and its initial data should be loaded
4. With everything setup and running, run the Main.java file found the ui folder in the src folder.  If everything was setup properly, a window should appear.
5. This window is how you will interact with the database and allow you to select, insert, update, and delete information from the database.
6. To close the program, click the 'x' button found at the top of the window until you are met with the Home Page.  Here click Exit to safely close the program.
7. If you are completely done using the program, open the terminal and execute the command 'brew services stop mysql'.  This will shut down mysql.
8. Have fun using the program!

If there are any problems getting the Java project and the connector working, this video https://youtu.be/y1IU65ffx7A?si=LFP6AUelVYkcoj-P was very useful in helping the team members get it setup.
