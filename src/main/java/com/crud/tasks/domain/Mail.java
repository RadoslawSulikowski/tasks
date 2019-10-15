package com.crud.tasks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Mail {
    private String receiverEmail;
    private String toCC;
    private String subject;
    private String message;

    public Mail(final String receiverEmail, final String subject, final String message){
        this.receiverEmail = receiverEmail;
        this.subject = subject;
        this.message = message;
    }
}
