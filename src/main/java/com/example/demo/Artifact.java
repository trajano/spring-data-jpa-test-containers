package com.example.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class Artifact {

    @Column(length = 128, nullable = false)
    private String siteId;

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    @EqualsAndHashCode.Exclude
    private UUID uuid;

    @Version
    @EqualsAndHashCode.Exclude
    private int versionNo;
}
