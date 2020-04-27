# MIGRATIONS #

Run migrations against thumbnote-dev
```bash
 mvn clean liquibase:update -Ppostgres -Dliquibase.url="jdbc:postgresql://thumbnote-dev.thumbtack.lo:5432/thumbnote" -Dliquibase.username="sa" -Dliquibase.password="Pass1234"
```

To add new migration:
- add new xml migration to `thumbnote/changes`
- include line in `thumbnote/db.changelog-master.xml` 
  (ex, `<include file="changes/initdatabase.xml" relativeToChangelogFile="true"/>`)
