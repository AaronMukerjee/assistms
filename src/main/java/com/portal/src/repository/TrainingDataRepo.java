package com.portal.src.repository;

import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

import com.portal.src.entity.TrainingSet;

//https://docs.spring.io/spring-cloud-gcp/docs/current/reference/html/firestore.html
@Repository
public interface TrainingDataRepo extends FirestoreReactiveRepository<TrainingSet>{
}
