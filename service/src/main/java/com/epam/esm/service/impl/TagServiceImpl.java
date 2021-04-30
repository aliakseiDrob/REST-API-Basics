package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> getAll() {
        return tagDao.getAll();
    }

    @Override
    public List<Tag> getAllTagsByCertificate(GiftCertificate giftCertificate) {
        return tagDao.getAllGiftCertificateTags(giftCertificate);
    }

    @Override
    public Tag getById(long id) {
        return tagDao.getById(id);
    }

    @Override
    public Long save(Tag tag) {
        return tagDao.save(tag);
    }

    @Override
    public void delete(Long id) {
        tagDao.delete(id);
    }
}
