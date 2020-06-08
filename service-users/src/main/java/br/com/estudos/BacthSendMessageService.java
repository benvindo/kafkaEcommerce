package br.com.estudos;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BacthSendMessageService {

    private final Connection connection;

    BacthSendMessageService () throws SQLException {
        String url = "jdbc:sqlite:target/sqlite:users_database.db";
        this.connection = DriverManager.getConnection(url);
        try{
            connection.createStatement().execute("create table Users (" +
                    "uuid varchar(200) primary key," +
                    "email varchar(200))");
        } catch (SQLException ex){
            //be careful, the sql could be wrong, be realllly careful
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) throws SQLException {
        BacthSendMessageService bacthService = new BacthSendMessageService();
        try (var service = new KafkaService<>(BacthSendMessageService.class.getSimpleName(),
                "SEND_MESSAGE_TO_ALL_USERS",
                bacthService::parse,
                String.class,
                Map.of()
        )) {
            service.run();
        }
    }

    private  final KafkaDispatcher<User>  userDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, String> record) throws SQLException, ExecutionException, InterruptedException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new bacth");
        System.out.println("Topic: " + record.value());


        for (User user: getAllUsers()){
            userDispatcher.send(record.value(), user.getUuid(), user);
        }


    }

    private List<User> getAllUsers() throws SQLException {
        var results = connection.prepareStatement("select uuid from Users").executeQuery();
        List<User> users = new ArrayList<>();
        while (results.next()){
            users.add(new User(results.getString(1)));
        }
        return users;
    }

}
