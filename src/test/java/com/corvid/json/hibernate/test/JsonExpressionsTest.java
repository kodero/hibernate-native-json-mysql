/**
 * Copyright (C) 2016 Marvin Herman Froeder (marvin@marvinformatics.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.corvid.json.hibernate.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.corvid.json.hibernate.model.Item;
import com.corvid.json.hibernate.model.Label;
import com.corvid.json.hibernate.model.Order;
import com.corvid.json.hibernate.util.HibernateUtility;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsonExpressionsTest {

    private static SessionFactory sessionFactory;

    @BeforeClass
    public static void createFactory() {
        sessionFactory = HibernateUtility.getSessionFactory();
    }

    @Before
    public void createSession() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(new Item("test1", new Label("french label", "fr", 1)));
        session.save(new Item("test2", new Label("label without lang")));

        session.save(new Order("40bdce70-9412-11e3-baa8-0800200c9a66", "", new Label("french label", "fr", 2), new Label("english label", "en", 3)));
        session.save(new Order("40bdce70-9412-11e3-baa8-0800200c9a69", "", new Label("label without lang")));
        session.save(new Order("40bdce70-9412-11e3-baa8-0800200c9a67", ""));
        session.getTransaction().commit();
    }

    @AfterClass
    public static void shutdown() {
        sessionFactory.close();
    }

    private <E> E load(Class<E> clazz, Serializable id) {
        Session session = sessionFactory.openSession();
        @SuppressWarnings("unchecked")
        E entity = (E) session.get(clazz, id);
        session.close();

        return entity;
    }

    @SuppressWarnings("unchecked")
    private <E> E save(E entity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Object entityAttached = session.merge(entity);
        session.getTransaction().commit();

        return (E) entityAttached;
    }

    @Test
    public void queryJson() {
        save(new Item("test3", new Label("brasiu um", "pt_br", 4)));
        save(new Item("test4", new Label("brasiu dois", "pt_br", 5)));
        save(new Item("test5", new Label("brasio tles", "pt_br", 6)));

        Session session = sessionFactory.openSession();
        List<String> result = session.createQuery("select json_text(label, value) from Item i where json_text(label, lang) =:lang")
                .setParameter("lang", "pt_br").list();
        session.close();

        assertThat(result, hasSize(3));
        assertThat(result, contains("brasio tles", "brasiu dois", "brasiu um"));
    }

    @Test
    public void map() {
        Map<String, String> extra = new HashMap<>();
        extra.put("key", "value");
        extra.put("foo", "bar");

        Long item3 = save(new Item("test3", extra)).getId();

        Item item = load(Item.class, item3);
        assertThat(item.getExtra(), hasEntry("foo", "bar"));
        assertThat(item.getExtra(), hasEntry("key", "value"));

        Session session = sessionFactory.openSession();

        List<String> result = session.createQuery("select json_text(extra, foo) from Item i where json_text(extra, value) =:value")
                .setParameter("value", "value").list();
        session.close();

        assertThat(result, hasSize(1));
        assertThat(result, containsInAnyOrder("bar"));
    }

}