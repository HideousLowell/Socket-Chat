package edu.school21.sockets.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Room {

    private Long id;
    private String name;

    public Room(String name) {
        this.name = name;
    }

}
