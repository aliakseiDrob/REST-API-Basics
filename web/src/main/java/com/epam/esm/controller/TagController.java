package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller provides CRD operations on Tag entity.
 */
@RestController
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Finds all tags
     *
     * @return the list of all tags
     */
    @GetMapping("/tags")
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    /**
     * Finds Tag by id
     *
     * @param id the id of Tag entity
     * @return the Tag with queried id
     */
    @GetMapping("/tags/{id}")
    public Tag getTag(@PathVariable Long id) {
        return tagService.getById(id);
    }

    /**
     * Saves Tag
     *
     * @param tag Tag entity
     * @return id the added Tag
     */
    @PostMapping("/tags")
    public Long save(@RequestBody Tag tag) {
        return tagService.save(tag);
    }

    /**
     * Removes Tag
     *
     * @param id the id of Tag to remove
     */
    @DeleteMapping("/tags/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.delete(id);
    }
}
