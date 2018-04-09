package com.hibernate.example.hotel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;

@Entity
@Table(name="suite", schema = "public")
@NamedQueries(
        @NamedQuery(name = "findSuite",
                    query = "select s from Suite s where bedsNumber=:bedsNumber and hotel.name=:hotelName")
)
public class Suite implements Serializable {

    @Id
    @Column(name = "room_id")
    private int roomNumber;
    @Id
    @ManyToOne
    private Hotel hotel;
    @Column(name = "beds_count")
    private int bedsNumber;
    @Column(length = 255)
    private String description;
    @Version
    private int version;
    private boolean booked;

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getBedsNumber() {
        return bedsNumber;
    }

    public void setBedsNumber(int bedsNumber) {
        this.bedsNumber = bedsNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    @Override
    public String toString() {
        return "Suite{" +
                "roomNumber=" + roomNumber +
                ", hotel=" + hotel.getName() +
                ", bedsNumber=" + bedsNumber +
                ", description='" + description + '\'' +
                ", version=" + version +
                ", booked=" + booked +
                '}';
    }
}
