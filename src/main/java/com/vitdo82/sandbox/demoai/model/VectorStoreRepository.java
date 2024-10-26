package com.vitdo82.sandbox.demoai.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VectorStoreRepository  extends JpaRepository<VectorStoreEntity, UUID> {

    List<VectorStoreEntity> findVectorStoreEntitiesByDocument_DocumentName(String name);
}
