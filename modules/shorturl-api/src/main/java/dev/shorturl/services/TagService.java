package dev.shorturl.services;

import dev.shorturl.dto.TagDTO;
import dev.shorturl.exception.TagNotFoundException;
import dev.shorturl.model.Tag;
import dev.shorturl.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public TagService(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
      this.modelMapper = modelMapper;
    }

    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = modelMapper.map(tagDTO, Tag.class);
        Tag savedTag = tagRepository.save(tag);
        return modelMapper.map(savedTag, TagDTO.class);
    }

    @Transactional(readOnly = true)
    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new TagNotFoundException(id));
        return modelMapper.map(tag, TagDTO.class);
    }

    @Transactional(readOnly = true)
    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
            .map(tag -> modelMapper.map(tag, TagDTO.class))
            .collect(Collectors.toList());
    }

    public TagDTO updateTag(TagDTO tagDTO) {
        if (!tagRepository.existsById(tagDTO.getId())) {
            throw new TagNotFoundException(tagDTO.getId());
        }
        Tag tag = modelMapper.map(tagDTO, Tag.class);
        Tag updatedTag = tagRepository.save(tag);
        return modelMapper.map(updatedTag, TagDTO.class);
    }

    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new TagNotFoundException(id);
        }
        tagRepository.deleteById(id);
    }
}