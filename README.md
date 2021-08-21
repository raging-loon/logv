# logv
A log viewing application, used to view logs, compare, search, or otherwise analyze logs.


# Summary
This log viewing application(written in Java so it can be platform independant) will be able to do many things once it is finished.
  - Compare logs, looking for ip addresses, times
  - Identify directory brute forcing, traversal, xss, SQLi, etc
  - Look at Windows event logs
  - Detect Nmap scans
  - Produce frequency charts for various things
## Help
I am looking for help with this project, mostly the parsing of logs and functions related to them(such as sqli detection)
Please contact me at jgnovak1@protonmail.com if you would like to contribute or have any ideas about how to make this program better.
## Supported Log Files
  - Apache2/Nginx access.log
  - MySQL error.log
  - VSFTPD vsftpd.log

## Installation
The JAR file will be located at https://github.com/raging-loon/logv-jar
Follow the installation steps for you operating system. 
If you already are familiar with Java, you know what to do.
## What's happening now
Right now I am working on putting everything in a GUI with each log being seperated by tabs. After the GUI is up and running, I will work on more log files.
Also, the shell for headless systems is under construction.
![](https://github.com/raging-loon/logv/blob/main/screenshots/screenshot1.png)
  