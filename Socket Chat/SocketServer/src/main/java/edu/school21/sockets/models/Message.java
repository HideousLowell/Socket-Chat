package edu.school21.sockets.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Message {

    private Long id;
    private User user;
    private String text;
    private LocalDateTime dateTime;
    private Room room;

    public Message(User user, String text, Room room) {
        this.user = user;
        this.text = text;
        dateTime = LocalDateTime.now();
        this.room = room;
    }
}
