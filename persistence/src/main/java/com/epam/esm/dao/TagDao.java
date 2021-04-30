package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Set;

/**
 * Interface for managing Tag entities
 */
public interface TagDao {

    /**
     * Finds all  Tags  from database
     *
     * @return list of Tags from database
     */
    List<Tag> getAll();

    /**
     * Finds all  Tags from database by GiftCertificate
     *
     * @return list of Tags from database
     */
    List<Tag> getAllGiftCertificateTags(GiftCertificate giftCertificate);

    /**
     * Finds Tag from database by id
     *
     * @param id Tag id
     * @return Tag entity
     */
    Tag getById(Long id);

    /**
     * Finds Tag from database by name
     *
     * @param name Tag name
     * @return Tag entity
     */
    Tag getByName(String name);

    /**
     * Saves  Tag in database
     *
     * @param tag Tag entity
     * @return Tag id
     */
    Long save(Tag tag);

    /**
     * Removes  Tag from database
     *
     * @param id Tag id
     */
    void delete(Long id);

    /**
     * Saves  set of Tags in database
     *
     * @param tags set of Tags
     * @return set of Tags saved in database
     */
    Set<Tag> saveTags(Set<Tag> tags);
}
