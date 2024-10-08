package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int bookingId;
    @ManyToOne
    @JoinColumn(name="refugee_id", referencedColumnName = "refugeeId")
    private Refugee refugee;
    private Date startDate;
    private Date endDate;
    private boolean status;
    @OneToMany(mappedBy = "booking",fetch= FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Review>review;
}
