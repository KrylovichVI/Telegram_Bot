package com.belintersat.bot.Domain;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "flags")
public class Flags {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private int value;

    @Column(name = "date")
    private Date date;

    public Flags() {
    }

    public Flags(String name, int value, Date date) {
        this.name = name;
        this.value = value;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
