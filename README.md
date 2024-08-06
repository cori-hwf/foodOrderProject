# Food Order project
This project contains a frontend & backend application for food ordering. The frontend application is meant to be customer-facing for food ordering while the backend application allows admin to manage cuisine, food sets and so on.

## PostgreSQL setup in docker

To ease the workload of database, Master-slave replication is utilized to ensure the master database to serve Read&Write and slave database serve only Read operation. 

Version used: `PG_VERSION 16.0-1.pgdg120+1`

1.Create a volume to persist the database between container restarts:
```
docker volume create foodDelivery_db
```

2. Create and start master db container:
```
docker run -d -v foodDelivery_db:/var/lib/postgresql/data -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=foodDelivery -p 5432:5432 --name foodDelivery_db postgres
```

3. Create and start slave db container:
```
docker run -d -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=foodDelivery -p 5431:5431 --name foodDelivery_slave postgres
```

4. Create subnetwork for static Ip address (Static Ip address is important for replication config)
```  
docker network create --subnet<Ip address range> mynetwork
```

5. Bind Master db with a static Ip address & the same for slave
```
docker network connect --ip=<Ip address> mynetwork <master container id>
```
```
docker network connect --ip=<Ip address> mynetwork <slave container id>
```

### Master container config
1. Create replication role for slave db to use
```
CREATE USER replica replication encrypted password 'replica';
```

2. Config pg_hba.conf file
Add the following for slave to login as replica
```
host   replication    replica    <slave staic Ip address>/32     md5
host   all all  <slave staic Ip address>/32   trust  
```

3. Config postgresql.conf file

```
listen_addresses = ‘*’   #listen to all ip
 
archive_mode = on   #turn on archive
 
archive_command = ‘cp %p /var/lib/postgresql/archivefolder/%f’   #%p:wal fiel full path %f:wal file name  
 
wal_level = replica    # replica mode
 
max_wal_senders = 10   #at most 10 cocurrent stream replication 
 
wal_sender_timeout = 60s    
 
max_connections = 100   #Max connection time, should be smaller than slave conf
```

### Slave container config

1. clear all data in var/lib/postgresql/data
```
rm -r var/lib/postgresql/data
```

2. sychronous slave db with the current master db
```
pg_basebackup -D /var/lib/postgresql/data -h 172.18.0.3 -p 5432 -U replica -X stream -P -R

-h: Specifies the hostname or IP address of the master server from which to take the backup.
-D: Specifies the directory where the backup data will be stored on the local machine (slave server).
-U: Specifies the username to use when connecting to the master server for replication.
-P: Prompts for the replication password of the specified user. After entering the password, the backup process begins.
-v: Enables verbose output, providing more detailed information about the backup process.
-R: Enables the creation of a recovery.conf(From PostgreSQL 12, recovery. conf is replaced by standby. signal ) file in the backup directory. This is essential for setting up a standby server for replication.
-X stream: Specifies that the backup method should use streaming replication, allowing continuous replication from the master to the standby server.
```

3. config postgresql.conf file
```
wal_level = replica
 
max_connections = 300   #should be bigger than master db
 
hot_standby = on 
 
max_standby_streaming_delay = 30s 
 
wal_receiver_status_interval = 10s  
 
hot_standby_feedback = on #if error will feedback to master
```

Restart both container after the configuration before testing
