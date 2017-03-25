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

import com.corvid.json.hibernate.model.Label;
import com.corvid.json.hibernate.usertype.JsonUserType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonUserTypeTest {

    private JsonUserType type = null;

    @Before
    public void createType() {
        type = new JsonUserType() {
            @Override
            public Class<?> returnedClass() {
                return Label.class;
            }
        };
    }

    @Test
    public void testConvertJsonToObject() throws Exception {
        String json = "{\"value\": \"french label\", \"lang\":\"fr\"}";

        Label label = (Label) type.convertJsonToObject(json);

        assertThat(label, notNullValue());
        assertThat(label, allOf(
                hasProperty("value", is("french label")),
                hasProperty("lang", is("fr"))));
    }

    @Test
    public void testConvertObjectToJson() throws Exception {
        Label label = new Label("french label", "fr", 1);

        String json = type.convertObjectToJson(label);
        assertThat(json, is("{\"value\":\"french label\",\"lang\":\"fr\",\"order\":1}"));
    }

    @Test
    public void testConvertJsonToObjectEmpty() throws Exception {
        String json = "";

        Label label = (Label) type.convertJsonToObject(json);
        assertThat(label, nullValue());
    }

    @Test
    public void testConvertJsonToObjectNull() throws Exception {
        String json = null;

        Label label = (Label) type.convertJsonToObject(json);
        assertThat(label, nullValue());
    }

    @Test
    public void testDeepCopy() throws Exception {
        Label label = new Label("french label", "fr", 2);
        Label copy = (Label) type.deepCopy(label);

        assertThat(copy, equalTo(label));
        assertThat(copy, not(sameInstance(label)));

        assertThat(label, allOf(
                hasProperty("value", is("french label")),
                hasProperty("order", is(2)),
                hasProperty("lang", is("fr"))));

    }
}