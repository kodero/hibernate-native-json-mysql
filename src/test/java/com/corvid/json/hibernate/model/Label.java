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
package com.corvid.json.hibernate.model;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class Label {

    private String value;
    private String lang;
    private Integer order;

    public Label() {
        this.order = 0;
    }

    public Label(String value) {
        this.order = 0;
        this.value = value;
    }

    public Label(String value, String lang, Integer order) {
        this.value = value;
        this.lang = lang;
        this.order = order;
    }

    public String getValue() {
        return value;
    }

    public Label setValue(String value) {
        this.value = value;
        return this;
    }

    public Label setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public String getLang() {
        return lang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Label label = (Label) o;

        if (lang != null ? !lang.equals(label.lang) : label.lang != null) return false;
        if (value != null ? !value.equals(label.value) : label.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        return result;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}