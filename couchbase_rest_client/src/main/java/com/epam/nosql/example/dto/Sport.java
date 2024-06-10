package com.epam.nosql.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Sport {

    @Id
    private String id;

    @Field
    private String sportName;

    @Field
    private String sportProficiency;

}
