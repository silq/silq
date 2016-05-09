Comandos para geração da estrutura e dos dados:

```
pg_dump -h 0.0.0.0 -p 5431 -d silq2 -U postgres -s > sql/schema.sql
pg_dump -h 0.0.0.0 -p 5431 -d silq2 -U postgres -a -t tb_qualis_periodico > sql/qualis_periodico.sql
pg_dump -h 0.0.0.0 -p 5431 -d silq2 -U postgres -a -t tb_qualis_evento > sql/qualis_evento.sql
```
