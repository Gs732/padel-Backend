package com.ephec.padel.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ephec.padel.site.model.Site;

public interface SiteRepository extends JpaRepository<Site, Long> {
}