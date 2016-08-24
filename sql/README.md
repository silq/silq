Para gerar os arquivos `schema.sql`, `qualis_evento.sql` e `qualis_periodico`.

```
$ pg_dump -h 0.0.0.0 -p 5431 -d silq2 -U postgres -s > sql/schema.sql
$ pg_dump -h 0.0.0.0 -p 5431 -d silq2 -U postgres -a -t tb_qualis_periodico > sql/qualis_periodico.sql
$ pg_dump -h 0.0.0.0 -p 5431 -d silq2 -U postgres -a -t tb_qualis_evento > sql/qualis_evento.sql
```

***

Para ciar a base de dados através do arquivos gerados:

```
psql -h 0.0.0.0 -p 5431 -U postgres -f sql/schema.sql
psql -h 0.0.0.0 -p 5431 -U postgres -f sql/qualis_evento.sql
psql -h 0.0.0.0 -p 5431 -U postgres -f sql/qualis_periodico.sql
```
