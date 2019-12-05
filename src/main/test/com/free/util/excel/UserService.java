package com.free.util.excel;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "张小敬", '男', 35, 98.8d,
            Date.valueOf(LocalDate.parse("1984-12-01")), BigDecimal.valueOf(30000.00d)));
        users.add(new User(1L, "闻染", '女', 25, 92.5d,
            Date.valueOf(LocalDate.parse("1994-12-01")), BigDecimal.valueOf(28000.50d)));
        users.add(new User(1L, "龙泽", '男', 38, 100.0d,
            Date.valueOf(LocalDate.parse("1987-12-01")), BigDecimal.valueOf(40000.20d)));
        return users;
    }
}
