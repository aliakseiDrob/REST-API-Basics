package com.epam.esm.service.impl;

import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    private static final List<Tag> TAG_LIST = Arrays.asList(new Tag(1L, "first"), new Tag(2L, "second"));
    private static final Tag TAG_INSTANCE = new Tag(3L, "third");
    private static final GiftCertificate CERTIFICATE_INSTANCE = new GiftCertificate();
    private static final long TAG_ID_VALUE = 1L;

    @Mock
    private TagDaoImpl tagDao;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    public void testGetAllShouldReturnListTags() {
        when(tagDao.getAll()).thenReturn(TAG_LIST);
        assertEquals(tagService.getAll(), TAG_LIST);
        verify(tagDao).getAll();
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    public void testGetAllTagsByCertificateShouldReturnListTags() {
        when(tagDao.getAllGiftCertificateTags(CERTIFICATE_INSTANCE)).thenReturn(TAG_LIST);
        assertEquals(tagService.getAllTagsByCertificate(CERTIFICATE_INSTANCE), TAG_LIST);
        verify(tagDao).getAllGiftCertificateTags(CERTIFICATE_INSTANCE);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    public void testGetByIdTestShouldReturnTag() {
        when(tagDao.getById(anyLong())).thenReturn(TAG_INSTANCE);
        assertEquals(tagService.getById(anyLong()), TAG_INSTANCE);
        verify(tagDao).getById(anyLong());
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    public void testSaveTestShouldReturnTagsId() {
        when(tagDao.save(TAG_INSTANCE)).thenReturn(TAG_ID_VALUE);
        assertEquals(tagService.save(TAG_INSTANCE), TAG_ID_VALUE);
        verify(tagDao).save(TAG_INSTANCE);
        verifyNoMoreInteractions(tagDao);
    }

    @Test
    public void testDelete() {
        tagService.delete(TAG_ID_VALUE);
        verify(tagDao).delete(TAG_ID_VALUE);
        verifyNoMoreInteractions(tagDao);
    }
}
