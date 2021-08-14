# LOG SHELL
This is the rules for the log viewer shell.
## Syntax
The syntax for the shell are very similar to that of SQL and shell script:
```sql
SELECT request FROM xyzLogFile WHERE ipaddress = "192.168.0.1"
newlog -apache2 /var/log/apache2/access.log -n log1
show logs;
log1 -parse
log1 -attackid xss -save /home/user/.logv/log1.xss.xml
