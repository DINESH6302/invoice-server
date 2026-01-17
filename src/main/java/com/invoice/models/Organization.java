package com.invoice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "organizations")
@EntityListeners(EntityListeners.class)
public class Organization extends BaseModel {

    @Id
    @Column(name = "org_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orgId;

    @Column(name = "org_name", nullable = false)
    private String orgName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String currency;

    @Column(name = "gst_no", nullable = false, unique = true)
    private String gstNo;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
