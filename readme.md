**BANZAI-TEST**

    Standalone сервис для загрузки файлов из локальной папки и сохранения их в БД по расписанию.
    По умолчанию мониторит заданную в конфигурационном файле application.yml директорию раз в 10 минут.


_**First start:**_
    
    В приложении существует три профиля: prod, dev, test.
    Общая конфигурация находится в папке application.yml.
    Конфигурация профиля prod - application-prod.yml
    Конфигурация профиля dev - application-dev.yml
    Конфигурация профиля test - application-test.
    
    Перед первым запуском необходимо ознакомиться с общей конфигурацией(application.yml), сконфигурировать приложение в вашей системе(задать пути папок и т.д.).
    Затем необходимо в application-prod.yml изменить настройки БД на ваши.
    При выборе профилей dev или test приложение работает с in-memory h2 db, конфигурация данных профилей доработки не требует.  

_**Build**_

Для сборки jar файла выполняем в терминале команду:<br/> 
`mvn clean package`

_**Deploy linux:**_

С root правамиНеобходимо создать файл:

`/etc/systemd/system/banzai-test.service `

со следующим содержимым:

`[Unit]`<br/>
`Description= banzai-test service`<br/>
`After=syslog.target`<br/>
`[Service]`<br/>
`SyslogIdentifier=banzai-test`<br/>
`ExecStart=/usr/bin/java -server -Xmx1g -Xms1g -XX:+HeapDumpOnOutOfMemoryError -jar /home/../../banzai-test.jar --spring.profiles.active=prod`<br/>
`User=Имя пользователя `<br/>
`Type=simple Restart=on-failure`<br/>
`RestartSec=15`<br/>
`WorkingDirectory=/home/../../`<br/>
`[Install]`<br/>
`WantedBy=multi-user.target`<br/>

Затем выполнить команды:

1) sudo systemctl daemon-reload
2) sudo systemctl enable banzai-test.service

Теперь можно запускать/останавливать/перезапускать/проверять статус сервиса следующими командами:

1)  sudo systemctl start banzai-test.service - запуск сервиса
2)  sudo systemctl stop banzai-test.service - остановка сервиса 
3)  sudo systemctl restart banzai-test.service - рестарт сервиса 
3)  sudo systemctl status banzai-test.service - проверить статус сервиса(работает/не работает)

_**Deploy windows:**_

Можно развернуть приложение как служба ОС windows с помощью windows service wrapper.
Полную инструкция здесь я писать не буду, просто приведу ссылку на инструкции, как это делается. 

https://dzone.com/articles/spring-boot-as-a-windows-service-in-5-minutes

Конфигурационный файл, в котором при желании нужно заменить пути на свои:
banzai-test.xml

`<?xml version="1.0" encoding="UTF-8"?>`<br/>
`<service>`<br/>
    `<id>banzai-test</id>`<br/>
    `<name>banzai-test</name>`<br/>
    `<description>This runs Spring Boot as a Service.</description>`<br/>
    `<env name="BANZAI_TEST" value="%BASE%"/>`<br/>
    `<executable>java</executable>`<br/>
    `<arguments>-server -Xmx1g -Xms1g -XX:+HeapDumpOnOutOfMemoryError -Dsun.jnu.encoding=UTF8 -Dfile.encoding=UTF8 -Dspring.profiles.active=prod -jar "%BASE%\jar\banzai-test.jar"</arguments>`<br/>
	`<log mode="none"/>`<br/>
	`<onfailure action="restart" delay="20 sec"/>`<br/>
	`<serviceaccount>`<br/>
		`<domain>xxx</domain>`<br/>
		`<user>xxx</user>`<br/>
		`<password>xxx</password>`<br/>
		`<allowservicelogon>true</allowservicelogon>`<br/>
	`</serviceaccount>`<br/>
`</service>`<br/>


Более простой вариант:
