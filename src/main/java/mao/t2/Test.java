package mao.t2;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Project name(项目名称)：java并发编程_享元模式
 * Package(包名): mao.t2
 * Class(类名): Test
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 13:46
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class Test
{
    private static final ConnectionPool connectionPool = new ConnectionPool();

    public static void main(String[] args) throws SQLException
    {

        for (int i = 0; i < 20; i++)
        {
            Connection connection = connectionPool.getConnection();
            System.out.println(connection);
            connection.close();
        }

//        List<HashMap<String, Object>> students = JDBCTemplate2.queryForListMap("select * from student");
//        for (HashMap<String, Object> student : students)
//        {
//            System.out.println(student);
//        }
    }
}
