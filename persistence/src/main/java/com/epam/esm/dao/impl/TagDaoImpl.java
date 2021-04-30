package com.epam.esm.dao.impl;

import com.epam.esm.dao.exception.DataException;
import com.epam.esm.dao.exception.EntityNotFoundException;
import com.epam.esm.dao.exception.TypeErrorCode;
import com.epam.esm.dao.utils.DaoUtils;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String GET_ALL_TAGS = "SELECT * FROM tag";
    private static final String GET_TAG_BY_ID = "SELECT * FROM tag WHERE id = :id";
    private static final String GET_BY_NAME = "SELECT * FROM tag WHERE name = :name";
    private static final String SAVE_TAG = "INSERT INTO tag (name) values (:name)";
    private static final String DELETE_TAG = "DELETE FROM tag WHERE id = :id";
    private static final String GET_ALL_CERTIFICATE_TAGS = "SELECT tag.id, tag.name FROM tag " +
            "JOIN gift_certificate_tag gct ON tag.id = gct.tag_id " +
            "JOIN gift_certificate ON gift_certificate.id = gct.gift_certificate_id " +
            "WHERE gift_certificate.id = :id";
    private static final RowMapper<Tag> mapper = (rs, mapRow) -> new Tag(rs.getLong("id"),
            rs.getString("name"));
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public TagDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Tag> getAll() {
        return namedParameterJdbcTemplate.query(GET_ALL_TAGS, mapper);
    }

    @Override
    public List<Tag> getAllGiftCertificateTags(GiftCertificate giftCertificate) {
        return namedParameterJdbcTemplate.query(GET_ALL_CERTIFICATE_TAGS, new BeanPropertySqlParameterSource(giftCertificate), mapper);
    }

    @Override
    public Tag getById(Long id) {
        try {
            return namedParameterJdbcTemplate.queryForObject(GET_TAG_BY_ID, Collections.singletonMap("id", id), mapper);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(TypeErrorCode.TAG_NOT_FOUND.getErrorCode());
        }
    }

    @Override
    public Tag getByName(String name) {
        try {
            return  namedParameterJdbcTemplate.queryForObject(GET_BY_NAME, Collections.singletonMap("name", name), mapper);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(TypeErrorCode.TAG_NOT_FOUND.getErrorCode());
        }
    }

    @Override
    public Long save(Tag tag) {
        DaoUtils.validateEmptyTagName(tag.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            namedParameterJdbcTemplate.update(SAVE_TAG, new BeanPropertySqlParameterSource(tag), keyHolder, new String[]{"id"});
        } catch (DuplicateKeyException ex) {
            throw new DataException(TypeErrorCode.TAG_NAME_DUPLICATE.getErrorCode());
        }
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void delete(Long id) {
        namedParameterJdbcTemplate.update(DELETE_TAG, Collections.singletonMap("id", id));
    }

    @Override
    public Set<Tag> saveTags(Set<Tag> tags) {
        saveNewTags(tags);
        getAllTagsByCertificate(tags);
        return tags;
    }

    private void saveNewTags(Set<Tag> tags) {
        tags.stream()
                .filter(tag -> !getAll().contains(tag))
                .forEach(tag -> tag.setId(save(tag)));
    }

    private void getAllTagsByCertificate(Set<Tag> tags) {
        List<Tag> allTags = getAll();
        tags.stream()
                .filter(allTags::contains)
                .forEach(tag -> tag.setId(allTags.get(allTags.indexOf(tag)).getId()));
    }
}
