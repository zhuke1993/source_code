package hello;

import mybatis.bean.User;
import mybatis.dao.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");

        jdbcTemplate.execute("DROP  TABLE IF EXISTS user_info");
        jdbcTemplate.execute("CREATE TABLE user_info(" +
                "id bigint(20) AUTO_INCREMENT PRIMARY KEY, firstName VARCHAR(255), lastName VARCHAR(255))");

        // Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> log.info(String.format("Inserting user record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO user_info(firstName, lastName) VALUES (?,?)", splitUpNames);

        log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
                "SELECT id, firstName, lastName FROM user_info WHERE firstName = ?", new Object[]{"Josh"},
                (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"))
        ).forEach(customer -> log.info(customer.toString()));


        String resource = "mybatis-config.xml";
        InputStream inputStream = new ClassPathResource(resource).getInputStream();
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();

        try {

            User user = session.selectOne("mybatis.dao.UserMapper.select", 1);
            System.out.println(user);

            UserMapper userInfoMapper = session.getMapper(UserMapper.class);
            User user1 = userInfoMapper.select(1L);
            System.out.println(user1);

            User user2 = new User();
            user2.setFirstName("firstname");
            user2.setLastName("lastname");
            int insert = userInfoMapper.insert(user2);
            System.out.println(insert);

            session.insert("mybatis.dao.UserMapper.insert", user2);
            session.commit();

            session.insert("mybatis.dao.UserMapper.insert", user2);
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
        //session.commit();
        Thread.sleep(Integer.MAX_VALUE);
    }
}