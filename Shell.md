# LOG SHELL
This is the rules for the log viewer shell.
## Syntax
The syntax for the shell is somewhat reminiscent of metasploit:
```bash
logv > newlog apache2 /var/log/apache2/access.log
logv > show logs
+-+-+-+-+-+-+-+-+-+-+-+-+-+
+ log1-apache2 | apache2  + 
+-+-+-+-+-+-+-+-+-+-+-+-+-+
logv > use log1-apache2
logv(log1-apache2) > sqli all
...
...
logv(log1-apache2) > back
logv >
```
