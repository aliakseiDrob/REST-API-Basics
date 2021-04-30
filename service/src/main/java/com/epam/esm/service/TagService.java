package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;

/**
 * Interface for serving Tag objects according to the business logic of Tag
 */
public interface TagService {

    /**
     * Returns all Tags
     *
     * @return list of Tags
     */
    List<Tag> getAll();

    /**
     * Returns all Tags by GiftCertificate
     *
     * @return list of Tags
     */
    List<Tag> getAllTagsByCertificate(GiftCertificate giftCertificate);

    /**
     * Returns Tag by id
     *
     * @param id Tag id
     * @return Tag object
     */
    Tag getById(long id);

    /**
     * Saves  Tag
     *
     * @param tag Tag entity
     * @return Tag id
     */
    Long save(Tag tag);

    /**
     * Removes  Tag
     *
     * @param id Tag id
     */
    void delete(Long id);
}
