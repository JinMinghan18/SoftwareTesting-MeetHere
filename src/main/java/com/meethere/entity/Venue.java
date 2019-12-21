package com.meethere.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Venue {
    @Id
    @GeneratedValue
    private int venueID;

    @Column(name="venue_name")
    private String venueName;

    private String description;

    private int price;

    private String picture;
}