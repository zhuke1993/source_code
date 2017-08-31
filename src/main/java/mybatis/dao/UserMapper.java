package mybatis.dao;

import mybatis.bean.User;

/**
 * Created by ZHUKE on 2017/8/18.
 */
public interface UserMapper {
    User select(long id);

    int insert(User user);
}
