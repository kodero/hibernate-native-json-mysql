<<<<<<< HEAD
hibernate-native-json
=================

:hp-tags: jpa, hibernate, json

Read/Write an object to JSON / JSON to object into a database table field (declared as a string column).
This also allow to query json. Originally forked from [Hibernate native json](https://github.com/velo/hibernate-native-json)

Currently supported databases(Modified from the original repo):
- MySQL

This project provided a hibernate UserType and a dialect with json support.

The UserType uses jackson object mappper to do a fast serialize/deserialize of a json string representation.  More information  [how to implements a user type](http://blog.xebia.com/2009/11/09/understanding-and-writing-hibernate-user-types/)

Check the src/test folder to see a full example.

### Example

You can serialize either a class or a Map<String, Object> (in any cases a more dynamic field is necessary).

```
@Entity
public class MyClass {

	@Type(type = "com.corvid.json.hibernate.usertype.JsonListUserType")
	@Target(Label.class)
	private List<Label> labels = new ArrayList<Label>();

	@Type(type = "com.corvid.json.hibernate.usertype.JsonUserType")
	private Map<String, String> extra;

}
```

Keep in mind, for collections @org.hibernate.annotations.Target is mandatory.

In order to be able to persist, query and generate DDL for this objects you need to set hibernate dialect to `PostgreSQLJsonDialect`.


```
  <property name="hibernate.dialect">com.corvid.json.hibernate.usertype.MySQL57InnoDBDialect</property>
```


Now you can persist your object as a json using your hibernate session / jpa repository.

### Querying

`json_text`: is equivalent to postgres `->>` get JSON object field as text
http://www.postgresql.org/docs/9.5/static/functions-json.html

This allow a HQL query like this:
```
	select
		json_text(i.label, 'value')
	from
		Item i
	where
		json_text(i.label, 'lang') = :lang
```

Witch will produce the following SQL for MySQL(json):
```
    select
        item0_.label ->> "$.value" as col_0_0_
    from
        items item0_
    where
        item0_.label ->> "$.lang"=?
```


### DDL generation
Considering this class:
```
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Type(type = "com.corvid.json.hibernate.usertype.JsonUserType")
    @Column(name = "label")
    private Label label;

    @Type(type = "com.corvid.json.hibernate.usertype.JsonUserType")
    private Map<String, String> extra;
```

This in produce the following DDL:
```
    create table items (
        id int8 not null,
        extra json,
        label json,
        name varchar(255),
        ORDER_ID int8,
        primary key (id)
    )
```

=======
# hibernate-native-json-mysql
A hibernate user type for mysql 
>>>>>>> c107667bdc64e78a9bd02952830c63861f6d794b
