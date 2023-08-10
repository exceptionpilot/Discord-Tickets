Pre-Release beta-1.2.0 out now!

<code><img align="right" src="https://cdn.discordapp.com/avatars/1024758270327533628/24e01d61e543269a5f5b754375bfade7.png?size=2048" height="200" width="200"></code>
[![Discord Shield](https://discordapp.com/api/guilds/1013213427843485817/widget.png?style=shield)](https://discord.gg/V6KB4kQnKc)
## Java-Ticket-Bot
Introducing our meticulously crafted Ticket bot for Discord, powered by the JDA (Java Discord API). This delightful bot offers various features, including web-based access to previous transcripts. Enhance your server experience with seamless ticket management and effortless interaction. Enjoy the convenience and versatility it brings to your Discord community!


## Summary:
1. [Getting started](#getting-started)
2. [Transcript Guide](#transcript-guide)
3. [Commands](#commands)
4. [Screenshots](#screenshots)
5. [Tasks](#tasks)
6. [Dependencies](#dependencies)

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

## Transcript Guide:
Before installing apache2 for the web, update and upgrade your packages.
```ssh
$ sudo apt update && sudo apt upgrade -y
```
Install apache2 via SSH.
```ssh
$ sudo apt install apache2
```
> This is only the basic setup, for more security check out the [Digital Ocean](https://www.digitalocean.com/community/tutorials/how-to-install-the-apache-web-server-on-ubuntu-20-04) Wiki.
Open apache2 configuration file in the nano Editor.
```ssh
## Input
$ sudo nano /etc/apache2/apache2.conf

## Output
<Directory /var/www/>
        Options Indexes FollowSymLinks
        AllowOverride None
        Require all granted
</Directory>
```
Change output to:
```ssh
<Directory /path/to/transcript/>
        Options Indexes FollowSymLinks
        AllowOverride None
        Require all granted
        Allow from all
</Directory>
```
Open the default virtual host configuration file from the path.
```ssh
## Input
$ sudo nano /etc/apache2/sites-available/000-default.conf

## Output
DocumentRoot /var/www/html
```
Change output to:
```ssh
DocumentRoot /path/to/transcript/
```
Finally, restart apache2.
```ssh
$ sudo service apache2 restart
```
Well done😄 Enjoy!

## Commands:
Command       | Description
------------- | -------------
/config       | Change Guild configurations
/open         | Open a Ticket via Command
/close        | Close a Ticket via Command
/add          | Add a Member to a current ticket
/remove       | Remove a Member from a current ticket
/claim        | Show that you are progressing with this ticket
/topic        | Change the ticket Topic

## Screenshots:
<details> <summary>Create a new Ticket for you or others.</summary>
 <img align="middle" src="https://github.com/DevChewbacca/Java-Ticket-Bot/blob/main/images/create-ticket.png" height="200">
 <img align="middle" src="https://github.com/DevChewbacca/Java-Ticket-Bot/blob/main/images/create-for-others.png" height="200">
</details>
<details> <summary>Ticket in progress.</summary>
 <img align="middle" src="https://github.com/DevChewbacca/Java-Ticket-Bot/blob/main/images/ticket-progress.png" height="200">
 <img align="middle" src="https://github.com/DevChewbacca/Java-Ticket-Bot/blob/main/images/ticket-topic.png" height="200">
 <img align="middle" src="https://github.com/DevChewbacca/Java-Ticket-Bot/blob/main/images/add-remove-member.png" height="200">
</details>
<details> <summary>Transcript view and Announcement.</summary>
 <img align="middle" src="https://github.com/DevChewbacca/Java-Ticket-Bot/blob/main/images/dm-transcript.png" height="200">
 <img align="middle" src="https://github.com/DevChewbacca/Java-Ticket-Bot/blob/main/images/web-transcript-view.png" height="200">
</details>
<details> <summary>Syntax for bad usage.</summary>
 <img align="middle" src="https://github.com/DevChewbacca/Java-Ticket-Bot/blob/main/images/not-in-ticket.png" height="200">
</details>

## Tasks:
* [ ] Including messages.json for custom messages setup.
* [ ] Multi-Language for each Guild. (de-DE.json, en-GB.json, etc)
* [x] Ticket Logging Channel on close.
* [ ] Including a Plugin-System for Developers

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
  
