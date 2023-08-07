![logo](https://cdn.discordapp.com/attachments/1133772375368941628/1133773227458564116/772EBBB6-E091-4D95-964F-45A8652A0E5E.png)
## Java-Ticket-Bot

## Navigation:
1. [Config](#configure)
2. [Dependencies](#dependencies)

## Configure:
1. Verbinden Sie sich über den SFTP-Client mit Ihrem Server.
2. Öffnen Sie das Verzeichnis `/home` und laden Sie Ihr Build jar hoch.
3. Wechseln Sie zu Ihrem SSH-Client in den aktuellen Pfad.
4. Starten Sie `TicketBot-1.0.jar` mit `java -jar TicketBot-1.0.jar`.
5. Die Java Datei erstellt alle Dateien, die Sie benötigen.
6. Verwenden Sie `nano .env` und fügen Sie Ihren Token ein. (Speichern und Schließen)
7. Verwenden Sie `nano config.json` und verbinden Sie Ihre MySQL und Domain/IP für API
8. Starten Sie den Bot in einem screen `screen -R Tickets java -jar TicketBot-1.0.jar`.
9. Installieren Sie apache auf Ihrem Server.
10. Um Transcripts problemlos anzuzeigen folgen sie diesem Guide: https://gist.github.com/masudcsesust04/9e6e2b598e5eeab80dd80f2b5f54c1f1

***
Preview: http://193.187.255.169/1013213427843485817/1135155682920890439/

## ToDo's:
1. Eine messages.json um Nachrichten anzupassen. (evtl mehrsprachige Funktionen)
2. Ticket close log channel
3. vieles mehr

## Dependencies:
  * JDA (Java Discord API)
    * Version: `5.0.0-beta.12`
    * [GitHub](https://github.com/discord-jda/JDA)
  * slf4j-api (logback-classic)
    * Version: `1.2.8` 
    * [Website](https://www.slf4j.org/)
  * json-simple
    * Version: `1.1.1`
    * [GitHub](https://github.com/fangyidong/json-simple)
  * java-dotenv
    * Version: `2.3.2`
    * [GitHub](https://github.com/fangyidong/json-simple) 
  * MySQL Connector Java
    * Version: `8.0.33`
    * [GitHub](https://github.com/mysql/mysql-connector-j)

Leave a ⭐️ if you like this project!
