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

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Target;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "SSN")
    private String ssn;

    @Column(name = "DESCRIPTION")
    private String description;

    @Type(type = "com.marvinformatics.hibernate.json.JsonListUserType")
    @Column(name = "labels")
    @Target(Label.class)
    private List<Label> labels = new ArrayList<Label>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private List<Item> items = new ArrayList<Item>();

    public Order() {
    }

    public Order(String ssn, String description, Label... labels) {
        super();
        this.ssn = ssn;
        this.description = description;
        this.labels = new ArrayList<>(asList(labels));
    }

    public Order addLabel(Label label) {
        if (label == null) {
            throw new NullPointerException();
        }

        labels.add(label);
        return this;
    }

    public Order addItem(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        items.add(item);
        return this;
    }

    public Long getId() {
        return id;
    }

    public Order setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSsn() {
        return ssn;
    }

    public Order ssn(String sSN) {
        ssn = sSN;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Order description(String description) {
        this.description = description;
        return this;
    }

    public Order labels(List<Label> labels) {
        this.labels = labels;
        return this;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}