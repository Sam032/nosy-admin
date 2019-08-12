package com.nosy.admin.nosyadmin.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nosy.admin.nosyadmin.exceptions.EmailFeedNameInvalidException;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@DynamicInsert
@Table(name = "emailFeed", uniqueConstraints = @UniqueConstraint(columnNames = {"emailFeedName", "input_system_id"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmailFeed {

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Id
    @NotNull
    private String emailFeedId;

    @NotNull
    private String emailFeedName;

    @NotNull
    private String emailFeedAddress;

    @NotNull
    @ElementCollection
    @JoinTable(name = "email_feed_subscribers", joinColumns = @JoinColumn(name = "email_feed_id"))
    private Set<@NotEmpty @Email String> emailFeedSubscribers;

    @NotNull
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "input_system_id")
    private InputSystem inputSystem;

    @PrePersist
    protected void onCreate() {
        if (emailFeedName == null || emailFeedName.isEmpty()) {
            throw new EmailFeedNameInvalidException();
        }
    }

    public String getEmailFeedId() {
        return emailFeedId;
    }

    public void setEmailFeedId(String emailFeedId) {
        this.emailFeedId = emailFeedId;
    }

    public String getEmailFeedName() {
        return emailFeedName;
    }

    public void setEmailFeedName(String emailFeedName) {
        this.emailFeedName = emailFeedName;
    }

    public String getEmailFeedAddress() {
        return emailFeedAddress;
    }

    public void setEmailFeedAddress(String emailFeedAddress) {
        this.emailFeedAddress = emailFeedAddress;
    }

    public Set<String> getEmailFeedSubscribers() {
        return emailFeedSubscribers;
    }

    public void setEmailFeedSubscribers(Set<String> emailFeedSubscribers) {
        this.emailFeedSubscribers = emailFeedSubscribers;
    }

    public InputSystem getInputSystem() {
        return inputSystem;
    }

    public void setInputSystem(InputSystem inputSystem) {
        this.inputSystem = inputSystem;
    }
}
