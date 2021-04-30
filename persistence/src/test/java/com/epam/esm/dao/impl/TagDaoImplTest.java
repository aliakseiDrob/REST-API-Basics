package com.epam.esm.dao.impl;

import com.epam.esm.config.SpringJdbcConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.config.SpringJdbcConfigTest;
import com.epam.esm.dao.exception.DataException;
import com.epam.esm.dao.exception.EntityNotFoundException;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitWebConfig(SpringJdbcConfig.class)
@ContextConfiguration(
        classes = {SpringJdbcConfigTest.class},
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class TagDaoImplTest {

    private static final List<Tag> TAG_LIST = Arrays.asList(new Tag(1L, "first"), new Tag(2L, "second"));
    private static final List<Tag> TAG_LIST_AFTER_SAVE_TAG = Arrays.asList(new Tag(1L, "first"),
            new Tag(2L, "second"), new Tag(3L, "third"));
    private static final List<Tag> TAG_LIST_AFTER_DELETE_TAG = Collections.singletonList(new Tag(2L, "second"));
    private static final Tag TAG_INSTANCE = new Tag(1L, "first");
    private static final GiftCertificate CERTIFICATE_INSTANCE = new GiftCertificate(1L, "first", "for men",
            new BigDecimal(128), 11, null, null);
    private static final Tag TAG_INSTANCE_FOR_SAVE = new Tag("third");
    private static Set<Tag> setTagsForSave;
    private static List<Tag> listTagsAfterSaveInDb;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TagDao tagDao;

    private static void setTagInstanceForSave() {
        setTagsForSave = new HashSet<>();
        setTagsForSave.add(new Tag(5L, "third"));
        setTagsForSave.add(new Tag(7L, "fourth"));
    }

    private static void setTagInstanceAfterSave() {
        listTagsAfterSaveInDb = Arrays.asList(new Tag(1L, "first"), new Tag(2L, "second"),
                new Tag(3L, "third"), new Tag(4L, "fourth"));
    }

    @Test
    @Rollback
    public void testGetAllShouldReturnListTags() {
        assertEquals(tagDao.getAll(), TAG_LIST);
    }

    @Test
    @Rollback
    public void testGetByIdShouldReturnTag() {
        assertEquals(TAG_INSTANCE, tagDao.getById(1L));
    }

    @Test
    @Rollback
    public void testGetByIdShouldReturnThrowExceptionWhenTagNotExist() {
        assertThrows(EntityNotFoundException.class, () -> tagDao.getById(3L));
    }

    @Test
    @Rollback
    void getAllGiftCertificateTags() {
        assertEquals(tagDao.getAllGiftCertificateTags(CERTIFICATE_INSTANCE), Collections.singletonList(new Tag(1L, "first")));
    }

    @Test
    @Rollback
    void testGetByNameShouldReturnTag() {
        assertEquals(TAG_INSTANCE, tagDao.getByName("first"));
    }

    @Test
    @Rollback
    public void testGetByNameShouldReturnThrowExceptionWhenTagNotExist() {
        assertThrows(EntityNotFoundException.class, () -> tagDao.getByName("some name"));
    }

    @Test
    @Rollback
    void testSaveShouldSaveTag() {
        tagDao.save(TAG_INSTANCE_FOR_SAVE);
        assertEquals(TAG_LIST_AFTER_SAVE_TAG, tagDao.getAll());
    }

    @Test
    @Rollback
    void testSaveShouldThrowExceptionWhenAlreadyExist() {
        assertThrows(DataException.class, () -> tagDao.save(TAG_INSTANCE));
    }

    @Test
    @Rollback
    void testSaveShouldThrowExceptionWhenDataNotValid() {
        assertThrows(DataException.class, () -> tagDao.save(new Tag()));
    }

    @Test
    @Rollback
    void TestDeleteShouldDeleteTag() {
        tagDao.delete(1L);
        assertEquals(TAG_LIST_AFTER_DELETE_TAG, tagDao.getAll());
    }

    @Test
    @Rollback
    void testSaveTagsShouldReturnSavedTags() {
        setTagInstanceForSave();
        setTagInstanceAfterSave();
        assertEquals(tagDao.saveTags(setTagsForSave), setTagsForSave);
        assertEquals(tagDao.getAll(), listTagsAfterSaveInDb);
    }
}
