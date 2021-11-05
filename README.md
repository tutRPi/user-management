

Build Postgres Docker
```
docker build -t database ./db
```

Run Postgres
```
docker run --name postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d database
docker start postgres
```