# logv
A log viewing application, used to view logs, compare, search, or otherwise analyze logs.

# Summary
This log viewing application(written in Java so it can be platform in dependant) will be able to do many things once it is finished.
  - Compare logs, looking for ip addresses, times
  - Identify directory brute forcing, traversal, xss, SQLi, etc
  - Look at Windows event logs
  - Detect Nmap scans
  - Produce frequency charts for various things
## Supported Log Files
  - Apache2/Nginx access.log
  - MySQL error.log
  - VSFTPD vsftpd.log

## Bugs
 - SQLi detector is not working :(

## Plan
 - Every log should have it's own tab in a JTabbedPane object
 - Each pane should have the ability to produce a pop up that will display the log name, format, SHA256 hash, MD5 hash, number of entries, etc
 - 
  
