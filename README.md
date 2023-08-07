[discord-invite]: https://discord.gg/V6KB4kQnKc
<code><img align="right" src="https://cdn.discordapp.com/avatars/1024758270327533628/24e01d61e543269a5f5b754375bfade7.png?size=2048" height="200" width="200"></code>
## Java-Ticket-Bot
A Ticket bot has been thoughtfully crafted for Discord using the JDA (Java Discord API). This delightful bot offers a range of features, including the ability to view previous transcripts on the web.


## Navigation:
1. [Getting started](#getting-started)
2. [Dependencies](#dependencies)

## Getting started:
* Connect to your server using the SFTP client.
  * If a release is available, you can also use git on SSH
* Open the `/home` directory and upload your Build jar file.
  * If you want use the command `mkdir ticketBot` for our own path.
* Switch to your SSH client and navigate to the current path.
* Start your Build Java Document using the command: `java -jar (filename).jar`
  * You can also do this in a screen: `screen -R discord_ticket java -jar (filename).jar`
* The Java file will create all the necessary files you need.
  * Configuration Files: `.env & config.json`
* Use the command `nano .env` to open the .env file and insert your Token. (Save and close the file after editing)
  * Checkout the [Discord Developer Portal](https://discord.com/developers/docs/getting-started) 
* Use the command `nano config.json` to open the config.json file and set up your MySQL and Domain/IP for the Transcript API.

> This setup does not include the Transcript-Web function but will still work as well.

## Transcript:
Before installing apache2 for the web, update and upgrade your packages.
```ssh
sudo apt update && sudo apt upgrade -y
```
Install apache2 via SSH.
```ssh
sudo apt install apache2
```
> This is only the basic setup, for more security check out the [Digital Ocean](https://www.digitalocean.com/community/tutorials/how-to-install-the-apache-web-server-on-ubuntu-20-04) Wiki.


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
