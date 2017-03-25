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
import com.corvid.json.hibernate.usertype.JsonListUserType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class JsonListUserTypeTest {

    private JsonListUserType type = null;

    @Before
    public void createType() {
        type = new JsonListUserType() {
            @Override
            public Class<?> returnedClass() {
                return Label.class;
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConvertJsonToObject() throws Exception {
        String json = "[{\"value\": \"french label\", \"lang\":\"fr\"}, {\"value\": \"english label\", \"lang\":\"en\"}]";

        List<Label> labels = (List<Label>) type.convertJsonToObject(json);

        assertThat(labels, hasSize(2));
        assertThat(labels, containsInAnyOrder(
                allOf(hasProperty("value", is("french label")), hasProperty("lang", is("fr"))),
                allOf(hasProperty("value", is("english label")), hasProperty("lang", is("en")))));
    }

    @Test
    public void testConvertObjectToJson() throws Exception {
        List<Label> labels = new ArrayList<Label>();
        labels.add(new Label("french label", "fr", 1));
        labels.add(new Label("english label", "en", 2));

        String json = type.convertObjectToJson(labels);

        assertThat(json, containsString("{\"value\":\"french label\",\"lang\":\"fr\",\"order\":1}"));
        assertThat(json, containsString("{\"value\":\"english label\",\"lang\":\"en\",\"order\":2}"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeepCopy() throws Exception {
        List<Label> labels = new ArrayList<Label>();
        labels.add(new Label("french label", "fr", 3));
        labels.add(new Label("english label", "en", 4));

        List<Label> copy = (List<Label>) type.deepCopy(labels);

        assertThat(labels, hasSize(2));
        assertThat(copy, equalTo(labels));
        assertThat(copy, not(sameInstance(labels)));

        assertThat(labels, containsInAnyOrder(
                allOf(hasProperty("value", is("french label")), hasProperty("lang", is("fr"))),
                allOf(hasProperty("value", is("english label")), hasProperty("lang", is("en")))));

    }

}