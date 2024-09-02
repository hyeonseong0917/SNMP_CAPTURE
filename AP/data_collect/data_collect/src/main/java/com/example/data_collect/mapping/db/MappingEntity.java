package com.example.data_collect.mapping.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "tb_if_ip_mapping_mst")
public class MappingEntity {
    @EmbeddedId
    private MappingEntityId id;
}

