package com.nosy.admin.nosyadmin.repository;

import com.nosy.admin.nosyadmin.model.EmailFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;

@Transactional
@Repository
@CrossOrigin
public interface EmailFeedRepository extends JpaRepository<EmailFeed, String> {

    @Query("from EmailFeed where email_feed_name=:emailFeedName and input_system_id=:inputSystemId")
    EmailFeed findEmailFeedByEmailFeedNameAndInputSystemId(
            @Param("emailFeedName") String emailFeedName,
            @Param("inputSystemId") String inputSystemId
    );

    @Query("from EmailFeed where email_feed_id=:emailFeedId and input_system_id=:inputSystemId")
    EmailFeed findEmailFeedByEmailFeedIdAndInputSystemId(
            @Param("emailFeedId") String emailFeedId,
            @Param("inputSystemId") String inputSystemId
    );

}
