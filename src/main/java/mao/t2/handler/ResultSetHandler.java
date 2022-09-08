package mao.t2.handler;

import java.sql.ResultSet;

/**
 * Project name(项目名称)：java并发编程_享元模式
 * Package(包名): mao.t2.handler
 * Interface(接口名): ResultSetHandler
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/9/8
 * Time(创建时间)： 14:00
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public interface ResultSetHandler<T>
{
    <T> T handler(ResultSet resultSet);
}
