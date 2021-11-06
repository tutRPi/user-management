

Build Postgres Docker
```
docker build -t database ./db
```

Run Postgres
```
docker run --name postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d database
docker start postgres
```

Local Mail Server
```
docker run --name maildev -p 1080:80 -p 1025:25 -d maildev/maildev
docker run maildev
```

