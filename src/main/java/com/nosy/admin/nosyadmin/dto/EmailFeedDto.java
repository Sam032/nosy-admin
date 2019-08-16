package com.nosy.admin.nosyadmin.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nosy.admin.nosyadmin.model.EmailTemplate;
import com.nosy.admin.nosyadmin.model.InputSystem;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class EmailFeedDto {

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private Set<@NotEmpty @Email String> subscribers;

    @JsonIgnore
    @NotNull
    private InputSystem inputSystem;

    @JsonIgnore
    @NotNull
    private EmailTemplate emailTemplate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<String> subscribers) {
        this.subscribers = subscribers;
    }

    public InputSystem getInputSystem() {
        return inputSystem;
    }

    public void setInputSystem(InputSystem inputSystem) {
        this.inputSystem = inputSystem;
    }

    public EmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }
}
