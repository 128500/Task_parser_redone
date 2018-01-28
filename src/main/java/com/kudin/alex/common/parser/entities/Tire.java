package com.kudin.alex.common.parser.entities;

import lombok.*;

import javax.persistence.*;

/**
 * Created by KUDIN ALEKSANDR on 25.01.2018.
 *
 * This class represents an entity of a tire.
 */
@Entity                                         //JPA
@Table(name="tires")                            //JPA
@NoArgsConstructor @AllArgsConstructor          //Lombok
@ToString(exclude="id")                         //Lombok
public class Tire {

    /*Entity id*/
    @Id                                         //JPA
    @GeneratedValue                             //JPA
    @Column(name="tire_id")                     //JPA
    @Getter @Setter private Long id;            //Lombok

    /*Type of a tire*/
    @Column(name="type")
    @Getter @Setter private String type;

    /*Manufacturer of a tire (brand)*/
    @Column(name="manufacturer")
    @Getter @Setter private String manufacturer;

    /*Model of a tire*/
    @Column(name="model")
    @Getter @Setter private String model;

    /*Tire width*/
    @Column(name="width")
    @Getter @Setter private int width;

    /*Tire height*/
    @Column(name="height")
    @Getter @Setter private int height;

    /*Diameter of a tire*/
    @Column(name="diameter")
    @Getter @Setter private double diameter;

    /*Season of tire using*/
    @Column(name="season")
    @Getter @Setter private String season;

    /*Load index of a tire*/
    @Column(name="load_index")
    @Getter @Setter private String loadIndex;

    /*Speed index of a tire*/
    @Column(name="speed_index")
    @Getter @Setter private String speedIndex;

    /*Strengthening mark*/
    @Column(name="strengthen")
    @Getter @Setter private boolean strengthen;

    /*Studding mark*/
    @Column(name="studded")
    @Getter @Setter private boolean studded;

    /*Additional parameter*/
    @Column(name="additional_param")
    @Getter @Setter private String additionalParam;

    /*Manufacturing country*/
    @Column(name="country")
    @Getter @Setter private String country;

    /*Year of manufacturing*/
    @Column(name="year")
    @Getter @Setter private int year;

    /*Current balance*/
    @Column(name="balance")
    @Getter @Setter private int balance;

    /*Price of a tire*/
    @Column(name="price")
    @Getter @Setter private double price;
}
