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
package com.corvid.json.hibernate.usertype;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.usertype.UserCollectionType;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Marvin H Froeder
 */
public class JsonListUserType extends JsonUserType implements UserCollectionType {

    @Override
    public JavaType createJavaType(ObjectMapper mapper) {
        return mapper.getTypeFactory().constructCollectionType(List.class, returnedClass());
    }

    @Override
    public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister)
            throws HibernateException {
        return new PersistentList(session);
    }

    private PersistentList cast(Object collection) {
        return (PersistentList) collection;
    }

    @Override
    public PersistentCollection wrap(SessionImplementor session, Object collection) {
        return new PersistentList(session, (List<?>) collection);
    }

    @Override
    public Iterator<?> getElementsIterator(Object collection) {
        return cast(collection).iterator();
    }

    @Override
    public boolean contains(Object collection, Object entity) {
        return cast(collection).contains(entity);
    }

    @Override
    public Object indexOf(Object collection, Object entity) {
        return cast(collection).indexOf(entity);
    }

    @Override
    public Object replaceElements(Object original, Object target, CollectionPersister persister, Object owner,
            @SuppressWarnings("rawtypes") Map copyCache, SessionImplementor session) throws HibernateException {

        PersistentList originalList = cast(original);
        PersistentList targetList = cast(target);
        targetList.clear();
        targetList.addAll(originalList);

        return target;
    }

    @Override
    public Object instantiate(int anticipatedSize) {
        return new PersistentList();
    }
}