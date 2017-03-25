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

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Type(type = "com.marvinformatics.hibernate.json.JsonUserType")
    @Column(name = "label")
    private Label label;

    @Type(type = "com.marvinformatics.hibernate.json.JsonUserType")
    private Map<String, String> extra;

    public Item() {
    }

    public Item(String name, Label label) {
        this();
        this.name = name;
        this.label = label;
    }

    public Item(String name, Map<String, String> extra) {
        super();
        this.name = name;
        this.extra = extra;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Label getLabel() {
        return label;
    }

    public Item label(Label label) {
        this.label = label;
        return this;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public Item setExtra(Map<String, String> extra) {
        this.extra = extra;
        return this;
    }

}