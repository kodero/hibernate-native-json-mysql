package com.corvid.json.hibernate.usertype;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StringType;

import java.sql.Types;

/**
 * Created by kodero on 3/25/17.
 */
public class MySQL57InnoDBDialect extends MySQL5InnoDBDialect {

    public MySQL57InnoDBDialect() {
        registerColumnType(Types.BIT, "tinyint(1)");
        registerColumnType(Types.JAVA_OBJECT, "json");
        registerFunction("json_text",
                new SQLFunctionTemplate(StringType.INSTANCE, "?1 ->> \"$.?2\""));
    }

    @Override
    protected void registerVarcharTypes() {

        // TODO: The MySQL default makes it difficult to migrate the data because mysqldump is braindead...
        // registerColumnType(Types.BIT, "tinyint(1)");

        // It's pretty safe to assume that anything with more than 1024 characters (minus length byte) should probably be
        // a TEXT, not a VARCHAR which would have the "maximum row size" limit of 65KB.
        // I mean, where is the limit? If you have 20 of these VARCHAR fields on a table, and your character set is
        // UTF8, you are over the limit. Less than 20 or so should be OK. Just another fine example of how MySQL
        // protects its users from seeing its ugly internal implementation details.
        registerColumnType(Types.VARCHAR, "longtext");
        registerColumnType(Types.VARCHAR, 16777215, "mediumtext");
        registerColumnType(Types.VARCHAR, 1023, "varchar($l)");
    }

    // Create all tables as default UTF8!
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
    }
}
