package mao.t2;


import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project name(项目名称)：java并发编程_享元模式
 * Package(包名): mao.t2
 * Class(类名): JDBCTemplate2
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 14:03
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class JDBCTemplate2
{
    private static final ConnectionPool connectionPool = new ConnectionPool();

    /**
     * 查询方法，此方法用于将一条或者多条记录封装成一个list集合并返回，适用于多表查询
     * 此方法不推荐使用，因为在map集合中，key是列名，重复。但是集合重写了toString方法，可以直接输出字符串。
     *
     * @param sql  预编译的sql查询语句，经常使用多表查询，sql语句实例：查询某号学生选修的课程名称和对应课程的分数：
     *             SELECT course.`name`, grade.grade
     *             FROM grade, course WHERE grade.cno = course.cno AND grade.`no`=?
     * @param objs sql中的问号占位符，数量和顺序一定要一致，在上一个例子中，问号的数量为1，数组个数为一个。
     *             例如，查询2号学生的学生选修的课程名称和对应课程的分数：可变数组的值为一个，值为2，
     * @return 返回一个ArrayList集合，list集合的泛型为HashMap集合，map集合中的键为列名，值为此行的列名对应的值
     * 此方法不推荐使用，因为在map集合中，key是列名，重复。
     */
    public static List<HashMap<String, Object>> queryForListMap(String sql, Object... objs)
    {
        //定义一个List集合
        List<HashMap<String, Object>> list = new ArrayList<>();
        //连接对象
        Connection connection = null;
        //预编译执行者对象
        PreparedStatement preparedStatement = null;
        //结果集对象
        ResultSet resultSet = null;
        try
        {
            //获取连接对象(Druid连接池)
            //connection = Druid.getConnection();
            //或者(自定义数据库连接池)：
            connection = connectionPool.getConnection();
            //或者直接获取(自定义JDBC工具类)：
            //connection = JDBC.getConnection();

            //预编译sql，返回执行者对象
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //获取参数的源信息对象
            ParameterMetaData parameterMetaData = preparedStatement.getParameterMetaData();
            //获取参数个数
            int count = parameterMetaData.getParameterCount();
            //判断参数是否一致，如果不一致，异常抛出
            if (objs.length != count)
            {
                throw new RuntimeException("queryForArray方法中参数个数不一致!");
            }
            //为问号占位符赋值
            for (int i = 0; i < count; i++)
            {
                preparedStatement.setObject(i + 1, objs[i]);
            }
            //执行sql语句,返回结果集
            resultSet = preparedStatement.executeQuery();
            //通过结果集的对象获取结果源信息对象
            ResultSetMetaData metaData = resultSet.getMetaData();
            //通过源信息对象获取列数
            int column = metaData.getColumnCount();
            //定义HashMap集合对象
            HashMap<String, Object> map = null;
            //填充数据
            while (resultSet.next())
            {
                //开辟对象
                map = new HashMap<>();
                //循环遍历列数
                for (int i = 0; i < column; i++)
                {
                    //获取列名
                    String columnName = metaData.getColumnName(i + 1);
                    //通过列名获取数据
                    Object object = resultSet.getObject(columnName);
                    //System.err.print(object+" ");
                    //键值对加入到map集合中
                    map.put(columnName, object);
                }
                //加入到list集合中
                list.add(map);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //释放资源
            Druid.close(connection, preparedStatement, resultSet);
            //或者：
            //JDBC.close(connection, preparedStatement,resultSet);
        }
        //返回结果
        return list;
    }
}
