# kafkaEcommerce

### Comandos Kafka CDH 6 + Kerberos
---
* Criar o arquivo jaas.config

```
KafkaClient {
 com.sun.security.auth.module.Krb5LoginModule required
 useKeyTab=true
 keyTab="/run/cloudera-scm-agent/process/1330-kafka-KAFKA_BROKER/kafka.keytab"
 storeKey=true
 useTicketCache=false
 principal="kafka/ctn5.lab.local@KDC.CDH6";
};
```

*Criar o arquivo consumer.properties
```
security.protocol=SASL_PLAINTEXT
sasl.kerberos.service.name=kafka
```

* Exporta a variavel de ambiente
>export KAFKA_OPTS="-Djava.security.auth.login.config=/tmp/jaas.config

* Comandos

###### _**Consumir Topico**_
> kafka-console-consumer --bootstrap-server ctn2.lab.local:9092,ctn3.lab.local:9092,ctn4.lab.local:9092,ctn5.lab.local:9092 --topic teste --from-beginning --consumer.config consumer.propertiers

###### _**Produzir Topico**_
> kafka-console-consumer --bootstrap-server ctn2.lab.local:9092 --topic teste --from-beginning --consumer.config consumer.propertiers

###### _**Lista Topicos**_
>kafka-topics --list --zookeeper ctn1.lab.local,ctn2.lab.local,ctn3.lab.local:2181

###### _**Descrever Topico**_
>kafka-topics --describe --zookeeper ctn1.lab.local,ctn2.lab.local,ctn3.lab.local:2181 --topic teste

###### _**Alterar partições do Topico**_
O Numero maximo de parelizações será o numero maximo de partições
>kafka-topics --alter --zookeeper ctn1.lab.local,ctn2.lab.local,ctn3.lab.local:2181 --topic ECOMMERCE_SEND_EMAIL --partitions 3

###### _**Listar os grupo de consumo**_
>kafka-consumer-groups  --bootstrap-server ctn2.lab.local:9092,ctn3.lab.local:9092,ctn4.lab.local:9092,ctn5.lab.local:9092 --list --command-config consumer.propertiers

###### _**Descrever grupo de consumo**_
>kafka-consumer-groups  --bootstrap-server ctn2.lab.local:9092,ctn3.lab.local:9092,ctn4.lab.local:9092,ctn5.lab.local:9092 --describe --group FraudDetectorService  --command-config consumer.propertiers

