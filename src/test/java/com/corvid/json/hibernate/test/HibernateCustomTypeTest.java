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

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.corvid.json.hibernate.model.Item;
import com.corvid.json.hibernate.model.Label;
import com.corvid.json.hibernate.model.Order;
import com.corvid.json.hibernate.util.HibernateUtility;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

public class HibernateCustomTypeTest {

    private static SessionFactory sessionFactory;
    private Serializable item1;
    private Serializable item2;
    private Serializable order1;

    @BeforeClass
    public static void createFactory() {
        sessionFactory = HibernateUtility.getSessionFactory();
    }

    @Before
    public void createSession() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        item1 = session.save(new Item("test1", new Label("french label", "fr", 1)));
        item2 = session.save(new Item("test2", new Label("label without lang")));

        order1 = session.save(new Order("40bdce70-9412-11e3-baa8-0800200c9a66", "", new Label("french label", "fr", 2), new Label("english label", "en", 3)));
        session.save(new Order("40bdce70-9412-11e3-baa8-0800200c9a69", "", new Label("label without lang")));
        session.save(new Order("40bdce70-9412-11e3-baa8-0800200c9a67", ""));
        session.getTransaction().commit();
    }

    @AfterClass
    public static void shutdown() {
        sessionFactory.close();
    }

    @Test
    public void shouldCreateLabel() {
        Item item = new Item().label(new Label("Message French", "fr", 4));

        Item id = (Item) save(item);

        assertThat(id.getId(), greaterThan(0l));

    }

    @Test
    public void shouldLoadLabel() {
        Item item = (Item) load(Item.class, item1);

        assertThat(item, hasProperty("label",
                allOf(
                        hasProperty("value", is("french label")),
                        hasProperty("lang", is("fr")))));
    }

    @Test
    public void shouldUpdateLabel() {
        Item loaded = (Item) load(Item.class, item2);
        loaded.getLabel().setLang("en").setValue("new text");

        save(loaded);

        loaded = (Item) load(Item.class, item2);

        assertThat(loaded, hasProperty("label",
                allOf(
                        hasProperty("value", is("new text")),
                        hasProperty("lang", is("en")))));

    }

    @Test
    public void shouldDeleteLabel() {
        Item item = (Item) load(Item.class, item2);
        item.label(null);

        update(item);

        Item loadedItem = (Item) load(Item.class, item2);

        assertThat(loadedItem.getLabel(), nullValue());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldCreateLabels() throws JsonProcessingException {

        Order order = new Order()
                .description("customer order")
                .ssn(UUID.randomUUID().toString())
                .addLabel(new Label("french value", "fr", 5))
                .addLabel(new Label("english value", "en", 6));

        Order saved = (Order) save(order);

        Order loadedOrder = (Order) load(Order.class, saved.getId());

        assertThat(loadedOrder.getLabels(), hasSize(2));

        assertThat(loadedOrder.getLabels(), containsInAnyOrder(
                allOf(
                        hasProperty("value", is("french value")),
                        hasProperty("lang", is("fr"))),
                allOf(
                        hasProperty("value", is("english value")),
                        hasProperty("lang", is("en")))));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldUpdateLabels() {
        Order loadedOrder = (Order) load(Order.class, order1);
        loadedOrder.getLabels().get(0).setValue("new value").setLang("zh");

        update(loadedOrder);

        Order refreshOrder = (Order) load(Order.class, order1);

        assertThat(refreshOrder.getLabels(), containsInAnyOrder(
                allOf(
                        hasProperty("value", is("new value")),
                        hasProperty("lang", is("zh"))),
                allOf(
                        hasProperty("value", is("english label")),
                        hasProperty("lang", is("en")))));

    }

    @Test
    public void shouldDeleteLabels() {
        Order loadedOrder = (Order) load(Order.class, order1);
        loadedOrder.labels(new ArrayList<Label>());

        update(loadedOrder);

        Order refreshOrder = (Order) load(Order.class, order1);

        assertThat(refreshOrder.getLabels(), empty());
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

    private void update(Object entity) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();
    }

    @Test
    public void queryJson() {
        save(new Item("test3", new Label("brasiu um", "pt_br", 7)));
        save(new Item("test4", new Label("brasiu dois", "pt_br", 8)));
        save(new Item("test5", new Label("brasio tles", "pt_br", 9)));

        Session session = sessionFactory.openSession();
        Query query = session.createQuery("select json_text(i.label, 'value') from Item i where json_text(i.label, 'lang') = :lang");
        query.setParameter("lang", "pt_br");
        @SuppressWarnings("unchecked")
        List<String> result = query.list();
        session.close();

        assertThat(result, hasSize(3));
        assertThat(result, containsInAnyOrder("brasiu um", "brasiu dois", "brasio tles"));
    }

    @Test
    public void map() {
        Map<String, String> extra = new HashMap<String, String>();
        extra.put("key", "value");
        extra.put("foo", "bar");

        Long item3 = save(new Item("test3", extra)).getId();

        Item item = load(Item.class, item3);
        assertThat(item.getExtra(), hasEntry("foo", "bar"));
        assertThat(item.getExtra(), hasEntry("key", "value"));

        Session session = sessionFactory.openSession();
        Query query = session.createQuery("select json_text(i.extra, 'foo') from Item i where json_text(i.extra, 'key') = :value");
        query.setParameter("value", "value");
        @SuppressWarnings("unchecked")
        List<String> result = query.list();
        session.close();

        assertThat(result, hasSize(1));
        assertThat(result, containsInAnyOrder("bar"));
    }

}