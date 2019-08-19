package com.nosy.admin.nosyadmin.repository;

import com.nosy.admin.nosyadmin.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
@CrossOrigin
public interface FeedRepository extends JpaRepository<Feed, String> {

    @Query("from Feed where feed_name=:feedName and input_system_id=:inputSystemId")
    Feed findFeedByFeedNameAndInputSystemId(
            @Param("feedName") String feedName,
            @Param("inputSystemId") String inputSystemId
    );

    @Query("from Feed where feed_id=:feedId and input_system_id=:inputSystemId")
    Feed findFeedByIdAndInputSystemId(
            @Param("feedId") String feedId,
            @Param("inputSystemId") String inputSystemId
    );

    @Query("from Feed where input_system_id=:inputSystemId")
    List<Feed> findFeedsByInputSystemId(@Param("inputSystemId") String inputSystemId);

}
