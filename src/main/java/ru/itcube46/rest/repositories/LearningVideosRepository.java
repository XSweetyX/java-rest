package ru.itcube46.rest.repositories;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ru.itcube46.rest.entities.LearningVideos;
public interface LearningVideosRepository extends CrudRepository<LearningVideos, String> { 

   @Query("SELECT * FROM LEARNING_VIDEOS WHERE VIDEO_TITLE = :videoTitle")
   Iterable<LearningVideos> findAllByVideoTitle(@Param("videoTitle") String videoTitle);
   
   @Query("SELECT * FROM LEARNING_VIDEOS WHERE THEME = :videoTheme")
   Iterable<LearningVideos> findAllByVideoTheme(@Param("videoTheme") String videoTheme);
}